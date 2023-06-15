package phoupraw.mcmod.cancelblockupdate.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phoupraw.mcmod.cancelblockupdate.registry.CBUGameRules;

/**
 阻止铁轨在放置时更新形状。 */
@Mixin(AbstractRailBlock.class)
class MAbstractRailBlock {

@Inject(method = "updateCurves", at = @At("HEAD"), cancellable = true)
private void cancelUpdateCurves(BlockState state, World world, BlockPos pos, boolean notify, CallbackInfoReturnable<BlockState> cir) {
    if (!CBUGameRules.CACHE.getOrDefault(world, false)) {
        cir.setReturnValue(state);
    }
}

}
