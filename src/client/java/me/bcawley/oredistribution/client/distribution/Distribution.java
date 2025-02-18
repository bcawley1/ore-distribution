package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Distribution {
    @JsonProperty
    private List<Ore> distributions;

    @JsonCreator
    public Distribution(@JsonProperty("distributions") List<Ore> distributions) {
        this.distributions = distributions;
    }

    @JsonIgnore
    public List<Ore> getDistributions() {
        return distributions;
    }

    public void moveUp(Ore ore) {
        int index = distributions.indexOf(ore);
        if (index > 0) {
            Collections.swap(distributions, index, index - 1);
        }
    }

    @JsonIgnore
    public Ore getOre(String name){
        return distributions.stream()
                .filter(ore -> ore.getName().equalsIgnoreCase(name))
                .findFirst().orElseGet(null);
    }
}
