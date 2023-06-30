package phoupraw.mcmod.cancelblockupdate;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phoupraw.mcmod.cancelblockupdate.registry.CBUGameRules;

public final class CancelBlockUpdate {

    public static final String MOD_ID = "cancelblockupdate";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void setCanReplace(ItemPlacementContext context, CallbackInfoReturnable<Boolean> cir) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        VoxelShape shape = world.getBlockState(pos).getOutlineShape(world, pos);
        System.out.println(shape);
        if (!CBUGameRules.get(CBUGameRules.REPLACE, world) && !shape.isEmpty()) {
            cir.setReturnValue(false);
        }
    }

}
