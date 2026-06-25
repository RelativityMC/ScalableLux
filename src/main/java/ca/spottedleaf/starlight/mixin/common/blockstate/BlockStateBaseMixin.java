package ca.spottedleaf.starlight.mixin.common.blockstate;

import ca.spottedleaf.starlight.common.blockstate.ExtendedAbstractBlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin extends StateHolder<Block, BlockState> implements ExtendedAbstractBlockState {

    @Shadow
    @Final
    private boolean useShapeForLightOcclusion;

    @Shadow
    @Final
    private boolean canOcclude;

    @Unique
    private boolean scalablelux$isConditionallyFullOpaque;

    protected BlockStateBaseMixin(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
        super(owner, propertyKeys, propertyValues);
    }

    /**
     * Initialises our light state for this block.
     */
    @Inject(
            method = "initCache",
            at = @At("RETURN")
    )
    public void initLightAccessState(final CallbackInfo ci) {
        this.scalablelux$isConditionallyFullOpaque = this.canOcclude & this.useShapeForLightOcclusion;
    }

    @Override
    public final boolean scalablelux$isConditionallyFullOpaque() {
        return this.scalablelux$isConditionallyFullOpaque;
    }

}
