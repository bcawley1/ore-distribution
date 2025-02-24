package me.bcawley.oredistribution.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.neoforged.fml.loading.FMLPaths;

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
            File dir = new File(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/");
            if (!dir.exists()) {
                Files.createDirectory(Paths.get(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/"));
            }
            File config = new File(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/config.json");
            if (!config.exists()) {
                Files.copy(Config.class.getResourceAsStream("/configs/config.json"), Paths.get(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/config.json"));
            }
            File distr = new File(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/distributions.json");
            if (!distr.exists()) {
                Files.copy(Config.class.getResourceAsStream("/configs/distributions.json"), Paths.get(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/distributions.json"));
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