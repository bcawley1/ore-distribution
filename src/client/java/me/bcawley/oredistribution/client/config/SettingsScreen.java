package me.bcawley.oredistribution.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bcawley.oredistribution.client.OredistributionClient;
import me.bcawley.oredistribution.client.distribution.Ore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.io.File;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class SettingsScreen extends Screen {
    private Screen parent;

    protected SettingsScreen(Screen parent) {
        super(Text.literal("Settings"));
        this.parent = parent;
    }


    @Override
    protected void init() {
        Config config = OredistributionClient.getConfig();
        int offset = 30;
        int maxTextWidth = Math.min(OredistributionClient.getDistribution().getDistributions().stream()
                .mapToInt(ore -> MinecraftClient.getInstance().textRenderer.getWidth(ore.getName()))
                .max().getAsInt(), 100);

        ButtonWidget showOverlay = ButtonWidget.builder(Text.literal(config.overlayShown ? "Overlay: Shown" : "Overlay: Hidden"), button -> {
                    config.overlayShown = !config.overlayShown;
                    MinecraftClient.getInstance().setScreen(this);
                })
                .dimensions(20, 20, width / 5, 20)
                .build();
        addDrawableChild(showOverlay);
        SliderWidget xOffset = new SliderWidget(2 * (width / 5) + 40, 20, width / 5, 20, Text.literal("X Offset: %d".formatted(config.xOffset)), (double) config.xOffset / 1000 + 0.5) {
            @Override
            protected void updateMessage() {
                setMessage(Text.of("X Offset: %d".formatted((int) ((this.value - 0.5) * 200))));
            }

            @Override
            protected void applyValue() {
                config.xOffset = (int) ((this.value - 0.5) * 200);
            }
        };
        addDrawableChild(xOffset);

        SliderWidget yOffset = new SliderWidget(3 * (width / 5) + 50, 20, width / 5, 20, Text.literal("Y Offset: %d".formatted(config.yOffset)), (double) config.yOffset / 1000 + 0.5) {
            @Override
            protected void updateMessage() {
                setMessage(Text.of("Y Offset: %d".formatted((int) ((this.value - 0.5) * 200))));
            }

            @Override
            protected void applyValue() {
                config.yOffset = (int) ((this.value - 0.5) * 200);
            }
        };
        addDrawableChild(yOffset);

        ButtonWidget resetOffset = ButtonWidget.builder(Text.literal(""), button -> {
                    config.xOffset = 0;
                    config.yOffset = 0;
                    MinecraftClient.getInstance().setScreen(this);
                })
                .dimensions(4 * (width / 5) + 60, 20, 20, 20)
                .build();
        addDrawableChild(resetOffset);


        ButtonWidget overlayPos = ButtonWidget.builder(Text.literal(config.overlayPosition.getFormatted()), button -> {
                    config.overlayPosition = OverlayPosition.values()[(config.overlayPosition.getIndex() + 1) % 4];
                    MinecraftClient.getInstance().setScreen(this);
                })
                .dimensions(30 + width / 5, 20, width / 5, 20)
                .build();
        addDrawableChild(overlayPos);

        for (Ore ore : OredistributionClient.getDistribution().getDistributions()) {

            boolean oreShown = !config.hiddenOres.contains(ore.getName());
            ButtonWidget show = ButtonWidget.builder(Text.literal(oreShown ? "Shown" : "Hidden").withColor(oreShown ? 0xFF00FF00 : 0xFFFF0000), button -> {
                        if (oreShown) {
                            config.hiddenOres.add(ore.getName());
                        } else {
                            config.hiddenOres.remove(ore.getName());
                        }
                        MinecraftClient.getInstance().setScreen(this);
                    })
                    .dimensions(30 + maxTextWidth, 20 + offset, width / 3, 20)
                    .build();
            TextWidget name = new TextWidget(20, 20 + offset, 100, 20, Text.literal(ore.getName()), MinecraftClient.getInstance().textRenderer);
            name.setTextColor(ore.getColor());
            name.alignLeft();

            TextFieldWidget color = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, (width / 3) + 40 + maxTextWidth, 20 + offset, width - ((width / 3) + 50 + maxTextWidth) - 60, 20, Text.literal(""));
            color.setChangedListener(colorCode -> {
                try {
                    int newColor = Integer.parseInt(colorCode.replace("#", ""), 16);
                    if (newColor != ore.getColor()) {
                        ore.setColor(newColor);
                        name.setTextColor(newColor);
                    }
                } catch (NumberFormatException e) {
                }
            });
            color.setText("#" + "%6s".formatted(Integer.toHexString(ore.getColor())).replace(" ", "0"));

            ButtonWidget resetColor = ButtonWidget.builder(Text.literal(""), button -> {
                        ore.setColor(OredistributionClient.getDefaultDistribution().getOre(ore.getName()).getColor());
                        MinecraftClient.getInstance().setScreen(this);
                    })
                    .dimensions(width - 70, 20 + offset, 20, 20)
                    .build();

            ButtonWidget up = ButtonWidget.builder(Text.literal("↑"), button -> {
                        OredistributionClient.getDistribution().moveUp(ore);
                        MinecraftClient.getInstance().setScreen(this);
                    })
                    .dimensions(width - 40, 20 + offset, 20, 20)
                    .build();
            ButtonWidget done = ButtonWidget.builder(Text.literal("Done"), button -> close())
                    .dimensions(width / 2 - width / 4, height - 30, width / 2, 20)
                    .build();

            addDrawableChild(resetColor);
            addDrawableChild(name);
            addDrawableChild(show);
            addDrawableChild(color);
            addDrawableChild(up);
            addDrawableChild(done);
            offset += 20;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(2, 2, 1);
        for (int i = 0; i < OredistributionClient.getDistribution().getDistributions().size(); i++) {
            context.drawText(textRenderer, "↺", (width - 64) / 2, i*10 + 25, 0xFFFFFFFF, true);
        }
        context.drawText(textRenderer, "↺", (4 * (width / 5) + 68) / 2 - 1, 10, 0xFFFFFFFF, true);
        matrices.pop();
    }

    @Override
    public void close() {
        client.setScreen(parent);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/config.json"), OredistributionClient.getConfig());
            objectMapper.writeValue(new File(FabricLoader.getInstance().getConfigDir().toString() + "/oredistribution/distributions.json"), OredistributionClient.getDistribution());
        } catch (IOException e) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("Error while saving config.").withColor(0xFFFF0000), false);
            e.printStackTrace();
        }
    }
}
