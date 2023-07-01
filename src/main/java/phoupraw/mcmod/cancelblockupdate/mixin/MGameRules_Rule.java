package phoupraw.mcmod.cancelblockupdate.mixin;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import phoupraw.mcmod.cancelblockupdate.inject.IGameRules_Rule;

@Mixin(GameRules.Rule.class)
class MGameRules_Rule implements IGameRules_Rule {

    @Shadow
    @Final
    protected GameRules.Type<?> type;

    @SuppressWarnings("unchecked")
    public <T extends GameRules.Rule<T>> GameRules.Type<T> getType() {
        return (GameRules.Type<T>) type;
    }

}
