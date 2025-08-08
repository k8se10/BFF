package net.mod.buildcraft.fabric.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.Text;
import net.mod.buildcraft.fabric.block.entity.QuarryEntity;
import net.mod.buildcraft.fabric.block.entity.TankBlockEntity;
import net.mod.buildcraft.fabric.block.entity.RedstoneEngineEntity;
import net.mod.buildcraft.fabric.block.entity.AssemblyTableEntity;
import net.mod.buildcraft.fabric.block.entity.DiamondPipeEntity;

public class WrenchItem extends Item {
    public WrenchItem(Settings settings) { super(settings); }

    @Override
    public ActionResult useOnBlock(net.minecraft.item.ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;
        BlockState state = world.getBlockState(context.getBlockPos());
        Block block = state.getBlock();
        BlockPos pos = context.getBlockPos();
        var be = world.getBlockEntity(pos);
        if (be instanceof DiamondPipeEntity dp) {
            var held = context.getPlayer().getStackInHand(context.getHand());
            if (context.getPlayer().isSneaking()) { dp.setFilter(context.getSide(), net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.blank()); context.getPlayer().sendMessage(net.minecraft.text.Text.literal("Cleared filter for " + context.getSide()), true); return ActionResult.SUCCESS; }
            if (!held.isEmpty()) { dp.setFilter(context.getSide(), net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(held)); context.getPlayer().sendMessage(net.minecraft.text.Text.literal("Filter set for " + context.getSide() + ": " + held.getName().getString()), true); return ActionResult.SUCCESS; }
        }
        if (be instanceof QuarryEntity q) {
            // If landmarks are set, quarry uses them; otherwise allow cycling size
            q.cycleSize();
            context.getPlayer().sendMessage(Text.literal("Quarry size set to " + q.getPos().toShortString()), true);
            return ActionResult.SUCCESS;
        } else if (be instanceof TankBlockEntity t) {
            context.getPlayer().sendMessage(Text.literal("Tank: server-side fluid buffer active"), true);
        } else if (be instanceof RedstoneEngineEntity r) {
            context.getPlayer().sendMessage(Text.literal("Redstone Engine: MJ buffer active"), true);
        } else if (be instanceof AssemblyTableEntity a) {
            context.getPlayer().sendMessage(Text.literal("Assembly Table: progress running"), true);
        }
    
        // Rotate if it has a FACING property
        for (var prop : state.getProperties()) {
            if (prop instanceof DirectionProperty dp) {
                Direction current = state.get(dp);
                Direction next = current.rotateYClockwise();
                world.setBlockState(context.getBlockPos(), state.with(dp, next));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return new TypedActionResult<>(ActionResult.PASS, user.getStackInHand(hand));
    }
}