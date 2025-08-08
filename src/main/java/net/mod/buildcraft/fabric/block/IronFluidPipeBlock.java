package net.mod.buildcraft.fabric.block;
import net.mod.buildcraft.fabric.fluid.FluidSpeedTier;
public class IronFluidPipeBlock extends FluidPipeBlock implements FluidSpeedTier {@Override
public int getMbPerTick() { return net.mod.buildcraft.fabric.config.BCConfig.FLUID_MB_TIER_IRON; }
}