package ca.spottedleaf.starlight.common.light;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;

public interface StarLightLightingProvider {

    StarLightInterface scalablelux$getLightEngine();

    LongOpenHashSet scalablelux$getLightingEnabledChunks();

    Long2ObjectOpenHashMap<SWMRNibbleArray[]> scalablelux$getBlockLightMap();

    Long2ObjectOpenHashMap<SWMRNibbleArray[]> scalablelux$getSkyLightMap();

    @Deprecated(forRemoval = true)
    default StarLightInterface getLightEngine() {
        return this.scalablelux$getLightEngine();
    }

    @Deprecated(forRemoval = true)
    default void clientUpdateLight(final LightLayer lightType, final SectionPos pos,
                                   final DataLayer nibble, final boolean trustEdges) {
        if (this instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            clientStarLightLightingProvider.scalablelux$clientUpdateLight(lightType, pos, nibble, trustEdges);
        } else {
            throw new UnsupportedOperationException("Not an instance of ClientStarLightLightingProvider");
        }
    }

    @Deprecated(forRemoval = true)
    default void clientRemoveLightData(final ChunkPos chunkPos) {
        if (this instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            clientStarLightLightingProvider.scalablelux$clientRemoveLightData(chunkPos);
        } else {
            throw new UnsupportedOperationException("Not an instance of ClientStarLightLightingProvider");
        }
    }

    @Deprecated(forRemoval = true)
    default void clientChunkLoad(final ChunkPos pos, final LevelChunk chunk) {
        if (this instanceof ClientStarLightLightingProvider clientStarLightLightingProvider) {
            clientStarLightLightingProvider.scalablelux$clientChunkLoad(pos, chunk);
        } else {
            throw new UnsupportedOperationException("Not an instance of ClientStarLightLightingProvider");
        }
    }

}
