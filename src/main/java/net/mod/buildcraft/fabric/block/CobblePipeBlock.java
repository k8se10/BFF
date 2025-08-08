package net.mod.buildcraft.fabric.block;

import net.mod.buildcraft.fabric.transport.SpeedTieredPipe;

public class CobblePipeBlock extends PipeBlock implements SpeedTieredPipe {@Override
public int getItemsPerTick() { return 1; }
}