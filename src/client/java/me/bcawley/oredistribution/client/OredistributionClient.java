package me.bcawley.oredistribution.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bcawley.oredistribution.client.config.Config;
import me.bcawley.oredistribution.client.distribution.Distribution;
import me.bcawley.oredistribution.client.distribution.Ore;
import me.bcawley.oredistribution.client.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class OredistributionClient implements ClientModInitializer {
    private static OredistributionClient oredistributionClient;
    private static Distribution distribution;
    private static Distribution defaultDistribution;
    private static Config config;
    private static final Logger logger = LoggerFactory.getLogger("oredistribution");

    @Override
    public void onInitializeClient() {
        logger.info("Starting up!!!!!");
        oredistributionClient = this;

        Config.generateConfig();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            config = objectMapper.readValue(new File(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/config.json"), Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyInputHandler.register();
        registerTextRender();
        reloadDistribution();
    }

    public void reloadDistribution() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            distribution = objectMapper.readValue(new File(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/distributions.json"), Distribution.class);
            defaultDistribution = objectMapper.readValue(Config.class.getResourceAsStream("/configs/distributions.json"), Distribution.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerTextRender() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {

            if (config.isOverlayShown()) {
                double y = MinecraftClient.getInstance().player.getY();
                int textY = 0;
                for (Ore ore : distribution.getDistributions()) {
                    int initX = switch (config.getOverlayPosition()) {
                        case BOTTOM_LEFT, TOP_LEFT -> 10;
                        case TOP_RIGHT, BOTTOM_RIGHT ->
                                MinecraftClient.getInstance().getWindow().getScaledWidth() - 10 - MinecraftClient.getInstance().textRenderer.getWidth(ore.getText(y));
                    };
                    int initY = switch (config.getOverlayPosition()) {
                        case TOP_LEFT, TOP_RIGHT -> 10;
                        case BOTTOM_RIGHT, BOTTOM_LEFT ->
                                MinecraftClient.getInstance().getWindow().getScaledHeight() - (10 * (int) distribution.getDistributions().stream()
                                        .filter(distr -> !config.getHiddenOres().contains(distr.getName()) && distr.getWeight(y) > 0)
                                        .count()) - 10;
                    };
                    if (!config.getHiddenOres().contains(ore.getName()) && ore.getWeight(y) > 0) {
                        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(ore.getText(y));
                        drawContext.drawText(MinecraftClient.getInstance().textRenderer, ore.getText(y), initX + config.getxOffset(), initY + textY + config.getyOffset(), ore.getColor(), false);
                        textY += 10;
                    }
                }
            }
        });
    }

    public static OredistributionClient getOredistributionClient() {
        return oredistributionClient;
    }

    public static Config getConfig() {
        return config;
    }

    public static Distribution getDistribution() {
        return distribution;
    }

    public static Distribution getDefaultDistribution() {
        return defaultDistribution;
    }
}
