package net.mod.buildcraft.fabric.transport;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TravelingItem {
    public ItemVariant variant;
    public int amount;
    public BlockPos target;
    public Direction lastMove;

    public TravelingItem(ItemVariant variant, int amount, BlockPos target, Direction lastMove) {
        this.variant = variant;
        this.amount = amount;
        this.target = target;
        this.lastMove = lastMove;
    }
}
