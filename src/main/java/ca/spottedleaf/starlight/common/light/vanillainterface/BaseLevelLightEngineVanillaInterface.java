package ca.spottedleaf.starlight.common.light.vanillainterface;

import ca.spottedleaf.starlight.common.chunk.ExtendedChunk;
import ca.spottedleaf.starlight.common.light.ClientStarLightLightingProvider;
import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.light.StarLightEngine;
import ca.spottedleaf.starlight.common.light.StarLightInterface;
import ca.spottedleaf.starlight.common.light.StarLightLightingProvider;
import ca.spottedleaf.starlight.common.util.CoordinateUtils;
import ca.spottedleaf.starlight.common.util.WorldUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LayerLightEventListener;
import net.minecraft.world.level.lighting.LayerLightSectionStorage;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.jspecify.annotations.Nullable;

public class BaseLevelLightEngineVanillaInterface extends LevelLightEngine implements StarLightLightingProvider, ClientStarLightLightingProvider {

    protected final StarLightInterface lightEngine;
    protected final LongOpenHashSet lightingEnabledChunks = new LongOpenHashSet();
    protected final Long2ObjectOpenHashMap<SWMRNibbleArray[]> blockLightMap = new Long2ObjectOpenHashMap<>();
    protected final Long2ObjectOpenHashMap<SWMRNibbleArray[]> skyLightMap = new Long2ObjectOpenHashMap<>();

    public BaseLevelLightEngineVanillaInterface(LightChunkGetter chunkSource, boolean hasBlockLight, boolean hasSkyLight) {
        super(chunkSource, false, false);

        // avoid ClassCastException in cases where custom LightChunkGetters do not return a Level from getLevel()
        if (chunkSource.getLevel() instanceof Level) {
            this.lightEngine = new StarLightInterface(chunkSource, hasSkyLight, hasBlockLight, this);
        } else {
            this.lightEngine = new StarLightInterface(null, hasSkyLight, hasBlockLight, this);
        }
    }

    @Override
    public void checkBlock(BlockPos pos) {
        CommonLightEngineUtils.checkBlock(this, pos);
    }

    @Override
    public boolean hasLightWork() {
        return CommonLightEngineUtils.hasLightWork(this);
    }

    @Override
    public int runLightUpdates() {
        return CommonLightEngineUtils.runLightUpdates(this);
    }

