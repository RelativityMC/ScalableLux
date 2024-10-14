package ca.spottedleaf.starlight.mixin.common.world;

import ca.spottedleaf.starlight.common.config.Config;
import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.util.SaveUtil;
import ca.spottedleaf.starlight.common.world.ExtendedSerializableChunkData;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SerializableChunkData.class)
public abstract class SerializableChunkDataMixin implements ExtendedSerializableChunkData {

    @Mutable
    @Shadow
    @Final
    private boolean lightCorrect;
    @Unique
    private SWMRNibbleArray.SaveState[] scalablelux$blocklight;
    @Unique
    private SWMRNibbleArray.SaveState[] scalablelux$skylight;
    @Unique
    private boolean scalablelux$actuallyCorrect;

    @Override
    public void scalablelux$setBlockLight(SWMRNibbleArray.SaveState[] light) {
        this.scalablelux$blocklight = light;
    }

    @Override
    public void scalablelux$setSkyLight(SWMRNibbleArray.SaveState[] light) {
        this.scalablelux$skylight = light;
    }

    @Override
    public void scalableLux$setActuallyCorrect(boolean correct) {
        this.scalablelux$actuallyCorrect = correct;
    }

    @Override
    public SWMRNibbleArray.SaveState[] scalablelux$getBlockLight() {
        return this.scalablelux$blocklight;
    }

    @Override
    public SWMRNibbleArray.SaveState[] scalablelux$getSkyLight() {
        return this.scalablelux$skylight;
    }

    @Override
    public boolean scalablelux$getActuallyCorrect() {
        return this.scalablelux$actuallyCorrect;
    }

    @Override
    public void scalablelux$setLightCorrect(boolean correct) {
        this.lightCorrect = correct;
    }

    /**
     * Overwrites vanilla's light data with our own.
     * TODO this needs to be checked on update to account for format changes
     */
    @Inject(
            method = "copyOf",
            at = @At("RETURN")
    )
    private static void prepareSaveLightHook(ServerLevel world, ChunkAccess chunk, CallbackInfoReturnable<SerializableChunkData> cir) {
        if (Config.USE_STARLIGHT_FORMAT) {
            SaveUtil.prepareSaveLightHook(chunk, cir.getReturnValue());
        } else {
            SaveUtil.prepareSaveVanillaLightHook(world, chunk, cir.getReturnValue());
        }
    }

    @Inject(
            method = "write",
            at = @At("RETURN")
    )
    private void saveLightHook(CallbackInfoReturnable<CompoundTag> cir) {
        if (Config.USE_STARLIGHT_FORMAT) {
            SaveUtil.saveLightHook((SerializableChunkData) (Object) this, cir.getReturnValue());
        }
    }

    @Inject(
            method = "parse",
            at = @At("RETURN")
    )
    private static void prepareLoadLightHook(LevelHeightAccessor levelHeightAccessor, RegistryAccess registryAccess, CompoundTag compoundTag, CallbackInfoReturnable<SerializableChunkData> cir) {
        if (Config.USE_STARLIGHT_FORMAT) {
            SaveUtil.prepareLoadLightHook(levelHeightAccessor, compoundTag, cir.getReturnValue());
        }
    }

    /**
     * Loads our light data into the returned chunk object from the tag.
     * TODO this needs to be checked on update to account for format changes
     */
    @Inject(
            method = "read",
            at = @At("RETURN")
    )
    private void loadLightHook(ServerLevel serverLevel, PoiManager poiManager, RegionStorageInfo regionStorageInfo, ChunkPos chunkPos, CallbackInfoReturnable<ProtoChunk> cir) {
        if (Config.USE_STARLIGHT_FORMAT) {
            SaveUtil.loadLightHook(serverLevel, (SerializableChunkData) (Object) this, cir.getReturnValue());
        } else {
            SaveUtil.loadVanillaLightHook(serverLevel, (SerializableChunkData) (Object) this, cir.getReturnValue());
        }
    }
}
