package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import org.jetbrains.annotations.ApiStatus;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;

import java.util.Objects;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public final class CBUClientModInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> sender.sendPacket(CBUIdentifiers.CHANNEL, PacketByteBufs.empty()));
        ClientPlayNetworking.registerGlobalReceiver(CBUIdentifiers.CHANNEL, (client, handler, buf, responseSender) -> {
            var key = CBURegistries.BOOL_RULE.get(buf.readByte());
            boolean value = buf.readBoolean();
            var player = Objects.requireNonNull(client.player, client.toString());
            CBUGameRules.CACHES.get(key).put(player.getWorld(), value);
            CancelBlockUpdate.LOGGER.debug((Object) (player.getWorld() + "的" + key + "已改为" + value));
        });
    }

}
