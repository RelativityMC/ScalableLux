package ca.spottedleaf.starlight.mixin.common.lightengine;

import ca.spottedleaf.starlight.common.light.StarLightLightingProvider;
import ca.spottedleaf.starlight.common.light.vanillainterface.CommonLightEngineUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThreadedLevelLightEngine.class)
public class ThreadedLevelLightEngineMixin {
    @WrapOperation(method = "runUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/lighting/LevelLightEngine;runLightUpdates()I"))
    private int redirectUpdate(ThreadedLevelLightEngine instance, Operation<Integer> original) {
        if (this instanceof StarLightLightingProvider starLightLightingProvider) {
            return CommonLightEngineUtils.runLightUpdates(starLightLightingProvider);
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "tryScheduleUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/lighting/LevelLightEngine;hasLightWork()Z"))
    private boolean redirectHasUpdates(ThreadedLevelLightEngine instance, Operation<Boolean> original) {
        if (this instanceof StarLightLightingProvider starLightLightingProvider) {
            return starLightLightingProvider.scalablelux$getLightEngine().needsScheduling();
        } else {
            return original.call(instance);
        }
    }
}
