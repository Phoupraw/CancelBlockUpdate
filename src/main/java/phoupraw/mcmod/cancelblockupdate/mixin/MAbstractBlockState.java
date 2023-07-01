package phoupraw.mcmod.cancelblockupdate.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phoupraw.mcmod.cancelblockupdate.registry.CBUGameRules;

@Mixin(AbstractBlock.AbstractBlockState.class)
abstract class MAbstractBlockState {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    public abstract VoxelShape getRaycastShape(BlockView world, BlockPos pos);

    //以下是取消方块更新
    @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"), cancellable = true)
    private void cancelGetStateForNeighborUpdate(Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (!CBUGameRules.getOff(world)) {
            //noinspection ConstantConditions
            cir.setReturnValue((BlockState) (Object) this);
        }
    }

    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
    private void cancelNeighborUpdate(World world, BlockPos pos, Block block, BlockPos posFrom, boolean notify, CallbackInfo ci) {
        if (!CBUGameRules.getOff(world)) {
            ci.cancel();
        }
    }

    @Inject(method = "updateNeighbors*", at = @At("HEAD"), cancellable = true)
    private void cancelUpdateNeighbors(WorldAccess world, BlockPos pos, int flags, CallbackInfo ci) {
        if (!CBUGameRules.getOff(world)) {
            ci.cancel();
        }
    }

    //以下是取消计划刻
    @Inject(method = "scheduledTick", at = @At("HEAD"), cancellable = true)
    private void cancelScheduledTick(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!CBUGameRules.getOff(world)) {
            ci.cancel();
        }
    }

    //以下是强制允许放置
    @Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
    private void passCanPlaceAt(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!CBUGameRules.getOff(world)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void cancelRandomTick(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!CBUGameRules.getOff(world)) {
            ci.cancel();
        }
    }

    //@Inject(method = "isReplaceable", at = @At("HEAD"), cancellable = true)
    //private void notReplaceable(CallbackInfoReturnable<Boolean> cir) {
    //    //World world = context.getWorld();
    //    if (!CBUGameRules.get(CBUGameRules.CACHE_REPL, CBUGameRules.KEY_REPL, world) && !getRaycastShape(world, context.getBlockPos()).isEmpty()) {
    //        cir.setReturnValue(false);
    //    }
    //}
    @Inject(method = "canReplace", at = @At("HEAD"), cancellable = true)
    private void setCanReplace(ItemPlacementContext context, CallbackInfoReturnable<Boolean> cir) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        VoxelShape shape = world.getBlockState(pos).getOutlineShape(world, pos);
        if (!CBUGameRules.get(CBUGameRules.REPLACE, world) && !shape.isEmpty()) {
            cir.setReturnValue(false);
        }
    }

}
