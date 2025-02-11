package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public record Range(@JsonProperty int startY, @JsonProperty int endY, @JsonProperty double startPercent,
                    @JsonProperty double endPercent, @JsonProperty List<String> biome) {

    @JsonIgnore
    public boolean inRange(double y) {
        return y >= startY && y < endY && (biome.contains("any") || biome.contains(MinecraftClient.getInstance().world.getBiome(MinecraftClient.getInstance().player.getBlockPos()).getIdAsString()));
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
}
