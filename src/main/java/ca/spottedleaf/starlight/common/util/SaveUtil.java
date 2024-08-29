package ca.spottedleaf.starlight.common.util;

import ca.spottedleaf.starlight.common.chunk.ExtendedChunk;
import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.light.StarLightEngine;
import ca.spottedleaf.starlight.common.world.ExtendedSerializableChunkData;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.ListIterator;

public final class SaveUtil {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int STARLIGHT_LIGHT_VERSION = 9;

    public static int getLightVersion() {
        return STARLIGHT_LIGHT_VERSION;
    }

    private static final String BLOCKLIGHT_STATE_TAG = "starlight.blocklight_state";
    private static final String SKYLIGHT_STATE_TAG = "starlight.skylight_state";
    private static final String STARLIGHT_VERSION_TAG = "starlight.light_version";

    public static void prepareSaveLightHook(final ChunkAccess chunk, final SerializableChunkData data) {
        try {
            prepareSaveLightHookReal(chunk, data);
        } catch (final Throwable ex) {
            // failing to inject is not fatal so we catch anything here. if it fails, it will have correctly set lit to false
            // for Vanilla to relight on load and it will not set our lit tag so we will relight on load
            if (ex instanceof ThreadDeath) {
                throw (ThreadDeath)ex;
            }
            LOGGER.warn("Failed to inject light data into save data for chunk " + chunk.getPos() + ", chunk light will be recalculated on its next load", ex);
        }
    }

    private static void prepareSaveLightHookReal(final ChunkAccess chunk, final SerializableChunkData data) {
        // strip existing lighting data
        ListIterator<SerializableChunkData.SectionData> iterator = data.sectionData().listIterator(); // mutable in vanilla
        while (iterator.hasNext()) {
            SerializableChunkData.SectionData sectionData = iterator.next();
            iterator.set(new SerializableChunkData.SectionData(sectionData.y(), sectionData.chunkSection(), null, null));
        }

        // store lighting data
        ((ExtendedSerializableChunkData) (Object) data).scalablelux$setBlockLight(
                Arrays.stream(((ExtendedChunk) chunk).getBlockNibbles())
                        .map(SWMRNibbleArray::getSaveState)
                        .toArray(SWMRNibbleArray.SaveState[]::new)
        );
        ((ExtendedSerializableChunkData) (Object) data).scalablelux$setSkyLight(
                Arrays.stream(((ExtendedChunk) chunk).getSkyNibbles())
                        .map(SWMRNibbleArray::getSaveState)
                        .toArray(SWMRNibbleArray.SaveState[]::new)
        );
    }

    public static void saveLightHook(final SerializableChunkData data, final CompoundTag nbt) {
        try {
            saveLightHookReal(data, nbt);
        } catch (final Throwable ex) {
            // failing to inject is not fatal so we catch anything here. if it fails, it will have correctly set lit to false
            // for Vanilla to relight on load and it will not set our lit tag so we will relight on load
            if (ex instanceof ThreadDeath) {
                throw (ThreadDeath)ex;
            }
            LOGGER.warn("Failed to inject light data into save data for chunk " + data.chunkPos() + ", chunk light will be recalculated on its next load", ex);
        }
    }

