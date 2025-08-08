package net.mod.buildcraft.fabric.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.BuilderEntity;

public class BuilderBlock extends BlockWithEntity {
    public BuilderBlock(){ super(Settings.create().strength(2.0f)); }
@Override
public BlockEntity createBlockEntity(BlockPos pos, BlockState state){ return new BuilderEntity(pos, state); }
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World w, BlockState s, BlockEntityType<T> t){ return (world,p,st,be) -> { if (be instanceof BuilderEntity b && !world.isClient) b.serverTick(); }; }
@Override
public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient){ var be = world.getBlockEntity(pos); if (be instanceof net.minecraft.screen.NamedScreenHandlerFactory f) player.openHandledScreen(f);}
        if (world.isClient) return ActionResult.SUCCESS;
        var be = world.getBlockEntity(pos);
        if (!(be instanceof BuilderEntity b)) return ActionResult.PASS;
        ItemStack held = player.getStackInHand(hand);
        if (!held.isEmpty() && net.mod.buildcraft.fabric.item.BlueprintItem.hasSchematic(held)){
            b.setBlueprint(held.copy());
            if (!player.isCreative()) held.decrement(1);
            player.sendMessage(net.minecraft.text.Text.literal("Blueprint loaded."), true);
            return ActionResult.SUCCESS;
        }
        if (player.isSneaking()){
            var out = b.ejectBlueprint();
            if (!out.isEmpty()) {
                player.getInventory().offerOrDrop(out);
                player.sendMessage(net.minecraft.text.Text.literal("Blueprint ejected."), true);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}