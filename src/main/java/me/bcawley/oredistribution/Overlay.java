package me.bcawley.oredistribution;

import com.mojang.blaze3d.vertex.PoseStack;
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
        guiGraphics.drawString(Minecraft.getInstance().font, "testplswork", 100, 100, 0xFFFFFFFF);
        matrices.popPose();
    }
}
