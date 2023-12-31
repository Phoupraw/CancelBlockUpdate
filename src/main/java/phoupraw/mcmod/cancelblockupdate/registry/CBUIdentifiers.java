package phoupraw.mcmod.cancelblockupdate.registry;

import net.minecraft.util.Identifier;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;

public final class CBUIdentifiers {

    public static final Identifier
      CHANNEL = of("channel"),
      OFF = of("off"),
      REPLACE = of("replace"),
      BOOL_RULE = of("bool_rule");

    public static Identifier of(String path) {
        return new Identifier(CancelBlockUpdate.MOD_ID, path);
    }

    private CBUIdentifiers() {
    }

}
