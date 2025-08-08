package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.PipeBlockEntity;
import org.jetbrains.annotations.Nullable;

public class WoodenPipeBlock extends PipeBlock {
    public static final DirectionProperty FACING = Properties.FACING;

    public WoodenPipeBlock() {
        super();
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }
@Override
public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : (w, p, s, be) -> {
            if (be instanceof PipeBlockEntity pipe) {
                pipe.serverTick();
                pipe.tryAutoExtract(state.get(FACING)); // Extract in facing direction if powered
            }
        };
    }
}