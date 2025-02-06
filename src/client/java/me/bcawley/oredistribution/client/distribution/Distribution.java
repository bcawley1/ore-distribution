package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Distribution {
    @JsonProperty
    private Map<String, Weight> distributions;

    @JsonCreator
    public Distribution(@JsonProperty("distributions") Map<String, Weight> distributions) {
        this.distributions = distributions;
    }

    public List<String> getDisplayText(double y) {
        List<String> text = new ArrayList<>();
        for (Map.Entry<String, Weight> entry : distributions.entrySet()) {
            text.add("%s: %.0f%%".formatted(entry.getKey(), entry.getValue().getWeight(y)));
        }
        return text;
    }
}
