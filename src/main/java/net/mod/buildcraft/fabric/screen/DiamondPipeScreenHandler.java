package net.mod.buildcraft.fabric.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class DiamondPipeScreenHandler extends ScreenHandler {
    private final Inventory filters;

    public DiamondPipeScreenHandler(int syncId, PlayerInventory playerInv, net.mod.buildcraft.fabric.block.entity.DiamondPipeEntity be) {
        super(net.mod.buildcraft.fabric.registry.BCUI.DIAMOND_HANDLER, syncId);
        this.filters = be.getFilters();
        // layout: 6 rows (one per side), 3 cols each
        int y = 18;
        for (int row=0; row<6; row++) {
            for (int col=0; col<3; col++) {
                this.addSlot(new Slot(filters, row*3 + col, 8 + col*18, y + row*18));
            }
        }
        // player inv
        int py = y + 6*18 + 14;
        for (int r=0;r<3;r++) for (int c=0;c<9;c++) addSlot(new Slot(playerInv, 9 + r*9 + c, 8 + c*18, py + r*18));
        for (int c=0;c<9;c++) addSlot(new Slot(playerInv, c, 8 + c*18, py + 58));
    }

    @Override public boolean canUse(PlayerEntity player) { return true; }
}
