package net.mod.buildcraft.fabric.screen;
import net.minecraft.entity.player.PlayerEntity; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.screen.ScreenHandler;
public class SteamEngineScreenHandler extends ScreenHandler {
    public SteamEngineScreenHandler(int syncId, PlayerInventory inv){ super(net.mod.buildcraft.fabric.registry.BCUI.STEAMENGINE_HANDLER, syncId); }
    @Override public boolean canUse(PlayerEntity player){ return true; }
}
