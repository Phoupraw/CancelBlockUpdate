package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.GameRules;

public final class CBURegistries {

    public static final RegistryKey<Registry<GameRules.Key<GameRules.BooleanRule>>> BOOL_RULE_KEY = RegistryKey.ofRegistry(CBUIdentifiers.BOOL_RULE);
    public static final SimpleRegistry<GameRules.Key<GameRules.BooleanRule>> BOOL_RULE = FabricRegistryBuilder.createSimple((Class<GameRules.Key<GameRules.BooleanRule>>) null, CBUIdentifiers.BOOL_RULE).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    private CBURegistries() {
    }

}
