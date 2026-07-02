package ca.spottedleaf.starlight.mixin.common.lightengine;

import ca.spottedleaf.starlight.common.light.StarLightLightingProvider;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.lighting.LightEngine;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLightEngine.class)
public abstract class LevelLightEngineMixin {

    @Shadow
    @Nullable
    private LightEngine<?, ?> blockEngine;

    @Shadow
    @Nullable
    private LightEngine<?, ?> skyEngine;

    /**
     *
     * TODO since this is a constructor inject, check on update for new constructors
     */
    @Inject(
            method = "<init>*", at = @At("TAIL")
    )
    public void construct(final CallbackInfo ci) {
        if (this instanceof StarLightLightingProvider) {
            // intentionally destroy mods hooking into old light engine state
            this.blockEngine = null;
            this.skyEngine = null;
        }
    }

}
