package ca.spottedleaf.starlight.common;

import ca.spottedleaf.starlight.common.config.Config;
import net.fabricmc.api.ModInitializer;

public class ScalableLuxEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        Config.init();
    }
}
