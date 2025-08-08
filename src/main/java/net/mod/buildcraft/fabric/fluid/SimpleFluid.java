package net.mod.buildcraft.fabric.fluid;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Block;
import net.minecraft.world.WorldView;
import net.minecraft.fluid.FluidState;

public abstract class SimpleFluid extends FlowableFluid {
    @Override public Fluid getFlowing() { return this; }
    @Override public Fluid getStill() { return this; }
    @Override public Item getBucketItem() { return Items.BUCKET; } // bucket items registered separately
    @Override protected BlockState toBlockState(FluidState state){ return Blocks.WATER.getDefaultState(); } // placeholder
    @Override protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder){ builder.add(Properties.LEVEL_1_8); }
    @Override protected boolean isInfinite(WorldView world) { return false; }
    public static class Still extends SimpleFluid {}
    public static class Flowing extends SimpleFluid {}
}