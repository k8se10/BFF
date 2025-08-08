package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.mod.buildcraft.fabric.energy.MjProvider;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;

public class RedstoneEngineEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory, MjProvider, MjReceiver {
    private final SimpleMjStorage buffer = new SimpleMjStorage(500_000); // 0.5 MJ buffer

    public RedstoneEngineEntity(BlockPos pos, BlockState state) { super(BCContent.REDSTONE_ENGINE_BE, pos, state); }

    
    @Override
    public net.minecraft.text.Text getDisplayName() {
        return net.minecraft.text.Text.literal("Redstone Engine");
    }
    @Override
    public net.minecraft.screen.ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity player) {
        return new net.mod.buildcraft.fabric.screen.RedstoneEngineScreenHandler(syncId, inv);
    }
    
    public void serverTick() {
        if (!(world instanceof ServerWorld sw)) return;
        // If powered by redstone, generate a trickle of energy
        if (world.isReceivingRedstonePower(pos)) {
            buffer.receiveMicroMJ(5_000); // 0.005 MJ/t
        }
        // Try to push into adjacent kinesis pipes or receivers
        long toSend = Math.min(20_000, buffer.getStoredMicroMJ());
        if (toSend <= 0) return;
        long remaining = toSend;
        for (var d : net.minecraft.util.math.Direction.values()) {
            if (remaining <= 0) break;
            var be = sw.getBlockEntity(pos.offset(d));
            if (be instanceof MjReceiver r && r.canReceiveMJ()) {
                long accepted = r.receiveMicroMJ(remaining);
                remaining -= accepted;
            }
        }
        buffer.extractMicroMJ(toSend - remaining);
    }

    @Override public long extractMicroMJ(long max) { return buffer.extractMicroMJ(max); }
    @Override public boolean canProvideMJ() { return buffer.getStoredMicroMJ() > 0; }
    @Override public long receiveMicroMJ(long amount) { return buffer.receiveMicroMJ(amount); }
    @Override public boolean canReceiveMJ() { return buffer.getStoredMicroMJ() < buffer.getCapacityMicroMJ(); }
}