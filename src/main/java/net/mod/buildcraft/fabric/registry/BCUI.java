package net.mod.buildcraft.fabric.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class BCUI {
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.TankScreenHandler> TANK_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.EngineScreenHandler> ENGINE_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.TableScreenHandler> TABLE_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.QuarryScreenHandler> QUARRY_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.DiamondPipeScreenHandler> DIAMOND_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.ArchitectTableScreenHandler> ARCHITECT_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.BuilderScreenHandler> BUILDER_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.RefineryScreenHandler> REFINERY_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.CombustionEngineScreenHandler> COMBUSTIONENGINE_HANDLER;
    public static ScreenHandlerType<net.mod.buildcraft.fabric.screen.SteamEngineScreenHandler> STEAMENGINE_HANDLER;

    public static void register() {
        TANK_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("tank"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.TankScreenHandler::new));
        ENGINE_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("engine"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.EngineScreenHandler::new));
        TABLE_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("table"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.TableScreenHandler::new));
        QUARRY_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("quarry"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.QuarryScreenHandler::new));
        DIAMOND_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("diamond_pipe"), new ScreenHandlerType<>((syncId, inv) -> new net.mod.buildcraft.fabric.screen.DiamondPipeScreenHandler(syncId, inv, null)));
        ARCHITECT_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("architect_table"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.ArchitectTableScreenHandler::new));
        BUILDER_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("builder"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.BuilderScreenHandler::new));
        REFINERY_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("refinery"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.RefineryScreenHandler::new));
        COMBUSTIONENGINE_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("combustionengine"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.CombustionEngineScreenHandler::new));
        STEAMENGINE_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("steamengine"), new ScreenHandlerType<>(net.mod.buildcraft.fabric.screen.SteamEngineScreenHandler::new));
    }

    private static Identifier id(String p){ return new Identifier("buildcraft", p); }
}