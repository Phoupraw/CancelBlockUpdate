package phoupraw.mcmod.cancelblockupdate.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents.AfterPlayerChange;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents.Load;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;

@ApiStatus.Internal
public final class CBUModInitializer implements ModInitializer {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void loadClasses() {
        CBUGameRules.CACHE_OFF.hashCode();

    }

    /**
     在一个服务端世界被载入时，将其放入缓存{@link CBUGameRules#CACHE_OFF}。虽然说即使没有此方法，{@link CBUGameRules#getOff}也可以处理那些新加载的世界，但是还是用这个方法比较好。
     @see Load#onWorldLoad
     */
    private static void onWorldLoad(MinecraftServer server, ServerWorld world) {
        CBUGameRules.CACHE_OFF.put(world, server.getGameRules().getBoolean(CBUGameRules.KEY_OFF));
    }

    /**
     玩家转移到新世界后，客户端世界会被换新，导致缓存{@link CBUGameRules#CACHE_OFF}中的客户端世界失效，因此需要把新的客户端世界加入到缓存中。
     @see AfterPlayerChange#afterChangeWorld
     */
    private static void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeByte(CBUGameRules.CODE_OFF);
        buf.writeBoolean(CBUGameRules.CACHE_OFF.getOrDefault(origin, false));
        ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
        buf = PacketByteBufs.create();
        buf.writeByte(CBUGameRules.CODE_REPL);
        buf.writeBoolean(CBUGameRules.CACHE_REPL.getOrDefault(origin, false));
        ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
    }

/**
 注册指令，目前只有schedule和random两条子命令。
 @see CommandRegistrationCallback#register */
private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
    dispatcher.register(CommandManager.literal(CancelBlockUpdate.MOD_ID)
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
          })))*/);
}

@Override
public void onInitialize() {
    loadClasses();
    ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(CBUModInitializer::afterChangeWorld);
    CommandRegistrationCallback.EVENT.register(CBUModInitializer::register);
    ServerWorldEvents.LOAD.register(CBUModInitializer::onWorldLoad);
    //ServerChunkEvents.CHUNK_LOAD.register(new ServerChunkEvents.Load() {
    //    @Override
    //    public void onChunkLoad(ServerWorld world, WorldChunk chunk) {
    //        CBUGameRules.CACHE.put(chunk, world.getServer().getGameRules().getBoolean(CBUGameRules.KEY_OFF));
    //    }
    //});
}

}
