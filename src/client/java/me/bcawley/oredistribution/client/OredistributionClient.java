package me.bcawley.oredistribution.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bcawley.oredistribution.client.config.Config;
import me.bcawley.oredistribution.client.distribution.Distribution;
import me.bcawley.oredistribution.client.distribution.Range;
import me.bcawley.oredistribution.client.distribution.Weight;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OredistributionClient implements ClientModInitializer {
    private Distribution distribution;
    private static OredistributionClient oredistributionClient;
    private static final Logger logger = LoggerFactory.getLogger("oredistribution");
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        //TODO:
        // - Biomes
        // - Dimensions
        // - Colors
        // - Arrows

        logger.info("Starting up!!!!!");
        oredistributionClient = this;

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.distribution.reload", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_M, // The keycode of the key
                "category.examplemod.test" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                client.player.sendMessage(Text.literal("Reloaded Distribution Config!"), false);
                reloadDistribution();
            }
        });

//        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> {
//            if (Config.isShowText()) {
//                String text = "Y: " + MinecraftClient.getInstance().player.getY() + "\n X:";
//                int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
//                drawContext.drawText(MinecraftClient.getInstance().textRenderer, text, MinecraftClient.getInstance().getWindow().getScaledWidth()-Config.getTextOffset()-textWidth, 10, 0xFF00FF00, false);
//            }
//        }));

        registerTextRender();


        Map<String, Weight> weights = new HashMap<>();
        weights.put("Diamond", new Weight(new ArrayList<>(List.of(new Range(16, -60, 0, 100)))));

        reloadDistribution();
//        try {
//            objectMapper.writeValue(new File(FabricLoader.getInstance().getConfigDir().toString()+"/distributions.json"), dis);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void reloadDistribution(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            distribution = objectMapper.readValue(new File(FabricLoader.getInstance().getConfigDir().toString() + "/distributions.json"), Distribution.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerTextRender() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            double y = MinecraftClient.getInstance().player.getY();
            if (Config.isShowText()) {
                int textY = 0;
                for (String text : distribution.getDisplayText(y)) {
                    textY+=10;
                    int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, text, MinecraftClient.getInstance().getWindow().getScaledWidth() - Config.getTextOffset() - textWidth, textY, 0xFF00FF00, false);
                }
            }
        });
    }

    public static OredistributionClient getOredistributionClient() {
        return oredistributionClient;
    }
}
