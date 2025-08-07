package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.PipeBlockEntity;
import org.jetbrains.annotations.Nullable;

public class PipeBlock extends BlockWithEntity {
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, net.minecraft.util.math.Direction direction, BlockState neighborState, net.minecraft.world.WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        boolean connect = neighborState.getBlock() instanceof PipeBlock || world.getBlockEntity(neighborPos) != null;
        return state.with(switch(direction){
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        }, connect);
    }
    @Override
    public void onBlockAdded(BlockState state, net.minecraft.world.World world, BlockPos pos, BlockState oldState, boolean notify) {
        for (var d : net.minecraft.util.math.Direction.values()) {
            BlockPos n = pos.offset(d);
            BlockState ns = world.getBlockState(n);
            world.setBlockState(pos, getStateForNeighborUpdate(state, d, ns, world, pos, n), 3);
        }
    }
    
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;

    private static final VoxelShape CORE = createCuboidShape(6,6,6,10,10,10);

    public PipeBlock() {
        super(Settings.create().nonOpaque().strength(0.5f));
        setDefaultState(getStateManager().getDefaultState()
                .with(NORTH, false).with(SOUTH, false).with(EAST, false)
                .with(WEST, false).with(UP, false).with(DOWN, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState(); // Real impl should detect neighbors
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return CORE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipeBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? (w, p, s, be) -> { if (be instanceof PipeBlockEntity pipe) pipe.clientTick(); } : (w, p, s, be) -> {
            if (be instanceof PipeBlockEntity pipe) {
                pipe.serverTick();
            }
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.SUCCESS;
    }
}

    @Override
    public net.minecraft.util.ActionResult onUse(net.minecraft.block.BlockState state, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand, net.minecraft.util.hit.BlockHitResult hit){
        if(world.isClient) return net.minecraft.util.ActionResult.SUCCESS;
        var be = world.getBlockEntity(pos);
        if(!(be instanceof net.mod.buildcraft.fabric.block.entity.PipeBlockEntity pipe)) return net.minecraft.util.ActionResult.PASS;
        var stack = player.getStackInHand(hand);
        if(stack.getItem() instanceof net.mod.buildcraft.fabric.item.FacadeItem fi){
            var id = net.mod.buildcraft.fabric.item.FacadeItem.getBlock(stack);
            if(id!=null){
                pipe.setFacade(hit.getSide(), id);
                if(!player.isCreative()) stack.decrement(1);
                return net.minecraft.util.ActionResult.SUCCESS;
            }
        }
        // Remove all facades with wrench + shift
        if(player.isSneaking() && stack.getItem().toString().toLowerCase().contains("wrench")){
            for(var d: net.minecraft.util.math.Direction.values()) pipe.setFacade(d, null);
            return net.minecraft.util.ActionResult.SUCCESS;
        }
        return net.minecraft.util.ActionResult.PASS;
    }


    // Facade UX v2
    @Override
    public net.minecraft.util.ActionResult onUse(net.minecraft.block.BlockState state, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand, net.minecraft.util.hit.BlockHitResult hit){
        var stack = player.getStackInHand(hand);
        if(world.isClient) return net.minecraft.util.ActionResult.SUCCESS;
        var be = world.getBlockEntity(pos);
        if(!(be instanceof net.mod.buildcraft.fabric.block.entity.PipeBlockEntity pipe)) return net.minecraft.util.ActionResult.PASS;

        if(stack.getItem() instanceof net.mod.buildcraft.fabric.item.FacadeItem fi){
            if(player.isSneaking()){
                // copy facade from clicked side into item
                var id = pipe.getFacades().get(hit.getSide());
                if(id!=null){ net.mod.buildcraft.fabric.item.FacadeItem.setBlock(stack, id); player.sendMessage(net.minecraft.text.Text.literal("Copied facade: "+id), true); return net.minecraft.util.ActionResult.SUCCESS; }
                // or copy block under crosshair if not a pipe
            }
            // apply facade if item has one
            var id = net.mod.buildcraft.fabric.item.FacadeItem.getBlock(stack);
            if(id!=null){ pipe.setFacade(hit.getSide(), id); if(!player.isCreative()) stack.decrement(1); return net.minecraft.util.ActionResult.SUCCESS; }
        }

        // Wrench remove single side
        if(stack.getItem().toString().toLowerCase().contains("wrench")){
            if(player.isSneaking()){
                for(var d: net.minecraft.util.math.Direction.values()) pipe.setFacade(d, null);
            } else {
                pipe.setFacade(hit.getSide(), null);
            }
            return net.minecraft.util.ActionResult.SUCCESS;
        }
        return net.minecraft.util.ActionResult.PASS;
    }
