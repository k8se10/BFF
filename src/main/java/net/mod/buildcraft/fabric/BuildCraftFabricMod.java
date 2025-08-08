package net.mod.buildcraft.fabric;

import net.fabricmc.api.ModInitializer;
import net.mod.buildcraft.fabric.registry.BCContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildCraftFabricMod implements ModInitializer {
    public static final String MODID = "buildcraft";
    public static final Logger LOGGER = LoggerFactory.getLogger("BuildCraft (Fabric)");

    @Override
    public void onInitialize() {
        LOGGER.info("Starting BuildCraft (Fabric) scaffold...");
        BCContent.registerAll();
        net.mod.buildcraft.fabric.fluid.BCFluids.register();
        net.mod.buildcraft.fabric.registry.BCWorldgen.register();
        net.mod.buildcraft.fabric.fluid.BCFluids.register();
        net.mod.buildcraft.fabric.registry.BCUI.register();
    }
}