package ca.spottedleaf.starlight.mixin.client.multiplayer;

import ca.spottedleaf.starlight.common.light.ClientStarLightLightingProvider;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.core.SectionPos;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacketData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class, priority = 1001)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl implements ClientGamePacketListener, TickablePacketListener {

    protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    /*
      The call behaviors in the packet handler are much more clear about how they should affect the light engine,
      and as a result makes the client light load/unload more reliable
    */

    @Shadow
    private ClientLevel level;

    /*
      Now in 1.18 Mojang has added logic to delay rendering chunks until their lighting is ready (as they are delaying
      light updates). Fortunately for us, Starlight doesn't take any kind of hit loading in light data. So we have no reason
      to delay the light updates at all (and we shouldn't delay them or else desync might occur - such as with block updates).
     */

    @Shadow
    protected abstract void applyLightData(final int chunkX, final int chunkZ, final ClientboundLightUpdatePacketData clientboundLightUpdatePacketData, boolean markDirty);

    @Shadow
    protected abstract void enableChunkLight(final LevelChunk levelChunk, final int chunkX, final int chunkZ);

    /**
     * Call the runnable immediately to prevent desync
     * @author Spottedleaf
     */
    @WrapOperation(
            method = "handleLightUpdatePacket",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;queueLightUpdate(Ljava/lang/Runnable;)V"
            )
    )
    private void starlightCallUpdateImmediately(final ClientLevel instance, final Runnable runnable, final Operation<Void> original) {
        if (this.level.getChunkSource().getLightEngine() instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            runnable.run();
        } else {
            original.call(instance, runnable);
        }
    }

    /**
     * Re-route light update packet to our own logic
     * @author Spottedleaf
     */
    @WrapOperation(
            method = "readSectionList",
            at = @At(
                    target = "Lnet/minecraft/world/level/lighting/LevelLightEngine;queueSectionData(Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/chunk/DataLayer;)V",
                    value = "INVOKE",
                    ordinal = 0
            )
    )
    private void loadLightDataHook(final LevelLightEngine lightEngine, final LightLayer lightType, final SectionPos pos,
                                   final @Nullable DataLayer nibble, final Operation<Void> original) {
        if (this.level.getChunkSource().getLightEngine() instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            clientStarLightLightingProvider.scalablelux$clientUpdateLight(lightType, pos, nibble, true);
        } else {
            original.call(lightEngine, lightType, pos, nibble);
        }
    }


    /**
     * Avoid calling Vanilla's logic here, and instead call our own.
     * @author Spottedleaf
     */
    @WrapOperation(
            method = "handleForgetLevelChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;queueLightRemoval(Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)V"
            )
    )
    private void unloadLightDataHook(final ClientPacketListener instance, final ClientboundForgetLevelChunkPacket packet, final Operation<Void> original) {
        if (this.level.getChunkSource().getLightEngine() instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            clientStarLightLightingProvider.scalablelux$clientRemoveLightData(new ChunkPos(packet.pos().x(), packet.pos().z()));
        } else {
            original.call(instance, packet);
        }
    }

    /**
     * Don't call vanilla's load logic
     */
    @WrapOperation(
            method = "handleLevelChunkWithLight",
            at = @At(
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;queueLightUpdate(Ljava/lang/Runnable;)V",
                    value = "INVOKE",
                    ordinal = 0
            )
    )
    private void postChunkLoadHookRedirect(final ClientLevel instance, final Runnable runnable, Operation<Void> original) {
        if (this.level.getChunkSource().getLightEngine() instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            // don't call vanilla's logic, see below
        } else {
            original.call(instance, runnable);
        }
    }

    /**
     * Hook for loading in a chunk to the world
     * @author Spottedleaf
     */
    @Inject(
            method = "handleLevelChunkWithLight",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postChunkLoadHook(final ClientboundLevelChunkWithLightPacket packet, final CallbackInfo ci) {
        if (this.level.getChunkSource().getLightEngine() instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            final int chunkX = packet.getX();
            final int chunkZ = packet.getZ();
            final LevelChunk chunk = this.level.getChunkSource().getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
            if (chunk == null) {
                // failed to load
                return;
            }
            // load in light data from packet immediately
            this.applyLightData(chunkX, chunkZ, packet.getLightData(), true);
            clientStarLightLightingProvider.scalablelux$clientChunkLoad(new ChunkPos(chunkX, chunkZ), chunk);

            // we need this for the update chunk status call, so that it can tell starlight what sections are empty and such
            this.enableChunkLight(chunk, chunkX, chunkZ);

            // vanilla no longer need this since 26.2
            // this.minecraft.levelRenderer.onChunkReadyToRender(chunk.getPos());
        }
    }
}
