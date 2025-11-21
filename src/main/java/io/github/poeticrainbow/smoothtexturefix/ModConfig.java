package io.github.poeticrainbow.smoothtexturefix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ModConfig {
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .disableHtmlEscaping()
        .create();
    private boolean overrideTextureFilter;

    public ModConfig(boolean overrideTextureFiltering) {
        this.overrideTextureFilter = overrideTextureFiltering;
    }

    public static ModConfig loadConfig(Path path) {
        var configFile = path.toFile();
        if (configFile.exists() && configFile.isFile()) {
            try (var reader = Files.newBufferedReader(path)) {
                return gson.fromJson(reader, ModConfig.class);
            } catch (IOException | JsonParseException ignored) {
            }
        }
        return new ModConfig(true);
    }

    public void saveConfig(Path path) {
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            SmoothTextureFix.LOGGER.warn("Could not save config file");
        }
    }

    public boolean overrideTextureFilter() {
        return overrideTextureFilter;
    }

    public void overrideTextureFilter(boolean value) {
        overrideTextureFilter = value;
    }
}
