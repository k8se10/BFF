package net.mod.buildcraft.fabric.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class QuarryScreenHandler extends ScreenHandler {
    private int v1, v2;
    public QuarryScreenHandler(int syncId, PlayerInventory inv) {
        super(net.mod.buildcraft.fabric.registry.BCUI.QUARRY_HANDLER, syncId);
    }
    @Override public boolean canUse(PlayerEntity player) { return true; }
    public int getV1(){return v1;}
    public int getV2(){return v2;}
}
