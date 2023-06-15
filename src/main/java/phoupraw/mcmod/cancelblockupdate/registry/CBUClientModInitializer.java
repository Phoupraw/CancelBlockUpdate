package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public final class CBUClientModInitializer implements ClientModInitializer {

@Override
public void onInitializeClient() {
    ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ClientPlayNetworking.registerReceiver(CBUIdentifiers.CHANNEL, (client1, handler1, buf, responseSender) -> CBUGameRules.CACHE.put(client1.world, buf.readBoolean())));
    ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientPlayNetworking.unregisterReceiver(CBUIdentifiers.CHANNEL));
}

}