    private static void saveLightHookReal(final SerializableChunkData data, final CompoundTag tag) {
        if (tag == null) {
            return;
        }

        // light sections are exclusive
        final int minSection = data.minSectionY() - 1; // exclusive

        SWMRNibbleArray.SaveState[] blockNibbles = ((ExtendedSerializableChunkData) (Object) data).scalablelux$getBlockLight();
        SWMRNibbleArray.SaveState[] skyNibbles = ((ExtendedSerializableChunkData) (Object) data).scalablelux$getSkyLight();

        final int maxSection = minSection + blockNibbles.length - 1; // exclusive

        boolean lit = data.lightCorrect();
        // diff start - store our tag for whether light data is init'd
        if (lit) {
            tag.putBoolean("isLightOn", false);
        }
        // diff end - store our tag for whether light data is init'd
        ChunkStatus status = ChunkStatus.byName(tag.getString("Status"));

        CompoundTag[] sections = new CompoundTag[maxSection - minSection + 1];

        ListTag sectionsStored = tag.getList("sections", 10);

        for (int i = 0; i < sectionsStored.size(); ++i) {
            CompoundTag sectionStored = sectionsStored.getCompound(i);
            int k = sectionStored.getByte("Y");

            // strip light data
            sectionStored.remove("BlockLight");
            sectionStored.remove("SkyLight");

            if (!sectionStored.isEmpty()) {
                sections[k - minSection] = sectionStored;
            }
        }

        if (lit && status.isOrAfter(ChunkStatus.LIGHT)) {
            for (int i = minSection; i <= maxSection; ++i) {
                SWMRNibbleArray.SaveState blockNibble = blockNibbles[i - minSection];
                SWMRNibbleArray.SaveState skyNibble = skyNibbles[i - minSection];
                if (blockNibble != null || skyNibble != null) {
                    CompoundTag section = sections[i - minSection];
                    if (section == null) {
                        section = new CompoundTag();
                        section.putByte("Y", (byte)i);
                        sections[i - minSection] = section;
                    }

                    // we store under the same key so mod programs editing nbt
                    // can still read the data, hopefully.
                    // however, for compatibility we store chunks as unlit so vanilla
                    // is forced to re-light them if it encounters our data. It's too much of a burden
                    // to try and maintain compatibility with a broken and inferior skylight management system.

                    if (blockNibble != null) {
                        if (blockNibble.data != null) {
                            section.putByteArray("BlockLight", blockNibble.data);
                        }
                        section.putInt(BLOCKLIGHT_STATE_TAG, blockNibble.state);
                    }

                    if (skyNibble != null) {
                        if (skyNibble.data != null) {
                            section.putByteArray("SkyLight", skyNibble.data);
                        }
                        section.putInt(SKYLIGHT_STATE_TAG, skyNibble.state);
                    }
                }
            }
        }

        // rewrite section list
        sectionsStored.clear();
        for (CompoundTag section : sections) {
            if (section != null) {
                sectionsStored.add(section);
            }
        }
        tag.put("sections", sectionsStored);
        if (lit) {
            tag.putInt(STARLIGHT_VERSION_TAG, STARLIGHT_LIGHT_VERSION); // only mark as fully lit after we have successfully injected our data
        }
    }

    public static void prepareLoadLightHook(final LevelHeightAccessor levelHeightAccessor, final CompoundTag tag, final SerializableChunkData data) {
        try {
            prepareLoadLightHookReal(levelHeightAccessor, tag, data);
        } catch (final Throwable ex) {
            // failing to inject is not fatal so we catch anything here. if it fails, then we simply relight. Not a problem, we get correct
            // lighting in both cases.
            if (ex instanceof ThreadDeath) {
                throw (ThreadDeath)ex;
            }
            LOGGER.warn("Failed to load light for chunk " + data.chunkPos() + ", light will be recalculated", ex);
        }
    }

