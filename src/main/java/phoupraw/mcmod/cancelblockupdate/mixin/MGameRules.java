package phoupraw.mcmod.cancelblockupdate.mixin;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRules.class)
class MGameRules {

    @Inject(method = "register", at = @At("RETURN"))
    private static <T extends GameRules.Rule<T>> void setKey(String name, GameRules.Category category, GameRules.Type<T> type, CallbackInfoReturnable<GameRules.Key<T>> cir) {
        type.setKey(cir.getReturnValue());
    }

}
