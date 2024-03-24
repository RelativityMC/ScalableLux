package ca.spottedleaf.starlight.common.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class Config {

    static {
        final Properties properties = new Properties();
        final Properties newProperties = new Properties();
        final Path path = FabricLoader.getInstance().getConfigDir().resolve("scalablelux.properties");
        if (Files.isRegularFile(path)) {
            try (InputStream in = Files.newInputStream(path, StandardOpenOption.CREATE)) {
                properties.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        if (!newProperties.isEmpty()) {
            try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                newProperties.store(out, "Configuration file for ScalableLux");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void init() {
    }

    private static int getInt(Properties properties, Properties newProperties, String key, int def) {
        try {
            final int i = Integer.parseInt(properties.getProperty(key));
            newProperties.setProperty(key, String.valueOf(i));
            return i;
        } catch (NumberFormatException e) {
            newProperties.setProperty(key, String.valueOf(def));
            return def;
        }
    }

}
