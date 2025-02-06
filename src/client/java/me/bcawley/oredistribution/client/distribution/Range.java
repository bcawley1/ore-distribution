package me.bcawley.oredistribution.client.distribution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Range(@JsonProperty int startY, @JsonProperty int endY, @JsonProperty double startPercent,
                    @JsonProperty double endPercent) {

    @JsonIgnore
    public boolean inRange(double y) {
        return y >= startY && y < endY;
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
