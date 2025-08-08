package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.QuarryEntity;

import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.entity.player.PlayerEntity;

public class QuarryBlock extends BlockWithEntity {
    public QuarryBlock() { super(Settings.create().strength(3.0f)); }
    @Override 
    @Override
    public ActionResult onUse(BlockState state, net.minecraft.world.World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            var be = world.getBlockEntity(pos);
            if (be instanceof net.mod.buildcraft.fabric.block.entity.QuarryEntity) {
                player.openHandledScreen((net.minecraft.screen.NamedScreenHandlerFactory)be);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state){ return new QuarryEntity(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type){
        return world.isClient ? null : (w,p,s,be) -> { if (be instanceof QuarryEntity q) q.serverTick(); };
    }
}

    @Override
    public net.minecraft.util.ActionResult onUse(net.minecraft.block.BlockState state, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand, net.minecraft.util.hit.BlockHitResult hit){
        var stack = player.getStackInHand(hand);
        if(stack.getItem().toString().toLowerCase().contains("wrench")){
            if(!world.isClient){
                if(state.contains(net.minecraft.state.property.Properties.HORIZONTAL_FACING)){
                    var f = state.get(net.minecraft.state.property.Properties.HORIZONTAL_FACING);
                    world.setBlockState(pos, state.with(net.minecraft.state.property.Properties.HORIZONTAL_FACING, f.rotate(net.minecraft.util.math.Direction.Axis.Y)));
                }
            }
            return net.minecraft.util.ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }