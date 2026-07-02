package ca.spottedleaf.starlight.common.light.vanillainterface;

import ca.spottedleaf.starlight.common.light.StarLightLightingProvider;
import ca.spottedleaf.starlight.common.util.CoordinateUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.lighting.LayerLightEventListener;
import net.minecraft.world.level.lighting.LayerLightSectionStorage;
import org.jspecify.annotations.Nullable;

public class CommonLightEngineUtils {
    private CommonLightEngineUtils() {
    }

    public static void checkBlock(StarLightLightingProvider instance, BlockPos pos) {
        instance.scalablelux$getLightEngine().blockChange(pos.immutable());
    }

    public static boolean hasLightWork(StarLightLightingProvider instance) {
        return instance.scalablelux$getLightEngine().hasUpdates();
    }

    public static int runLightUpdates(StarLightLightingProvider instance) {
        final boolean hadUpdates = hasLightWork(instance);
        instance.scalablelux$getLightEngine().propagateChanges();
        return hadUpdates ? 1 : 0;
    }

    public static void updateSectionStatus(StarLightLightingProvider instance, SectionPos pos, boolean sectionEmpty) {
        instance.scalablelux$getLightEngine().sectionChange(pos, sectionEmpty);
    }

    public static void setLightEnabled(StarLightLightingProvider instance, ChunkPos pos, boolean enable) {
        // store state for implementation of lightOnInColumn()
        // needed for proper culling of chunks in the client
        final long key = pos.pack();
        if (enable) {
            instance.scalablelux$getLightingEnabledChunks().add(key);
        } else {
            instance.scalablelux$getLightingEnabledChunks().remove(key);
        }
    }

    public static void propagateLightSources(StarLightLightingProvider instance, ChunkPos pos) {
        // not invoked by the client
    }

    public static LayerLightEventListener getLayerListener(StarLightLightingProvider instance, LightLayer layer) {
        return layer == LightLayer.BLOCK ? instance.scalablelux$getLightEngine().getBlockReader() : instance.scalablelux$getLightEngine().getSkyReader();
    }

    public static String getDebugData(StarLightLightingProvider instance, LightLayer layer, SectionPos pos) {
        // TODO would be nice to make use of this
        return "n/a";
    }

    public static LayerLightSectionStorage.SectionType getDebugSectionType(StarLightLightingProvider instance, LightLayer layer, SectionPos pos) {
        if (layer == LightLayer.BLOCK) {
            return instance.scalablelux$getLightEngine().hasSectionBlockLight(pos) ? LayerLightSectionStorage.SectionType.LIGHT_AND_DATA : LayerLightSectionStorage.SectionType.EMPTY;
        } else {
            return instance.scalablelux$getLightEngine().hasSectionSkyLight(pos) ? LayerLightSectionStorage.SectionType.LIGHT_AND_DATA : LayerLightSectionStorage.SectionType.EMPTY;
        }
    }

    public static void queueSectionData(StarLightLightingProvider instance, LightLayer layer, SectionPos pos, @Nullable DataLayer data) {
        // do not allow modification of data from the non-chunk load hooks
    }

    public static void retainData(StarLightLightingProvider instance, ChunkPos pos, boolean retain) {
        // not used by new light impl
    }

    public static int getRawBrightness(StarLightLightingProvider instance, BlockPos pos, int skyDampen) {
        // need to use new light hooks for this
        return instance.scalablelux$getLightEngine().getRawBrightness(pos, skyDampen);
    }

    public static boolean lightOnInColumn(StarLightLightingProvider instance, long pos) {
        final long key = CoordinateUtils.getChunkKey(SectionPos.x(pos), SectionPos.z(pos));
        return instance.scalablelux$getLightingEnabledChunks().contains(key) ||
                (!instance.scalablelux$getLightEngine().hasBlockLight() || instance.scalablelux$getBlockLightMap().get(key) != null) &&
                        (!instance.scalablelux$getLightEngine().hasSkyLight() || instance.scalablelux$getSkyLightMap().get(key) != null);
    }

}
