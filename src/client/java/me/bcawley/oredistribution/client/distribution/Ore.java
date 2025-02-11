package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ore {
    @JsonProperty
    private String name;
    @JsonProperty
    private Weight weight;
    @JsonProperty
    private int color;

    @JsonCreator
    public Ore(@JsonProperty("name") String name, @JsonProperty("weight") Weight weight, @JsonProperty("color") int color) {
        this.name = name;
        this.weight = weight;
        this.color = color;
    }

    @JsonIgnore
    public String getText(double y){
        return "%s: %.0f%%".formatted(name, weight.getWeight(y));
    }

    @JsonIgnore
    public int getColor() {
        return color;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }
}
