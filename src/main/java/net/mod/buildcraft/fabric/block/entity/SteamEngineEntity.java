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
import net.mod.buildcraft.fabric.energy.MjProvider;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.screen.SteamEngineScreenHandler;

/**
 * Fixed header and moved fields to body.
 */
public class SteamEngineEntity extends BlockEntity implements NamedScreenHandlerFactory, MjProvider, MjReceiver {

    private final SimpleMjStorage buffer = new SimpleMjStorage(5_000_000); // 5 MJ
    private int heat = 0;

    public SteamEngineEntity(BlockPos pos, BlockState state) {
        super(BCContent.STEAM_ENGINE_BE, pos, state);
    }

    // ---- NamedScreenHandlerFactory ----
    @Override
    public Text getDisplayName() {
        return Text.literal("Steam Engine");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SteamEngineScreenHandler(syncId, inv);
    }

    // ---- MJ Provider ----
    @Override
    public long extractMicroMJ(long amount) {
        return buffer.extractMicroMJ(amount);
    }

    @Override
    public boolean canProvideMJ() {
        return true;
    }

    // ---- MJ Receiver (in case engines can be recharged) ----
    @Override
    public long receiveMicroMJ(long amount) {
        return buffer.receiveMicroMJ(amount);
    }

    @Override
    public boolean canReceiveMJ() {
        return true;
    }

    public int getHeat() { return heat; }

    public boolean isRunningClient() {
        return buffer.getStoredMicroMJ() > 0;
    }
}