    private static void prepareLoadLightHookReal(final LevelHeightAccessor world, final CompoundTag tag, final SerializableChunkData data) {
        final int minSection = WorldUtil.getMinLightSection(world);
        final int maxSection = WorldUtil.getMaxLightSection(world);

        // mark as unlit in case we fail parsing
        ((ExtendedSerializableChunkData) (Object) data).scalablelux$setLightCorrect(false);
        ((ExtendedSerializableChunkData) (Object) data).scalableLux$setActuallyCorrect(false);

        SWMRNibbleArray.SaveState[] blockLight = StarLightEngine.getFilledEmptySaveState(world);
        SWMRNibbleArray.SaveState[] skyLight = StarLightEngine.getFilledEmptySaveState(world);

        boolean lit = tag.get("isLightOn") != null && tag.getInt(STARLIGHT_VERSION_TAG) == STARLIGHT_LIGHT_VERSION;
        // not enough context: assumes always reads skylight
        ChunkStatus status = data.chunkStatus();

        if (lit && status.isOrAfter(ChunkStatus.LIGHT)) {
            ListTag sections = tag.getList("sections", 10);

            for (int i = 0; i < sections.size(); ++i) {
                CompoundTag sectionData = sections.getCompound(i);
                int y = sectionData.getByte("Y");

                if (sectionData.contains("BlockLight", 7)) {
                    blockLight[y - minSection] = new SWMRNibbleArray.SaveState(sectionData.getByteArray("BlockLight").clone(), sectionData.getInt(BLOCKLIGHT_STATE_TAG)); // clone for data safety
                } else {
                    blockLight[y - minSection] = new SWMRNibbleArray.SaveState(null, sectionData.getInt(BLOCKLIGHT_STATE_TAG));
                }

                if (sectionData.contains("SkyLight", 7)) {
                    // we store under the same key so mod programs editing nbt
                    // can still read the data, hopefully.
                    // however, for compatibility we store chunks as unlit so vanilla
                    // is forced to re-light them if it encounters our data. It's too much of a burden
                    // to try and maintain compatibility with a broken and inferior skylight management system.
                    skyLight[y - minSection] = new SWMRNibbleArray.SaveState(sectionData.getByteArray("SkyLight").clone(), sectionData.getInt(SKYLIGHT_STATE_TAG)); // clone for data safety
                } else {
                    skyLight[y - minSection] = new SWMRNibbleArray.SaveState(null, sectionData.getInt(SKYLIGHT_STATE_TAG));
                }
            }
        }

        ((ExtendedSerializableChunkData) (Object) data).scalablelux$setBlockLight(blockLight);
        ((ExtendedSerializableChunkData) (Object) data).scalablelux$setSkyLight(skyLight);

        ((ExtendedSerializableChunkData) (Object) data).scalableLux$setActuallyCorrect(lit);
    }

    public static void loadLightHook(final Level world, final SerializableChunkData data, final ChunkAccess into) {
        try {
            loadLightHookReal(world, data, into);
        } catch (final Throwable ex) {
            // failing to inject is not fatal so we catch anything here. if it fails, then we simply relight. Not a problem, we get correct
            // lighting in both cases.
            if (ex instanceof ThreadDeath) {
                throw (ThreadDeath)ex;
            }
            LOGGER.warn("Failed to load light for chunk " + data.chunkPos() + ", light will be recalculated", ex);
        }
    }

    private static void loadLightHookReal(final Level world, final SerializableChunkData data, final ChunkAccess into) {
        if (into == null) {
            return;
        }
        final int minSection = WorldUtil.getMinLightSection(world);
        final int maxSection = WorldUtil.getMaxLightSection(world);

        into.setLightCorrect(false); // mark as unlit in case we fail parsing

        SWMRNibbleArray[] blockNibbles = Arrays.stream(((ExtendedSerializableChunkData) (Object) data).scalablelux$getBlockLight())
                .map(state -> new SWMRNibbleArray(state.data, state.state))
                .toArray(SWMRNibbleArray[]::new);
        SWMRNibbleArray[] skyNibbles = Arrays.stream(((ExtendedSerializableChunkData) (Object) data).scalablelux$getSkyLight())
                .map(state -> new SWMRNibbleArray(state.data, state.state))
                .toArray(SWMRNibbleArray[]::new);

        boolean lit = ((ExtendedSerializableChunkData) (Object) data).scalablelux$getActuallyCorrect();

        ((ExtendedChunk)into).setBlockNibbles(blockNibbles);
        ((ExtendedChunk)into).setSkyNibbles(skyNibbles);

        into.setLightCorrect(lit); // now we set lit here, only after we've correctly parsed data
    }

    private SaveUtil() {}
}
