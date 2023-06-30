package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public final class CBUClientModInitializer implements ClientModInitializer {

    ///**
    // 在玩家加入服务器时，给玩家添加频道监听器，让玩家能够解析服务端发来的信包，并由此同步游戏规则值。
    // @see Join#onPlayReady
    // */
    //private static void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
    //    ClientPlayNetworking.registerReceiver(CBUIdentifiers.CHANNEL, (client1, handler1, buf, responseSender) -> {
    //        var key = CBURegistries.GAME_RULE.get(buf.readByte());
    //        CBUGameRules.CACHES.get(key).put(client1.world, buf.readBoolean());
    //    });
    //}

    ///**
    // 我猜不移除监听器也应该不会有什么事，但是以防万一，还是移除监听器比较好。
    // @see Disconnect#onPlayDisconnect
    // */
    //private static void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
    //    ClientPlayNetworking.unregisterReceiver(CBUIdentifiers.CHANNEL);
    //}

    @Override
    public void onInitializeClient() {
        //ClientPlayConnectionEvents.JOIN.register(CBUClientModInitializer::onPlayReady);
        //ClientPlayConnectionEvents.DISCONNECT.register(CBUClientModInitializer::onPlayDisconnect);

        ClientPlayNetworking.registerGlobalReceiver(CBUIdentifiers.CHANNEL, (client, handler, buf, responseSender) -> {
            var key = CBURegistries.BOOL_RULE.get(buf.readByte());
            CBUGameRules.CACHES.get(key).put(client.world, buf.readBoolean());
        });
    }

}
