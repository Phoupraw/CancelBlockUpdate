package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Join;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public final class CBUClientModInitializer implements ClientModInitializer {

/**
 在玩家加入服务器时，给玩家添加频道监听器，让玩家能够解析服务端发来的信包，并由此同步游戏规则值。
 @see Join#onPlayReady */
private static void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
    ClientPlayNetworking.registerReceiver(CBUIdentifiers.CHANNEL, (client1, handler1, buf, responseSender) -> {
        switch (buf.readByte()) {
            case CBUGameRules.CODE_OFF -> CBUGameRules.CACHE_OFF.put(client1.world, buf.readBoolean());
            case CBUGameRules.CODE_REPL -> CBUGameRules.CACHE_REPL.put(client1.world, buf.readBoolean());
        }
    });
}

/**
 我猜不移除监听器也应该不会有什么事，但是以防万一，还是移除监听器比较好。
 @see Disconnect#onPlayDisconnect */
private static void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
    ClientPlayNetworking.unregisterReceiver(CBUIdentifiers.CHANNEL);
}

@Override
public void onInitializeClient() {
    ClientPlayConnectionEvents.JOIN.register(CBUClientModInitializer::onPlayReady);
    ClientPlayConnectionEvents.DISCONNECT.register(CBUClientModInitializer::onPlayDisconnect);
}

}
