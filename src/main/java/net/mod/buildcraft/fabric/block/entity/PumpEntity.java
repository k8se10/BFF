package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import net.mod.buildcraft.fabric.registry.BCContent;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;

/**
 * Fixed header and field placement.
 */
public class PumpEntity extends BlockEntity implements MjReceiver {

    private final SimpleMjStorage buffer = new SimpleMjStorage(1_000_000); // 1 MJ in micro-MJ

    public PumpEntity(BlockPos pos, BlockState state) {
        super(BCContent.PUMP_BE, pos, state);
    }

    @Override
    public long receiveMicroMJ(long amount) {
        return buffer.receiveMicroMJ(amount);
    }

    @Override
    public boolean canReceiveMJ() {
        return true;
    }

    public boolean isActiveClient() {
        return buffer.getStoredMicroMJ() > 0;
    }
}
