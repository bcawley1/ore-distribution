package me.bcawley.oredistribution.client.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class Config {
    @JsonProperty
    Set<String> hiddenOres;
    @JsonProperty
    boolean overlayShown;
    @JsonProperty
    OverlayPosition overlayPosition;
    @JsonProperty
    int xOffset;
    @JsonProperty
    int yOffset;

    private Config() {
    }

    public static void generateConfig() {
        try {
            File dir = new File(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/");
            if (!dir.exists()) {
                Files.createDirectory(Paths.get(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/"));
            }
            File config = new File(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/config.json");
            if (!config.exists()) {
                Files.copy(Config.class.getResourceAsStream("/configs/config.json"), Paths.get(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/config.json"));
            }
            File distr = new File(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/distributions.json");
            if (!distr.exists()) {
                Files.copy(Config.class.getResourceAsStream("/configs/distributions.json"), Paths.get(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/distributions.json"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> getHiddenOres() {
        return hiddenOres;
    }

    public boolean isOverlayShown() {
        return overlayShown;
    }

    public OverlayPosition getOverlayPosition() {
        return overlayPosition;
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }
}
