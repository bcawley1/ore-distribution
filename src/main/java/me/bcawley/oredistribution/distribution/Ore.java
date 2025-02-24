package me.bcawley.oredistribution.distribution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.client.Minecraft;

import java.util.List;

public class Ore {
    @JsonProperty
    private String name;
    @JsonProperty
    private Weight weight;
    @JsonProperty
    private int color;
    @JsonProperty
    private String dimension;
    @JsonProperty
    private String fullRange;
    @JsonProperty
    private List<Integer> common;
    @JsonProperty
    private String tips;
    @JsonProperty
    private String item;

    private Ore() {
    }

    @JsonIgnore
    public String getText(double y) {
        String direction = switch (weight.getDirection(y)){
            case "up" -> "↑";
            case "down" -> "↓";
            default -> " ↕";
        };

        return "%s: %.0f%% %s".formatted(name, weight.getWeight(y), direction);
    }

    @JsonIgnore
    public int getColor() {
        return color;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public double getWeight(double y) {
        String currentDim = Minecraft.getInstance().player.level().dimension().location().toString();
        return currentDim.equals(dimension) ? weight.getWeight(y) : 0;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getItem() {
        return item;
    }

    public String getFullRange() {
        return fullRange;
    }

    public List<Integer> getCommon() {
        return common;
    }

    public String getTips() {
        return tips;
    }
}
