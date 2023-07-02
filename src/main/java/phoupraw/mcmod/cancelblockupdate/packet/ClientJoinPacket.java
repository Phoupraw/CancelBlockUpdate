package phoupraw.mcmod.cancelblockupdate.packet;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import phoupraw.mcmod.cancelblockupdate.registry.CBUPacketTypes;
/**
 @see CBUPacketTypes */
public class ClientJoinPacket implements FabricPacket {

    public static final ClientJoinPacket INSTANCE = new ClientJoinPacket();

    public static ClientJoinPacket of(PacketByteBuf buf) {
        return INSTANCE;
    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public PacketType<?> getType() {
        return CBUPacketTypes.CLIENT_JOIN;
    }

}
