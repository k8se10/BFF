package net.mod.buildcraft.fabric.screen;
import net.minecraft.entity.player.PlayerEntity; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.screen.ScreenHandler;
public class CombustionEngineScreenHandler extends ScreenHandler {
    public CombustionEngineScreenHandler(int syncId, PlayerInventory inv){ super(net.mod.buildcraft.fabric.registry.BCUI.COMBUSTIONENGINE_HANDLER, syncId); }
    @Override public boolean canUse(PlayerEntity player){ return true; }
}
