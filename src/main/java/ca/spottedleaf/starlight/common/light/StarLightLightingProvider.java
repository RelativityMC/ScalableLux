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

}
