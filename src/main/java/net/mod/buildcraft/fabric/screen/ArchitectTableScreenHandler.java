package net.mod.buildcraft.fabric.screen;
import net.minecraft.entity.player.PlayerEntity; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.screen.ScreenHandler;
public class ArchitectTableScreenHandler extends ScreenHandler {
    public ArchitectTableScreenHandler(int id, PlayerInventory inv){ super(net.mod.buildcraft.fabric.registry.BCUI.ARCHITECT_HANDLER, id); }
    @Override public boolean canUse(PlayerEntity player){ return true; }
}