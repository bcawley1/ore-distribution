package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    @JsonIgnore
    public String getDirection(double y){
        Optional<String> direction = ranges.stream()
                .filter(r -> r.inRange(y))
                .map(r -> r.getDirection(y))
                .max(Comparator.naturalOrder());
        return direction.orElse("both");
    }
}
