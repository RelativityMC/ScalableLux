package ca.spottedleaf.starlight.mixin.common.chunk;

import ca.spottedleaf.starlight.common.chunk.ExtendedChunk;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements ExtendedChunk {

    /**
     * Copies the nibble data from the protochunk.
     * TODO since this is a constructor inject, check for new constructors on update.
     */
    @Inject(
            method = "<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ProtoChunk;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;)V",
            at = @At("TAIL")
    )
    public void onTransitionToFull(ServerLevel serverLevel, ProtoChunk protoChunk, LevelChunk.PostLoadProcessor postLoadProcessor, CallbackInfo ci) {
        this.scalablelux$setBlockNibbles(((ExtendedChunk)protoChunk).scalablelux$getBlockNibbles());
        this.scalablelux$setSkyNibbles(((ExtendedChunk)protoChunk).scalablelux$getSkyNibbles());
        this.scalablelux$setSkyEmptinessMap(((ExtendedChunk)protoChunk).scalablelux$getSkyEmptinessMap());
        this.scalablelux$setBlockEmptinessMap(((ExtendedChunk)protoChunk).scalablelux$getBlockEmptinessMap());
    }

    /**
     * @reason Remove unused skylight sources
     * @author Spottedleaf
     */
    @Redirect(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/lighting/ChunkSkyLightSources;update(Lnet/minecraft/world/level/BlockGetter;III)Z"
            )
    )
    private boolean skipLightSources(final ChunkSkyLightSources instance, final BlockGetter blockGetter,
                                     final int x, final int y, final int z) {
        return instance != null ? instance.update(blockGetter, x, y, z) : false; // handle unmanaged lighting
    }
}
