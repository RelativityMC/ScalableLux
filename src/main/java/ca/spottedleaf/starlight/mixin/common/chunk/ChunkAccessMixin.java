package ca.spottedleaf.starlight.mixin.common.chunk;

import ca.spottedleaf.starlight.common.chunk.ExtendedChunk;
import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.light.StarLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerFactory;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkAccess.class)
public abstract class ChunkAccessMixin implements ExtendedChunk {

    @Shadow
    protected ChunkSkyLightSources skyLightSources;


    @Unique
    private volatile SWMRNibbleArray[] scalablelux$blockNibbles;

    @Unique
    private volatile SWMRNibbleArray[] scalablelux$skyNibbles;

    @Unique
    private volatile boolean[] scalablelux$skyEmptinessMap;

    @Unique
    private volatile boolean[] scalablelux$blockEmptinessMap;

    @Override
    public SWMRNibbleArray[] scalablelux$getBlockNibbles() {
        return this.scalablelux$blockNibbles;
    }

    @Override
    public void scalablelux$setBlockNibbles(final SWMRNibbleArray[] nibbles) {
        this.scalablelux$blockNibbles = nibbles;
    }

    @Override
    public SWMRNibbleArray[] scalablelux$getSkyNibbles() {
        return this.scalablelux$skyNibbles;
    }

    @Override
    public void scalablelux$setSkyNibbles(final SWMRNibbleArray[] nibbles) {
        this.scalablelux$skyNibbles = nibbles;
    }

    @Override
    public boolean[] scalablelux$getSkyEmptinessMap() {
        return this.scalablelux$skyEmptinessMap;
    }

    @Override
    public void scalablelux$setSkyEmptinessMap(final boolean[] emptinessMap) {
        this.scalablelux$skyEmptinessMap = emptinessMap;
    }

    @Override
    public boolean[] scalablelux$getBlockEmptinessMap() {
        return this.scalablelux$blockEmptinessMap;
    }

    @Override
    public void scalablelux$setBlockEmptinessMap(final boolean[] emptinessMap) {
        this.scalablelux$blockEmptinessMap = emptinessMap;
    }

    /**
     * @reason Remove unused skylight sources, and initialise nibble arrays.
     * @author Spottedleaf
     */
    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            )
    )
    private void nullSources(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory palettedContainerFactory, long l, LevelChunkSection[] levelChunkSections, BlendingData blendingData, CallbackInfo ci) {
        this.skyLightSources = null;
        if (!((Object)this instanceof ImposterProtoChunk)) {
            this.scalablelux$setBlockNibbles(StarLightEngine.getFilledEmptyLight(levelHeightAccessor));
            this.scalablelux$setSkyNibbles(StarLightEngine.getFilledEmptyLight(levelHeightAccessor));
        }
    }

    /**
     * @reason Remove unused skylight sources
     * @author Spottedleaf
     */
    @Redirect(
            method = "initializeLightSources",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/lighting/ChunkSkyLightSources;fillFrom(Lnet/minecraft/world/level/chunk/ChunkAccess;)V"
            )
    )
    private void skipInit(final ChunkSkyLightSources instance, final ChunkAccess chunkAccess) {}
}