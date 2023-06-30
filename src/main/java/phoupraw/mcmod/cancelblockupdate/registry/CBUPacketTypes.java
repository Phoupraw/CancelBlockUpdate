package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import phoupraw.mcmod.cancelblockupdate.packet.BoolRulePacket;
import phoupraw.mcmod.cancelblockupdate.packet.ClientJoinPacket;

public final class CBUPacketTypes {

    public static final PacketType<BoolRulePacket> BOOL_RULE = PacketType.create(CBUIdentifiers.of("bool_rule"), BoolRulePacket::of);
    public static final PacketType<ClientJoinPacket> CLIENT_JOIN = PacketType.create(CBUIdentifiers.of("client_join"), ClientJoinPacket::of);

    private CBUPacketTypes() {

    }

}
