package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.*;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

public final class CBUGameRules {

    /**
     从世界到游戏规则的值的映射。客户端仅靠此来获取游戏规则值，而服务端如果检测到没有缓存，则会尝试获取服务器来获得游戏规则值并加入缓存。
     <br/>
     本模组修改的所有方法的形参都包含{@link World}、{@link WorldAccess}、{@link WorldView}等，所以以这个作为缓存的键比较合适。
     */
    public static final Map<WorldView, Boolean> CACHE_OFF = new WeakHashMap<>();
    public static final Map<WorldView, Boolean> CACHE_REPL = new WeakHashMap<>();
    public static final int CODE_OFF = 0;
    public static final int CODE_REPL = 1;
    public static final GameRules.Key<GameRules.BooleanRule> KEY_OFF = GameRuleRegistry.register(CBUIdentifiers.of("off").toString(), GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false, newCallback(CACHE_OFF, CODE_OFF)));
    public static final GameRules.Key<GameRules.BooleanRule> KEY_REPL = GameRuleRegistry.register(CBUIdentifiers.of("no_replace").toString(), GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false, newCallback(CACHE_REPL, CODE_REPL)));

    public static BiConsumer<MinecraftServer, GameRules.BooleanRule> newCallback(Map<WorldView, Boolean> cache, int code) {
        return (server, booleanRule) -> {
            boolean newValue = booleanRule.get();
            for (ServerWorld world : server.getWorlds()) {
                cache.put(world, newValue);
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeByte(code);
            buf.writeBoolean(newValue);
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
            }
        };
    }

    /**
     从{@link #CACHE_OFF}中获取游戏规则的值，如果为{@code null}，如果为{@link ServerWorldAccess}，则会从服务器获取规则的值，否则直接为{@code false}，将其添加到缓存中。
     @param world 键
     @return 游戏规则值。
     */
    public static boolean getOff(WorldView world) {
        return get(CACHE_OFF, KEY_OFF, world);
    }

    /**
     从{@link #CACHE_OFF}中获取游戏规则的值，如果为{@code null}，如果为{@link ServerWorldAccess}，则会从服务器获取规则的值，否则直接为{@code false}，将其添加到缓存中。
     @param world 键
     @return 游戏规则值。
     */
    public static boolean get(Map<WorldView, Boolean> cache, GameRules.Key<GameRules.BooleanRule> key, WorldView world) {
        Boolean value = cache.get(world);
        if (value != null) return value;
        if (world instanceof ServerWorldAccess serverWorldAccess) {
            value = serverWorldAccess.toServerWorld().getServer().getGameRules().getBoolean(key);
        } else {
            value = false;
            CancelBlockUpdate.LOGGER.error("无法获取" + key + "的CACHE值！" + world);
        }
        cache.put(world, value);
        return value;
    }

    private CBUGameRules() {
    }

}
