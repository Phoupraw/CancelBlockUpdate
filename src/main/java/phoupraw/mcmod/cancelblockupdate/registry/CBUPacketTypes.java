package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import phoupraw.mcmod.cancelblockupdate.packet.BoolRulePacket;

public final class CBUPacketTypes {

    public static final PacketType<BoolRulePacket> BOOL_RULE = PacketType.create(CBUIdentifiers.of("bool_rule"), BoolRulePacket::of);

    private CBUPacketTypes() {

    }

}
