package me.bcawley.oredistribution.client.event;

import me.bcawley.oredistribution.client.DistributionScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_OREDISTRIBUTION = "key.category.oredistribution.keys";
    public static final String KEY_OPEN_MENU = "key.oredistribution.open_menu";

    public static KeyBinding openMenuKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openMenuKey.wasPressed()) {
                client.setScreen(new DistributionScreen());
            }
        });
    }

    public static void register() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_OPEN_MENU, InputUtil.GLFW_KEY_M, KEY_CATEGORY_OREDISTRIBUTION));
        registerKeyInputs();
    }
}
