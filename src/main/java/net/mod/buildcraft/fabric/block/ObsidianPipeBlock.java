package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.ObsidianPipeEntity;

public class ObsidianPipeBlock extends PipeBlock {
    @Override public BlockEntity createBlockEntity(BlockPos pos, BlockState state){ return new ObsidianPipeEntity(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : (w,p,s,be) -> { if (be instanceof ObsidianPipeEntity o) o.serverTick(); };
    }
}
