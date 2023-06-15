package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

import java.util.Map;
import java.util.WeakHashMap;

public final class CBUGameRules {

public static final Map<Object, Boolean> CACHE = new WeakHashMap<>();
public static final GameRules.Key<GameRules.BooleanRule> KEY_OFF = GameRuleRegistry.register(CBUIdentifiers.of("off").toString(), GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false, (server, booleanRule) -> {
    for (ServerWorld world : server.getWorlds()) {
        CACHE.put(world, booleanRule.get());
    }
    PacketByteBuf buf = PacketByteBufs.create();
    buf.writeBoolean(booleanRule.get());
    for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
        ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
    }
}));

private CBUGameRules() {
}

}
