package io.github.poeticrainbow.smoothtexturefix;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SmoothTextureFix implements ClientModInitializer {
    public static final String MOD_ID = "smoothtexturefix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
    public static ModConfig config;

    public static void setFilterOverride(boolean value) {
        config.overrideTextureFilter(value);
        Minecraft.getInstance().levelRenderer.resetSampler();
        saveConfig();
    }

    public static void toggleFilterOverride() {
        setFilterOverride(!config.overrideTextureFilter());
    }

    public static void saveConfig() {
        config.saveConfig(CONFIG_PATH);
    }

    @Override
    public void onInitializeClient() {
        config = ModConfig.loadConfig(CONFIG_PATH);
        saveConfig();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
            dispatcher.register(literal("smoothtexturefix")
                                    .then(argument("override_filtering", BoolArgumentType.bool())
                                              .executes(ctx -> {
                                                  setFilterOverride(ctx.getArgument("override_filtering", Boolean.class));
                                                  ctx.getSource()
                                                     .sendFeedback(Component.literal("Override Filtering: " + config.overrideTextureFilter()));
                                                  return 1;
                                              }))
                                    .executes(ctx -> {
                                        toggleFilterOverride();
                                        ctx.getSource()
                                           .sendFeedback(Component.literal("Override Filtering: " + config.overrideTextureFilter()));
                                        return 1;
                                    }));
        });

        LOGGER.info("Fixing those pesky smooth textures!");
    }
}
