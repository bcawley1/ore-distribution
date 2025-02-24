package me.bcawley.oredistribution.distribution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.client.Minecraft;

import java.util.List;

public record Range(@JsonProperty int startY, @JsonProperty int endY, @JsonProperty double startPercent,
                    @JsonProperty double endPercent, @JsonProperty List<String> biome) {

    @JsonIgnore
    public boolean inRange(double y) {
        String currentBiome = Minecraft.getInstance().player.level().getBiome(Minecraft.getInstance().player.blockPosition()).toString();
        return y >= startY && y < endY && (biome.contains("any") || biome.contains(currentBiome));
    }

    @JsonIgnore
    public double getPercent(double y) {
        if (inRange(y)) {
            double slope = (endPercent - startPercent) / (endY - startY);
            return slope * (y - startY) + startPercent;
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public String getDirection(double y) {
        if (inRange(y)) {
            double slope = (endPercent - startPercent) / (endY - startY);
            return slope > 0 ? "up" : (slope == 0 ? "both" : "down");
        } else {
            return null;
        }
    }
}
