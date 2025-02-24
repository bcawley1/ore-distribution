package me.bcawley.oredistribution.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bcawley.oredistribution.OreDistribution;
import me.bcawley.oredistribution.distribution.Ore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

import java.io.File;
import java.io.IOException;

public class SettingsScreen extends Screen {
    private Screen parent;

    public SettingsScreen() {
        super(Component.literal("Settings"));
    }


    @Override
    protected void init() {
        Config config = OreDistribution.getConfig();

        int offset = 30;
        int maxTextWidth = Math.min(OreDistribution.getDistribution().getDistributions().stream()
                .mapToInt(ore -> Minecraft.getInstance().font.width(ore.getName()))
                .max().getAsInt(), 100);

        Button showOverlay = Button.builder(Component.literal(config.overlayShown ? "Overlay: Shown" : "Overlay: Hidden"), button -> {
                    config.overlayShown = !config.overlayShown;
                    Minecraft.getInstance().setScreen(this);
                })
                .bounds(20, 20, width / 5, 20)
                .build();
        addRenderableWidget(showOverlay);
        System.out.println(config.xOffset);
        ExtendedSlider xOffset = new ExtendedSlider(2 * (width / 5) + 40, 20, width / 5, 20, Component.literal("X Offset: "), Component.empty(), -100, 100, config.xOffset, true) {
            @Override
            protected void applyValue() {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%applyed slider " + (int) ((value - 0.5) * 200));
                config.xOffset = (int) ((value - 0.5) * 200);
            }
        };
        addRenderableWidget(xOffset);

        ExtendedSlider yOffset = new ExtendedSlider(3 * (width / 5) + 50, 20, width / 5, 20, Component.literal("Y Offset: "), Component.empty(), -100, 100, config.yOffset, true) {
            @Override
            protected void applyValue() {
                config.yOffset = (int) value;
            }
        };
        addRenderableWidget(yOffset);

        Button resetOffset = Button.builder(Component.literal(""), button -> {
                    config.xOffset = 0;
                    config.yOffset = 0;
                    Minecraft.getInstance().setScreen(this);
                })
                .bounds(4 * (width / 5) + 60, 20, 20, 20)
                .build();
        addRenderableWidget(resetOffset);


        Button overlayPos = Button.builder(Component.literal(config.overlayPosition.getFormatted()), button -> {
                    config.overlayPosition = OverlayPosition.values()[(config.overlayPosition.getIndex() + 1) % 4];
                    Minecraft.getInstance().setScreen(this);
                })
                .bounds(30 + width / 5, 20, width / 5, 20)
                .build();
        addRenderableWidget(overlayPos);

        for (Ore ore : OreDistribution.getDistribution().getDistributions()) {

            boolean oreShown = !config.hiddenOres.contains(ore.getName());
            Button show = Button.builder(Component.literal(oreShown ? "Shown" : "Hidden").withColor(oreShown ? 0xFF00FF00 : 0xFFFF0000), button -> {
                        if (oreShown) {
                            config.hiddenOres.add(ore.getName());
                        } else {
                            config.hiddenOres.remove(ore.getName());
                        }
                        Minecraft.getInstance().setScreen(this);
                    })
                    .bounds(30 + maxTextWidth, 20 + offset, width / 3, 20)
                    .build();
            MultiLineTextWidget name = new MultiLineTextWidget(20, 20 + offset, Component.literal(ore.getName()), Minecraft.getInstance().font);
            name.setColor(ore.getColor());
            name.setCentered(false);

            EditBox color = new EditBox(Minecraft.getInstance().font, (width / 3) + 40 + maxTextWidth, 20 + offset, width - ((width / 3) + 50 + maxTextWidth) - 60, 20, Component.literal(""));
            color.setResponder(colorCode -> {
                try {
                    int newColor = Integer.parseInt(colorCode.replace("#", ""), 16);
                    if (newColor != ore.getColor()) {
                        ore.setColor(newColor);
                        name.setColor(newColor);
                    }
                } catch (NumberFormatException e) {
                }
            });
            color.setValue("#" + "%6s".formatted(Integer.toHexString(ore.getColor())).replace(" ", "0"));

            Button resetColor = Button.builder(Component.literal(""), button -> {
                        ore.setColor(OreDistribution.getDefaultDistribution().getOre(ore.getName()).getColor());
                        Minecraft.getInstance().setScreen(this);
                    })
                    .bounds(width - 70, 20 + offset, 20, 20)
                    .build();

            Button up = Button.builder(Component.literal("↑"), button -> {
                        OreDistribution.getDistribution().moveUp(ore);
                        Minecraft.getInstance().setScreen(this);
                    })
                    .bounds(width - 40, 20 + offset, 20, 20)
                    .build();
            Button done = Button.builder(Component.literal("Done"), button -> onClose())
                    .bounds(width / 2 - width / 4, height - 30, width / 2, 20)
                    .build();

            addRenderableWidget(resetColor);
            addRenderableWidget(name);
            addRenderableWidget(show);
            addWidget(color);
            addRenderableWidget(up);
            addRenderableWidget(done);
            offset += 20;
        }
    }

//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        super.render(context, mouseX, mouseY, delta);
//        MatrixStack matrices = context.getMatrices();
//        matrices.push();
//        matrices.scale(2, 2, 1);
//        for (int i = 0; i < OredistributionClient.getDistribution().getDistributions().size(); i++) {
//            context.drawText(textRenderer, "↺", (width - 64) / 2, i*10 + 25, 0xFFFFFFFF, true);
//        }
//        context.drawText(textRenderer, "↺", (4 * (width / 5) + 68) / 2 - 1, 10, 0xFFFFFFFF, true);
//        matrices.pop();
//    }


    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/distributions.json"), OreDistribution.getDistribution());
            objectMapper.writeValue(new File(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/config.json"), OreDistribution.getConfig());
        } catch (IOException e) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Error while saving config.").withColor(0xFFFF0000), false);
            e.printStackTrace();
        }
    }
}