    @Override
    public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) {
        CommonLightEngineUtils.updateSectionStatus(this, pos, sectionEmpty);
    }

    @Override
    public void setLightEnabled(ChunkPos pos, boolean enable) {
        CommonLightEngineUtils.setLightEnabled(this, pos, enable);
    }

    @Override
    public void propagateLightSources(ChunkPos pos) {
        CommonLightEngineUtils.propagateLightSources(this, pos);
    }

    @Override
    public LayerLightEventListener getLayerListener(LightLayer layer) {
        return CommonLightEngineUtils.getLayerListener(this, layer);
    }

    @Override
    public String getDebugData(LightLayer layer, SectionPos pos) {
        return CommonLightEngineUtils.getDebugData(this, layer, pos);
    }

    @Override
    public LayerLightSectionStorage.SectionType getDebugSectionType(LightLayer layer, SectionPos pos) {
        return CommonLightEngineUtils.getDebugSectionType(this, layer, pos);
    }

    @Override
    public void queueSectionData(LightLayer layer, SectionPos pos, @Nullable DataLayer data) {
        CommonLightEngineUtils.queueSectionData(this, layer, pos, data);
    }

    @Override
    public void retainData(ChunkPos pos, boolean retain) {
        CommonLightEngineUtils.retainData(this, pos, retain);
    }

    @Override
    public int getRawBrightness(BlockPos pos, int skyDampen) {
        return CommonLightEngineUtils.getRawBrightness(this, pos, skyDampen);
    }

    @Override
    public boolean lightOnInColumn(long sectionZeroNode) {
        return CommonLightEngineUtils.lightOnInColumn(this, sectionZeroNode);
    }

    @Override
    public int getLightSectionCount() {
        return super.getLightSectionCount(); // use vanilla impl
    }

    @Override
    public int getMinLightSection() {
        return super.getMinLightSection(); // use vanilla impl
    }

    @Override
    public int getMaxLightSection() {
        return super.getMaxLightSection(); // use vanilla impl
    }

    @Override
    public void updateSectionStatus(BlockPos pos, boolean sectionEmpty) {
        super.updateSectionStatus(pos, sectionEmpty); // use vanilla impl
    }

    @Override
    public StarLightInterface scalablelux$getLightEngine() {
        return this.lightEngine;
    }

    @Override
    public LongOpenHashSet scalablelux$getLightingEnabledChunks() {
        return this.lightingEnabledChunks;
    }

    @Override
    public Long2ObjectOpenHashMap<SWMRNibbleArray[]> scalablelux$getBlockLightMap() {
        return this.blockLightMap;
    }

    @Override
    public Long2ObjectOpenHashMap<SWMRNibbleArray[]> scalablelux$getSkyLightMap() {
        return this.skyLightMap;
    }

    @Override
    public void scalablelux$clientUpdateLight(final LightLayer lightType, final SectionPos pos,
                                              final DataLayer nibble, final boolean trustEdges) {
        // data storage changed with new light impl
        final ChunkAccess chunk = this.scalablelux$getLightEngine().getAnyChunkNow(pos.getX(), pos.getZ());
        switch (lightType) {
            case BLOCK: {
                final SWMRNibbleArray[] blockNibbles = this.blockLightMap.computeIfAbsent(CoordinateUtils.getChunkKey(pos), (final long keyInMap) -> {
                    return StarLightEngine.getFilledEmptyLight(this.lightEngine.getWorld());
                });

                blockNibbles[pos.getY() - WorldUtil.getMinLightSection(this.lightEngine.getWorld())] = SWMRNibbleArray.fromVanilla(nibble);

                if (chunk != null) {
                    ((ExtendedChunk)chunk).scalablelux$setBlockNibbles(blockNibbles);
                    this.lightEngine.getLightAccess().onLightUpdate(LightLayer.BLOCK, pos);
                }
                break;
            }
            case SKY: {
                final SWMRNibbleArray[] skyNibbles = this.skyLightMap.computeIfAbsent(CoordinateUtils.getChunkKey(pos), (final long keyInMap) -> {
                    return StarLightEngine.getFilledEmptyLight(this.lightEngine.getWorld());
                });

                skyNibbles[pos.getY() - WorldUtil.getMinLightSection(this.lightEngine.getWorld())] = SWMRNibbleArray.fromVanilla(nibble);

                if (chunk != null) {
                    ((ExtendedChunk)chunk).scalablelux$setSkyNibbles(skyNibbles);
                    this.lightEngine.getLightAccess().onLightUpdate(LightLayer.SKY, pos);
                }
                break;
            }
        }
    }

    @Override
    public void scalablelux$clientRemoveLightData(final ChunkPos chunkPos) {
        this.blockLightMap.remove(CoordinateUtils.getChunkKey(chunkPos));
        this.skyLightMap.remove(CoordinateUtils.getChunkKey(chunkPos));
    }

    @Override
    public void scalablelux$clientChunkLoad(final ChunkPos pos, final LevelChunk chunk) {
        final long key = CoordinateUtils.getChunkKey(pos);
        final SWMRNibbleArray[] blockNibbles = this.blockLightMap.get(key);
        final SWMRNibbleArray[] skyNibbles = this.skyLightMap.get(key);
        if (blockNibbles != null) {
            ((ExtendedChunk)chunk).scalablelux$setBlockNibbles(blockNibbles);
        }
        if (skyNibbles != null) {
            ((ExtendedChunk)chunk).scalablelux$setSkyNibbles(skyNibbles);
        }
    }
}
