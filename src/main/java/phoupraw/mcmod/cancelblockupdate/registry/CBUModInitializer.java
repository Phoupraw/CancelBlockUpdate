package phoupraw.mcmod.cancelblockupdate.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
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

    private static void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        List<PacketByteBuf> bufs = new LinkedList<>();
        var server = Objects.requireNonNull(player.getServer(), "player=" + player);
        for (var key : CBURegistries.BOOL_RULE) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeByte(CBURegistries.BOOL_RULE.getRawId(key));
            buf.writeBoolean(server.getGameRules().getBoolean(key));
            bufs.add(buf);
        }
        for (var buf : bufs) {
            ServerPlayNetworking.send(player, CBUIdentifiers.CHANNEL, buf);
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
              }))));
    }

    @Override
    public void onInitialize() {
        loadClasses();
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(CBUModInitializer::afterChangeWorld);
        CommandRegistrationCallback.EVENT.register(CBUModInitializer::register);
        ServerWorldEvents.LOAD.register(CBUModInitializer::onWorldLoad);
        ServerPlayNetworking.registerGlobalReceiver(CBUIdentifiers.CHANNEL, (server, player, handler, emptyBuf, responseSender) -> {
            List<PacketByteBuf> bufs = new LinkedList<>();
            for (var key : CBURegistries.BOOL_RULE) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeByte(CBURegistries.BOOL_RULE.getRawId(key));
                buf.writeBoolean(server.getGameRules().getBoolean(key));
                bufs.add(buf);
            }
            for (var buf : bufs) {
                responseSender.sendPacket(CBUIdentifiers.CHANNEL, buf);
            }
        });
    }

}
