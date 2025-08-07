package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.DiamondPipeEntity;

import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.entity.player.PlayerEntity;

public class DiamondPipeBlock extends PipeBlock {
    @Override public BlockEntity createBlockEntity(BlockPos pos, BlockState state){ return new DiamondPipeEntity(pos, state); }

    @Override
    public ActionResult onUse(BlockState state, net.minecraft.world.World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            var be = world.getBlockEntity(pos);
            if (be instanceof DiamondPipeEntity d) { player.openHandledScreen(d); return ActionResult.SUCCESS; }
        }
        return ActionResult.SUCCESS;
    }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? (w,p,s,be) -> { if (be instanceof DiamondPipeEntity d) d.clientTick(); }
                              : (w,p,s,be) -> { if (be instanceof DiamondPipeEntity d) d.serverTick(); };
    }
}
