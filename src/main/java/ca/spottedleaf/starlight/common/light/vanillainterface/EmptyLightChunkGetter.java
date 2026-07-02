package ca.spottedleaf.starlight.common.light.vanillainterface;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class EmptyLightChunkGetter implements LightChunkGetter {
    public static final EmptyLightChunkGetter INSTANCE = new EmptyLightChunkGetter();

    private EmptyLightChunkGetter() {
    }

    @Override
    public @Nullable LightChunk getChunkForLighting(int x, int z) {
        return null;
    }

    @Override
    public @NonNull BlockGetter getLevel() {
        return EmptyBlockGetter.INSTANCE;
    }
}
