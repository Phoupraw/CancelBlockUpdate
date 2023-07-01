package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.jetbrains.annotations.ApiStatus;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;
import phoupraw.mcmod.cancelblockupdate.packet.ClientJoinPacket;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public final class CBUClientModInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> sender.sendPacket(ClientJoinPacket.INSTANCE));
        ClientPlayNetworking.registerGlobalReceiver(CBUPacketTypes.BOOL_RULE, (packet, player, responseSender) -> {
            var key = packet.key;
            boolean value = packet.value;
            CBUGameRules.CACHES.get(key).put(player.getWorld(), value);
            CancelBlockUpdate.LOGGER.debug((Object) (player.getWorld() + "的" + key + "已改为" + value));
        });
    }

}
