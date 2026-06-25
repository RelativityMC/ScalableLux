package ca.spottedleaf.starlight.common.light;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;

public interface StarLightLightingProvider {

    public StarLightInterface scalablelux$getLightEngine();

    public void scalablelux$clientUpdateLight(final LightLayer lightType, final SectionPos pos,
                                              final DataLayer nibble, final boolean trustEdges);

    public void scalablelux$clientRemoveLightData(final ChunkPos chunkPos);

    public void scalablelux$clientChunkLoad(final ChunkPos pos, final LevelChunk chunk);

}
