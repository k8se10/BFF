package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.RedstoneEngineEntity;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.entity.player.PlayerEntity;

public class RedstoneEngineBlock extends BlockWithEntity {
    public RedstoneEngineBlock() { super(Settings.create().strength(0.8f)); }

    @Override
    
    @Override
    public ActionResult onUse(BlockState state, net.minecraft.world.World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            var be = world.getBlockEntity(pos);
            if (be instanceof net.mod.buildcraft.fabric.block.entity.RedstoneEngineEntity) {
                player.openHandledScreen((net.minecraft.screen.NamedScreenHandlerFactory)be);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneEngineEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : (w,p,s,be) -> {
            if (be instanceof RedstoneEngineEntity re) re.serverTick();
        };
    }
}
