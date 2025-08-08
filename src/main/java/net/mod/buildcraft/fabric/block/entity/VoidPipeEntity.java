package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.registry.BCContent;

public class VoidPipeEntity extends PipeBlockEntity {
    public VoidPipeEntity(BlockPos pos, BlockState state){ super(pos, state); }
    @Override public boolean tryAccept(ItemVariant variant, int amount, Direction from){ return true; } // items vanish
}