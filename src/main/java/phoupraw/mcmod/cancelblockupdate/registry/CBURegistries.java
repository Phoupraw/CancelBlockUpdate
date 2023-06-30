package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.world.GameRules;

public final class CBURegistries {

    public static final RegistryKey<Registry<GameRules.Key<GameRules.BooleanRule>>> BOOL_RULE_KEY = RegistryKey.ofRegistry(CBUIdentifiers.of("bool_rule"));
    public static final SimpleRegistry<GameRules.Key<GameRules.BooleanRule>> BOOL_RULE = FabricRegistryBuilder.createSimple(BOOL_RULE_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    private CBURegistries() {
    }

}
