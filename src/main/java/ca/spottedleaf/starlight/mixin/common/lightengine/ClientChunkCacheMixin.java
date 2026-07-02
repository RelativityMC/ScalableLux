package ca.spottedleaf.starlight.mixin.common.lightengine;

import ca.spottedleaf.starlight.common.light.vanillainterface.BaseLevelLightEngineVanillaInterface;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixin {
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/chunk/LightChunkGetter;ZZ)Lnet/minecraft/world/level/lighting/LevelLightEngine;"))
    private LevelLightEngine redirectLightEngine(LightChunkGetter chunkSource, boolean hasBlockLight, boolean hasSkyLight) {
        return new BaseLevelLightEngineVanillaInterface(chunkSource, hasBlockLight, hasSkyLight);
    }
}
