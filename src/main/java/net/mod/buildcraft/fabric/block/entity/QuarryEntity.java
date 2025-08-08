package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.mod.buildcraft.fabric.registry.BCContent;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.screen.QuarryScreenHandler;

/**
 * Cleaned header (removed stray comma/method after brace) and provided minimal API.
 * Fill in logic as needed later.
 */
public class QuarryEntity extends BlockEntity implements NamedScreenHandlerFactory, MjReceiver{

    private final SimpleMjStorage buffer = new SimpleMjStorage(10_000_000);

    public QuarryEntity(BlockPos pos, BlockState state) {
        super(BCContent.QUARRY_BE, pos, state);
    }

    // ---- NamedScreenHandlerFactory ----
    @Override
    public Text getDisplayName() {
        return Text.literal("Quarry");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new QuarryScreenHandler(syncId, inv);
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

    // ---- Sync helpers (safe defaults) ----
    private void sync() {
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound t = new NbtCompound();
        writeNbt(t);
        return t;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        // store minimal state if needed
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        // read minimal state if needed
    }
}
