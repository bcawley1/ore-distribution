package me.bcawley.oredistribution.client.config;

public class Config {
    private static boolean showText = true;
    private static int textOffset = 100;

    public static boolean isShowText() {
        return showText;
    }

    protected static void setShowText(boolean showText) {
        Config.showText = showText;
    }

    public static int getTextOffset() {
        return textOffset;
    }

    public static void setTextOffset(int textOffset) {
        Config.textOffset = textOffset;
    }
}
