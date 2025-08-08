package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.SteamEngineEntity;

import net.minecraft.util.ActionResult; import net.minecraft.util.Hand; import net.minecraft.util.hit.BlockHitResult; import net.minecraft.entity.player.PlayerEntity;
public class SteamEngineBlock extends BlockWithEntity {
    public SteamEngineBlock(){ super(Settings.create().strength(1.2f));     
}
@Override
public ActionResult onUse(BlockState s, net.minecraft.world.World w, BlockPos p, PlayerEntity pl, Hand h, BlockHitResult hit){ if(!w.isClient){ var be=w.getBlockEntity(p); if(be instanceof net.mod.buildcraft.fabric.block.entity.SteamEngineEntity) pl.openHandledScreen((net.minecraft.screen.NamedScreenHandlerFactory)be);} return ActionResult.SUCCESS; }
}
@Override
public BlockEntity createBlockEntity(BlockPos pos, BlockState state){ return new SteamEngineEntity(pos, state);
@Override
public ActionResult onUse(BlockState s, net.minecraft.world.World w, BlockPos p, PlayerEntity pl, Hand h, BlockHitResult hit){ if(!w.isClient){ var be=w.getBlockEntity(p); if(be instanceof net.mod.buildcraft.fabric.block.entity.SteamEngineEntity) pl.openHandledScreen((net.minecraft.screen.NamedScreenHandlerFactory)be);} return ActionResult.SUCCESS; }
}
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World w, BlockState s, BlockEntityType<T> t){
        return w.isClient ? null : (wr, p, st, be) -> { if (be instanceof SteamEngineEntity e) e.serverTick();
@Override
public ActionResult onUse(BlockState s, net.minecraft.world.World w, BlockPos p, PlayerEntity pl, Hand h, BlockHitResult hit){ if(!w.isClient){ var be=w.getBlockEntity(p); if(be instanceof net.mod.buildcraft.fabric.block.entity.SteamEngineEntity) pl.openHandledScreen((net.minecraft.screen.NamedScreenHandlerFactory)be);} return ActionResult.SUCCESS; }
}
;
@Override
public ActionResult onUse(BlockState s, net.minecraft.world.World w, BlockPos p, PlayerEntity pl, Hand h, BlockHitResult hit){ if(!w.isClient){ var be=w.getBlockEntity(p); if(be instanceof net.mod.buildcraft.fabric.block.entity.SteamEngineEntity) pl.openHandledScreen((net.minecraft.screen.NamedScreenHandlerFactory)be);} return ActionResult.SUCCESS; }
}
@Override
public ActionResult onUse(BlockState s, net.minecraft.world.World w, BlockPos p, PlayerEntity pl, Hand h, BlockHitResult hit){ if(!w.isClient){ var be=w.getBlockEntity(p); if(be instanceof net.mod.buildcraft.fabric.block.entity.SteamEngineEntity) pl.openHandledScreen((net.minecraft.screen.NamedScreenHandlerFactory)be);} return ActionResult.SUCCESS; }
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