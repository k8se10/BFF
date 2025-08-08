package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;

public class PumpEntity extends BlockEntity implements MjReceiver {, private final SimpleMjStorage buffer = new SimpleMjStorage(1_000_000); // 1 MJ
    public PumpEntity(BlockPos pos, BlockState state) { super(BCContent.PUMP_BE, pos, state); }
@Override
public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
@Override
public boolean canReceiveMJ(){ return true; }

    public void serverTick() {
        if (!(world instanceof ServerWorld sw)) return;
        if (buffer.getStoredMicroMJ() < 50_000) return; // need 0.05 MJ to work
        // Attempt to suck a source water block below and output to adjacent fluid storages
        BlockPos below = pos.down();
        if (sw.getBlockState(below).getBlock() == Blocks.WATER && sw.getFluidState(below).isOf(Fluids.WATER)) {
            FluidVariant water = FluidVariant.of(Fluids.WATER);
            for (Direction d : Direction.values()) {
                var storage = FluidStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
                if (storage == null) continue;
                try (Transaction t = Transaction.openOuter()) {
                    long accepted = storage.insert(water, FluidConstants.BUCKET, t);
                    if (accepted == FluidConstants.BUCKET) {
                        sw.setBlockState(below, Blocks.AIR.getDefaultState());
                        buffer.extractMicroMJ(50_000);
                        t.commit();
                        break;
                    }
                }
            }
        }
    }
}