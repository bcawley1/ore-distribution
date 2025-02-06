package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Weight {
    @JsonProperty
    private final List<Range> ranges;

    @JsonCreator
    public Weight(@JsonProperty("ranges") List<Range> ranges) {
        this.ranges = ranges;
    }

    @JsonIgnore
    public double getWeight(double y){
        return ranges.stream()
                .filter(r -> r.inRange(y))
                .mapToDouble(r -> r.getPercent(y))
                .sum();
    }
}
