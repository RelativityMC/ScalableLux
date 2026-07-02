package ca.spottedleaf.starlight.common.light.vanillainterface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class EmptyBlockGetter implements BlockGetter {
    public static final EmptyBlockGetter INSTANCE = new EmptyBlockGetter();

    private EmptyBlockGetter() {
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(@NonNull BlockPos pos) {
        return null;
    }

    @Override
    public @NonNull BlockState getBlockState(@NonNull BlockPos pos) {
        return Blocks.VOID_AIR.defaultBlockState();
    }

    @Override
    public @NonNull FluidState getFluidState(@NonNull BlockPos pos) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }
}
