package ca.spottedleaf.starlight.mixin.common.chunk;

import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.chunk.ExtendedChunk;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.PalettedContainerFactory;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ImposterProtoChunk.class)
public abstract class ImposterProtoChunkMixin extends ProtoChunk implements ExtendedChunk {

    @Final
    @Shadow
    private LevelChunk wrapped;

    public ImposterProtoChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory palettedContainerFactory, @Nullable BlendingData blendingData) {
        super(chunkPos, upgradeData, levelHeightAccessor, palettedContainerFactory, blendingData);
    }

    @Override
    public SWMRNibbleArray[] scalablelux$getBlockNibbles() {
        return ((ExtendedChunk)this.wrapped).scalablelux$getBlockNibbles();
    }

    @Override
    public void scalablelux$setBlockNibbles(final SWMRNibbleArray[] nibbles) {
        ((ExtendedChunk)this.wrapped).scalablelux$setBlockNibbles(nibbles);
    }

    @Override
    public SWMRNibbleArray[] scalablelux$getSkyNibbles() {
        return ((ExtendedChunk)this.wrapped).scalablelux$getSkyNibbles();
    }

    @Override
    public void scalablelux$setSkyNibbles(final SWMRNibbleArray[] nibbles) {
        ((ExtendedChunk)this.wrapped).scalablelux$setSkyNibbles(nibbles);
    }

    @Override
    public boolean[] scalablelux$getSkyEmptinessMap() {
        return ((ExtendedChunk)this.wrapped).scalablelux$getSkyEmptinessMap();
    }

    @Override
    public void scalablelux$setSkyEmptinessMap(final boolean[] emptinessMap) {
        ((ExtendedChunk)this.wrapped).scalablelux$setSkyEmptinessMap(emptinessMap);
    }

    @Override
    public boolean[] scalablelux$getBlockEmptinessMap() {
        return ((ExtendedChunk)this.wrapped).scalablelux$getBlockEmptinessMap();
    }

    @Override
    public void scalablelux$setBlockEmptinessMap(final boolean[] emptinessMap) {
        ((ExtendedChunk)this.wrapped).scalablelux$setBlockEmptinessMap(emptinessMap);
    }
}
