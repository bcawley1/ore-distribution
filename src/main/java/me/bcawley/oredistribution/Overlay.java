package me.bcawley.oredistribution;

import com.mojang.blaze3d.vertex.PoseStack;
import me.bcawley.oredistribution.config.OverlayPosition;
import me.bcawley.oredistribution.distribution.Ore;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public class Overlay implements LayeredDraw.Layer {
    public static final Overlay INSTANCE = new Overlay();

    public void register(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(OreDistribution.MODID, "overlay"), this);
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        PoseStack matrices = guiGraphics.pose();
        matrices.pushPose();

        guiGraphics.drawString(Minecraft.getInstance().font, Minecraft.getInstance().player.level().dimension().location().toString(), 100, 100, 0xFFFFFFFF);
        double y = Minecraft.getInstance().player.getY();
        int textY = 0;
        for (Ore ore : OreDistribution.getDistribution().getDistributions()) {
            int initX = switch (OverlayPosition.BOTTOM_RIGHT) {//config
                case BOTTOM_LEFT, TOP_LEFT -> 10;
                case TOP_RIGHT, BOTTOM_RIGHT ->
                        Minecraft.getInstance().getWindow().getGuiScaledWidth() - 10 - Minecraft.getInstance().font.width(ore.getText(y));
            };
            int initY = switch (OverlayPosition.BOTTOM_RIGHT) {//config
                case TOP_LEFT, TOP_RIGHT -> 10;
                case BOTTOM_RIGHT, BOTTOM_LEFT ->
                        Minecraft.getInstance().getWindow().getGuiScaledHeight() - (10 * (int) OreDistribution.getDistribution().getDistributions().stream()
                                .filter(distr -> distr.getWeight(y) > 0)//config
                                .count()) - 10;
            };
            if (ore.getWeight(y) > 0) { //add config line
                int textWidth = Minecraft.getInstance().font.width(ore.getText(y));
                guiGraphics.drawString(Minecraft.getInstance().font, ore.getText(y), initX, initY + textY, ore.getColor(), false);
                textY += 10;
            }
        }
        matrices.popPose();
    }
}
