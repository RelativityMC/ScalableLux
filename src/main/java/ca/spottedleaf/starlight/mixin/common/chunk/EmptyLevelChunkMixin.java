package ca.spottedleaf.starlight.mixin.common.chunk;

import ca.spottedleaf.starlight.common.chunk.ExtendedChunk;
import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.light.StarLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyLevelChunk.class)
public abstract class EmptyLevelChunkMixin extends LevelChunk implements ExtendedChunk {

    public EmptyLevelChunkMixin(final Level level, final ChunkPos pos) {
        super(level, pos);
    }

    @Override
    public SWMRNibbleArray[] scalablelux$getBlockNibbles() {
        return StarLightEngine.getFilledEmptyLight(this.getLevel());
    }

    @Override
    public void scalablelux$setBlockNibbles(final SWMRNibbleArray[] nibbles) {}

    @Override
    public SWMRNibbleArray[] scalablelux$getSkyNibbles() {
        return StarLightEngine.getFilledEmptyLight(this.getLevel());
    }

    @Override
    public void scalablelux$setSkyNibbles(final SWMRNibbleArray[] nibbles) {}

    @Override
    public boolean[] scalablelux$getSkyEmptinessMap() {
        return null;
    }

    @Override
    public void scalablelux$setSkyEmptinessMap(final boolean[] emptinessMap) {}

    @Override
    public boolean[] scalablelux$getBlockEmptinessMap() {
        return null;
    }

    @Override
    public void scalablelux$setBlockEmptinessMap(final boolean[] emptinessMap) {}

    @Override
    public boolean scalablelux$usingStarlight() {
        return false;
    }
}
