package net.mod.buildcraft.fabric.block;

import net.mod.buildcraft.fabric.transport.SpeedTieredPipe;

public class StonePipeBlock extends PipeBlock implements SpeedTieredPipe {
    @Override public int getItemsPerTick() { return 2; }
}
