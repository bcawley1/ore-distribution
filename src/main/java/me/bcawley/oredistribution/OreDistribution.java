package me.bcawley.oredistribution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.logging.LogUtils;
import me.bcawley.oredistribution.config.Config;
import me.bcawley.oredistribution.config.SettingsScreen;
import me.bcawley.oredistribution.distribution.Distribution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.io.File;

@Mod(OreDistribution.MODID)
public class OreDistribution {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "oredistribution";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    private static Distribution distribution;
    private static Distribution defaultDistribution;
    private static Config config;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public OreDistribution(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(Overlay.INSTANCE::register);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (OreDistribution) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, new IConfigScreenFactory() {
            @Override
            public Screen createScreen(ModContainer modContainer, Screen screen) {
                return new SettingsScreen();
            }
        });
        Config.generateConfig();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            distribution = objectMapper.readValue(new File(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/distributions.json"), Distribution.class);
            defaultDistribution = objectMapper.readValue(Config.class.getResourceAsStream("/configs/distributions.json"), Distribution.class);
            config = objectMapper.readValue(new File(FMLPaths.CONFIGDIR.get().toString() + "/oredistribution/config.json"), Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {


            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static Distribution getDistribution() {
        return distribution;
    }

    public static Distribution getDefaultDistribution() {
        return defaultDistribution;
    }

    public static Config getConfig() {
        return config;
    }
}
