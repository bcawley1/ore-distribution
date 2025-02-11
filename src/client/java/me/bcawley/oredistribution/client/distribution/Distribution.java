package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

//    public List<String> getDisplayText(double y) {
//        List<String> text = new ArrayList<>();
//        for (Map.Entry<String, Weight> entry : distributions.entrySet()) {
//            text.add("%s: %.0f%%".formatted(entry.getKey(), entry.getValue().getWeight(y)));
//        }
//        return text;
//    }
}
