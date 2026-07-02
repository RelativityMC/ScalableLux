package ca.spottedleaf.starlight.common.light;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;

public interface ClientStarLightLightingProvider {
    void scalablelux$clientUpdateLight(LightLayer lightType, SectionPos pos,
                                       DataLayer nibble, boolean trustEdges);

    void scalablelux$clientRemoveLightData(ChunkPos chunkPos);

    void scalablelux$clientChunkLoad(ChunkPos pos, LevelChunk chunk);
}
