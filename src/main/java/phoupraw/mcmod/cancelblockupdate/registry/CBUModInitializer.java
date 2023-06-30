package phoupraw.mcmod.cancelblockupdate.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents.AfterPlayerChange;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents.Load;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;
import phoupraw.mcmod.cancelblockupdate.packet.BoolRulePacket;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
public final class CBUModInitializer implements ModInitializer {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void loadClasses() {
        CBUGameRules.CACHES.hashCode();

    }

    /**
     在一个服务端世界被载入时，将其放入缓存。虽然说即使没有此方法，{@link CBUGameRules#getOff}也可以处理那些新加载的世界，但是还是用这个方法比较好。
     @see Load#onWorldLoad
     */
    private static void onWorldLoad(MinecraftServer server, ServerWorld world) {
        for (var key : CBURegistries.BOOL_RULE) {
            CBUGameRules.CACHES.get(key).put(world, server.getGameRules().getBoolean(key));
        }
    }

    /**
     玩家转移到新世界后，客户端世界会被换新，导致缓存中的客户端世界失效，因此需要把新的客户端世界加入到缓存中。
     @see AfterPlayerChange#afterChangeWorld
     */
    private static void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        List<BoolRulePacket> packets = new LinkedList<>();
        var server = Objects.requireNonNull(player.getServer(), "player=" + player);
        for (var key : CBURegistries.BOOL_RULE) {
            packets.add(new BoolRulePacket(key, server.getGameRules().getBoolean(key)));
        }
        for (var packet : packets) {
            ServerPlayNetworking.send(player, packet);
        }
    }

    /**
     注册指令，目前只有schedule和random两条子命令。
     @see CommandRegistrationCallback#register
     */
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
        //ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
        //    List<PacketByteBuf> bufs = new LinkedList<>();
        //    for (var key : CBURegistries.BOOL_RULE) {
        //        PacketByteBuf buf = PacketByteBufs.create();
        //        buf.writeByte(CBURegistries.BOOL_RULE.getRawId(key));
        //        buf.writeBoolean(server.getGameRules().getBoolean(key));
        //        bufs.add(buf);
        //    }
        //    for (PacketByteBuf buf : bufs) {
        //        ServerPlayNetworking.send(handler.player, CBUIdentifiers.CHANNEL, buf);
        //    }
        //});
        ServerPlayNetworking.registerGlobalReceiver(CBUPacketTypes.CLIENT_JOIN, (packet, player, responseSender) -> {
            List<BoolRulePacket> packets = new LinkedList<>();
            var server = Objects.requireNonNull(player.getServer(), "player=" + player);
            for (var key : CBURegistries.BOOL_RULE) {
                packets.add(new BoolRulePacket(key, server.getGameRules().getBoolean(key)));
            }
            for (var p : packets) {
                ServerPlayNetworking.send(player, p);
            }
        });
        //ServerChunkEvents.CHUNK_LOAD.register(new ServerChunkEvents.Load() {
        //    @Override
        //    public void onChunkLoad(ServerWorld world, WorldChunk chunk) {
        //        CBUGameRules.CACHE.put(chunk, world.getServer().getGameRules().getBoolean(CBUGameRules.KEY_OFF));
        //    }
        //});
    }

}
