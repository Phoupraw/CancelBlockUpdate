package phoupraw.mcmod.cancelblockupdate.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;

@ApiStatus.Internal
public final class CBUModInitializer implements ModInitializer {

@SuppressWarnings("ResultOfMethodCallIgnored")
private static void loadClasses() {
    CBUGameRules.CACHE.hashCode();

}

@Override
public void onInitialize() {
    loadClasses();
    ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(CBUGameRules.CACHE.getOrDefault(origin, false));
        ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
    });
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal(CancelBlockUpdate.MOD_ID)
        .then(CommandManager.literal("schedule")
          .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
            .executes(context -> {
                BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                ServerWorld world = context.getSource().getWorld();
                BlockState blockState = world.getBlockState(pos);
                //noinspection deprecation
                blockState.getBlock().scheduledTick(blockState, world, pos, world.getRandom());
                return 1;
            })))
        .then(CommandManager.literal("random")
          .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
            .executes(context -> {
                BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                ServerWorld world = context.getSource().getWorld();
                BlockState blockState = world.getBlockState(pos);
                //noinspection deprecation
                blockState.getBlock().randomTick(blockState, world, pos, world.getRandom());
                return 1;
            })))
      /*.then(CommandManager.literal("place")
        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
          .executes(context -> {
              BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
              ServerWorld world = context.getSource().getWorld();
              BlockState blockState = world.getBlockState(pos);
              blockState.getBlock().getPlacementState(new ItemPlacementContext(world,context.getSource().getPlayer(), Hand.MAIN_HAND,blockState.getBlock().asItem().getDefaultStack(),new BlockHitResult(Vec3d.ofCenter(pos),)))
              blockState.getBlock().scheduledTick(blockState, world, pos, world.getRandom());
              return 1;
          })))*/));
    ServerWorldEvents.LOAD.register((server, world) -> CBUGameRules.CACHE.put(world, server.getGameRules().getBoolean(CBUGameRules.KEY_OFF)));
    //ServerChunkEvents.CHUNK_LOAD.register(new ServerChunkEvents.Load() {
    //    @Override
    //    public void onChunkLoad(ServerWorld world, WorldChunk chunk) {
    //        CBUGameRules.CACHE.put(chunk, world.getServer().getGameRules().getBoolean(CBUGameRules.KEY_OFF));
    //    }
    //});
}

}
