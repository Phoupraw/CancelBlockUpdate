package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class CBUModInitializer implements ModInitializer {

@SuppressWarnings("ResultOfMethodCallIgnored")
private static void loadClasses() {
    CBUGameRules.CACHE.hashCode();

}

@Override
public void onInitialize() {
    loadClasses();
    ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(CBUGameRules.CACHE.getOrDefault(origin, false));
        ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
    });
}

}
