package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.ServerWorldAccess;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;

import java.util.Map;
import java.util.WeakHashMap;

public final class CBUGameRules {

public static final Map<Object, Boolean> CACHE = new WeakHashMap<>();
public static final GameRules.Key<GameRules.BooleanRule> KEY_OFF = GameRuleRegistry.register(CBUIdentifiers.of("off").toString(), GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false, (server, booleanRule) -> {
    boolean newValue = booleanRule.get();
    for (ServerWorld world : server.getWorlds()) {
        CACHE.put(world, newValue);
    }
    PacketByteBuf buf = PacketByteBufs.create();
    buf.writeBoolean(newValue);
    for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
        ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
    }
}));

public static boolean get(Object key) {
    Boolean value = CACHE.get(key);
    if (value != null) return value;
    if (key instanceof ServerWorldAccess serverWorldAccess) {
        value = serverWorldAccess.toServerWorld().getServer().getGameRules().getBoolean(KEY_OFF);
    } else {
        value = false;
        CancelBlockUpdate.LOGGER.error("无法获取CACHE值！键：" + key);
    }
    CACHE.put(key, value);
    return value;
}

private CBUGameRules() {
}

}
