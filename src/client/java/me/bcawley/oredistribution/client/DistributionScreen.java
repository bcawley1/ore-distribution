package me.bcawley.oredistribution.client;

import me.bcawley.oredistribution.client.distribution.Ore;
import me.bcawley.oredistribution.client.event.KeyInputHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class DistributionScreen extends Screen {
    public DistributionScreen() {
        super(Text.of("Distribution"));
    }

    @Override
    protected void init() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        addDrawableChild(new TextWidget(10, 0, 20, 20, Text.literal("Ore:"), textRenderer));
        addDrawableChild(new TextWidget(30, 0, 100, 20, Text.literal("Found between:"), textRenderer));
        addDrawableChild(new TextWidget(130, 0, 100, 20, Text.literal("Most Common at:"), textRenderer));
        addDrawableChild(new TextWidget(200, 0, 100, 20, Text.literal("Notes:"), textRenderer));

        int y = 20;
        for (Ore ore : OredistributionClient.getDistribution().getDistributions()) {
            addDrawableChild(new TextWidget(30, y, 100, 20, Text.literal(ore.getFullRange()).withColor(ore.getColor()), textRenderer));
            addDrawableChild(new TextWidget(130, y, 100, 20, Text.literal(ore.getCommon().toString().substring(1, ore.getCommon().toString().length() - 1)).withColor(ore.getColor()), textRenderer));

            List<OrderedText> notes = textRenderer.wrapLines(Text.literal(ore.getTips()).withColor(ore.getColor()), 200);
            for (int i = 0; i < notes.size(); i++) {
                StringBuilder builder = new StringBuilder();
                notes.get(i).accept((index, style, codePoint) -> {
                            builder.append(Character.toChars(codePoint));
                            return true;
                        });
                addDrawableChild(new TextWidget(230, y + i * 10, 300, 20, Text.literal(builder.toString()).withColor(ore.getColor()), textRenderer).alignLeft());
            }


            y += 20;
        }

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int y = 20;
        for (Ore ore : OredistributionClient.getDistribution().getDistributions()) {
            context.drawItem(new ItemStack(Registries.ITEM.get(Identifier.of(ore.getItem()))), 10, y);
            y += 20;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (InputUtil.fromTranslationKey(KeyInputHandler.openMenuKey.getBoundKeyTranslationKey()).getCode() == keyCode) {
            client.currentScreen.close();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }
}
