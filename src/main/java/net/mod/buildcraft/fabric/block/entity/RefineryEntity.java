package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.mod.buildcraft.fabric.registry.BCContent;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.screen.RefineryScreenHandler;

/**
 * Fixed class header and moved fields/methods into the body.
 * Minimal implementation to satisfy interfaces and compile cleanly.
 */
public class RefineryEntity extends BlockEntity implements NamedScreenHandlerFactory, MjReceiver{

    private final SimpleMjStorage buffer = new SimpleMjStorage(5_000_000); // 5 MJ in micro-MJ
    private long in1 = 0;
    private long in2 = 0;
    private long out = 0;

    public RefineryEntity(BlockPos pos, BlockState state) {
        super(BCContent.REFINERY_BE, pos, state);
    }

    // ---- NamedScreenHandlerFactory ----
    @Override
    public Text getDisplayName() {
        return Text.literal("Refinery");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RefineryScreenHandler(syncId, inv);
    }

    // ---- MJ Receiver ----
    @Override
    public long receiveMicroMJ(long amount) {
        return buffer.receiveMicroMJ(amount);
    }

    @Override
    public boolean canReceiveMJ() {
        return true;
    }

    // ---- Client helpers ----
    public boolean isActiveClient() {
        return buffer.getStoredMicroMJ() > 0;
    }

    public long getIn1() { return in1; }
    public long getIn2() { return in2; }
    public long getOut() { return out; }
}
