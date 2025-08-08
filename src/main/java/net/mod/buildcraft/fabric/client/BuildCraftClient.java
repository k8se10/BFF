package net.mod.buildcraft.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.mod.buildcraft.fabric.registry.BCUI;

public class BuildCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        net.mod.buildcraft.fabric.client.audio.LoopingAudioManager.init();
        // BERs
        HandledScreens.register(BCUI.TANK_HANDLER, net.mod.buildcraft.fabric.client.screen.TankScreen::new);
        HandledScreens.register(BCUI.ENGINE_HANDLER, net.mod.buildcraft.fabric.client.screen.EngineScreen::new);
        HandledScreens.register(BCUI.TABLE_HANDLER, net.mod.buildcraft.fabric.client.screen.TableScreen::new);
        HandledScreens.register(BCUI.QUARRY_HANDLER, net.mod.buildcraft.fabric.client.screen.QuarryScreen::new);
        HandledScreens.register(BCUI.DIAMOND_HANDLER, net.mod.buildcraft.fabric.client.screen.DiamondPipeScreen::new);
        HandledScreens.register(BCUI.ARCHITECT_HANDLER, net.mod.buildcraft.fabric.client.screen.ArchitectTableScreen::new);
        HandledScreens.register(BCUI.BUILDER_HANDLER, net.mod.buildcraft.fabric.client.screen.BuilderScreen::new);
        HandledScreens.register(net.mod.buildcraft.fabric.registry.BCUI.REFINERY_HANDLER, net.mod.buildcraft.fabric.client.screen.RefineryScreen::new);
        HandledScreens.register(net.mod.buildcraft.fabric.registry.BCUI.COMBUSTIONENGINE_HANDLER, net.mod.buildcraft.fabric.client.screen.CombustionEngineScreen::new);
        HandledScreens.register(net.mod.buildcraft.fabric.registry.BCUI.STEAMENGINE_HANDLER, net.mod.buildcraft.fabric.client.screen.SteamEngineScreen::new);

        BlockEntityRendererFactories.register(net.mod.buildcraft.fabric.registry.BCContent.PIPE_BE, net.mod.buildcraft.fabric.client.render.PipeBER::new);
        BlockEntityRendererFactories.register(net.mod.buildcraft.fabric.registry.BCContent.LASER_BE, net.mod.buildcraft.fabric.client.render.LaserBER::new);
        BlockEntityRendererFactories.register(net.mod.buildcraft.fabric.registry.BCContent.REDSTONE_ENGINE_BE, net.mod.buildcraft.fabric.client.render.EngineBER::new);
        BlockEntityRendererFactories.register(net.mod.buildcraft.fabric.registry.BCContent.QUARRY_BE, net.mod.buildcraft.fabric.client.render.QuarryBER::new);
    }
}