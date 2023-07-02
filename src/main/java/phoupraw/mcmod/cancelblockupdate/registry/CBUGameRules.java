package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

public final class CBUGameRules {

    /**
     从世界到游戏规则的值的映射。客户端仅靠此来获取游戏规则值，而服务端如果检测到没有缓存，则会尝试获取服务器来获得游戏规则值并加入缓存。
     <br/>
     本模组修改的所有方法的形参都包含{@link World}、{@link WorldAccess}、{@link WorldView}等，所以以这个作为缓存的键比较合适。
     */
    public static final Map<GameRules.Key<GameRules.BooleanRule>, Map<WorldView, Boolean>> CACHES;
    public static final BiConsumer<MinecraftServer, GameRules.BooleanRule> ON_CHANGE = CBUGameRules::onChange;
    public static final GameRules.Key<GameRules.BooleanRule> OFF = GameRuleRegistry.register(CBUIdentifiers.OFF.toString(), GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false, ON_CHANGE));
    public static final GameRules.Key<GameRules.BooleanRule> REPLACE = GameRuleRegistry.register(CBUIdentifiers.REPLACE.toString(), GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false, ON_CHANGE));

    static {
        Registry.register(CBURegistries.BOOL_RULE, CBUIdentifiers.OFF, OFF);
        Registry.register(CBURegistries.BOOL_RULE, CBUIdentifiers.REPLACE, REPLACE);
        Map<GameRules.Key<GameRules.BooleanRule>, Map<WorldView, Boolean>> map = new HashMap<>();
        for (var key : CBURegistries.BOOL_RULE) map.put(key, new WeakHashMap<>());
        CACHES = map;
    }
    public static void onChange(MinecraftServer server, GameRules.BooleanRule booleanRule) {
        boolean newValue = booleanRule.get();
        GameRules.Key<GameRules.BooleanRule> key = booleanRule.getType().getKey();
        for (ServerWorld world : server.getWorlds()) CACHES.get(key).put(world, newValue);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeByte(CBURegistries.BOOL_RULE.getRawId(key));
        buf.writeBoolean(newValue);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
        }
    }

    /**
     @see #get
     */
    public static boolean getOff(WorldView world) {
        return get(OFF, world);
    }

    /**
     从{@link #}中获取游戏规则的值，如果为{@code null}，如果为{@link ServerWorldAccess}，则会从服务器获取规则的值，否则直接为{@code false}，将其添加到缓存中。
     @param world 键
     @return 游戏规则值。
     */
    public static boolean get(GameRules.Key<GameRules.BooleanRule> key, WorldView world) {
        var cache = CACHES.get(key);
        Boolean value = cache.get(world);
        if (value != null) return value;
        if (world instanceof ServerWorldAccess serverWorldAccess) {
            value = serverWorldAccess.toServerWorld().getServer().getGameRules().getBoolean(key);
        } else {
            value = false;
            StringWriter writer = new StringWriter();
            new Throwable().printStackTrace(new PrintWriter(writer));
            CancelBlockUpdate.LOGGER.error("无法获取" + key + "的CACHE值！已设为false。" + world + System.lineSeparator() + writer);
            //CancelBlockUpdate.LOGGER.catching(new IllegalStateException());
        }
        cache.put(world, value);
        return value;
    }

    private CBUGameRules() {
    }

}
