package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;

public class MiningWellEntity extends BlockEntity implements MjReceiver {, private final SimpleMjStorage buffer = new SimpleMjStorage(2_000_000); // 2 MJ buffer
    private int progress = 0;

    public MiningWellEntity(BlockPos pos, BlockState state) { super(BCContent.MINING_WELL_BE, pos, state); }
@Override
public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
@Override
public boolean canReceiveMJ(){ return true; }

    public void serverTick() {
        if (!(world instanceof ServerWorld sw)) return;
        if (buffer.getStoredMicroMJ() < 100_000) return; // need 0.1 MJ to run
        BlockPos cursor = pos.down();
        while (sw.isAir(cursor) && cursor.getY() > sw.getBottomY()) cursor = cursor.down();
        if (cursor.getY() <= sw.getBottomY()) return;
        BlockState state = sw.getBlockState(cursor);
        if (state.getBlock() == Blocks.BEDROCK) return;

        progress++;
        if (progress >= 20) { // 1 sec per block baseline
            progress = 0;
            // drop as item to adjacent inventory if present
            ItemStack drop = new ItemStack(state.getBlock().asItem());
            boolean inserted = false;
            for (Direction d : Direction.values()) {
                var storage = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
                if (storage == null) continue;
                try (Transaction t = Transaction.openOuter()) {
                    long acc = storage.insert(ItemVariant.of(drop), drop.getCount(), t);
                    if (acc == drop.getCount()) { t.commit(); inserted = true; break; }
                }
            }
            if (!inserted) Block.dropStack(sw, pos, drop);
            sw.removeBlock(cursor, false);
            buffer.extractMicroMJ(100_000);
        }
    }
}