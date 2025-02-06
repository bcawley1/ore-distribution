package me.bcawley.oredistribution.client.config;

import me.bcawley.oredistribution.client.OredistributionClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class SettingsScreen extends Screen {
    protected SettingsScreen() {
        super(Text.literal("Settings"));
    }


    public ButtonWidget showText;
    public TextFieldWidget input;

    @Override
    protected void init() {
        showText = ButtonWidget.builder(Text.literal("Reload Config"), button -> {
                    OredistributionClient.getOredistributionClient().reloadDistribution();
                })
                .dimensions(width / 2 - 205, 20, 400, 20)
                .build();
//        input = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 100, 60, Text.literal(""));
//        input.setText(String.valueOf(Config.getTextOffset()));
//        input.setChangedListener((s) -> {
//            try {
//                Config.setTextOffset(Integer.parseInt(s));
//            } catch (NumberFormatException e){}
//            });
//
        addDrawableChild(showText);
//        addDrawableChild(input);
        //addDrawableChild(textWidget);
    }
}