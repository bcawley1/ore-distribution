package me.bcawley.oredistribution.client.config;

public enum OverlayPosition {
    TOP_LEFT("Top Left", 0), TOP_RIGHT("Top Right", 1), BOTTOM_RIGHT("Bottom Right", 2), BOTTOM_LEFT("Bottom Left", 3);

    private final String formatted;
    private final int index;

    OverlayPosition(String formatted, int index) {
        this.formatted = formatted;
        this.index = index;
    }

    public String getFormatted() {
        return formatted;
    }

    public int getIndex() {
        return index;
    }
}
