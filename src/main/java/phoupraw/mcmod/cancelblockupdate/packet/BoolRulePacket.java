package phoupraw.mcmod.cancelblockupdate.packet;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.GameRules;
import phoupraw.mcmod.cancelblockupdate.registry.CBUPacketTypes;
import phoupraw.mcmod.cancelblockupdate.registry.CBURegistries;

import java.util.Objects;
/**
 @see CBUPacketTypes */
public class BoolRulePacket implements FabricPacket {

    public static BoolRulePacket of(PacketByteBuf buf) {
        int rawId = buf.readByte();
        return new BoolRulePacket(Objects.requireNonNull(CBURegistries.BOOL_RULE.get(rawId), "rawId=" + rawId), buf.readBoolean());
    }

    public final GameRules.Key<GameRules.BooleanRule> key;
    public final boolean value;

    public BoolRulePacket(GameRules.Key<GameRules.BooleanRule> key, boolean value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(CBURegistries.BOOL_RULE.getRawId(key));
        buf.writeBoolean(value);
    }

    @Override
    public PacketType<?> getType() {
        return CBUPacketTypes.BOOL_RULE;
    }

}
