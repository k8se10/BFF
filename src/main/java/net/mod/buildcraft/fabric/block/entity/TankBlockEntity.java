package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.registry.BCContent;

public class TankBlockEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory {

    // 16 buckets
    public static final long CAPACITY = 16 * FluidConstants.BUCKET;

    private final SingleVariantStorage<FluidVariant> storage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return CAPACITY;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(BCContent.TANK_BE, pos, state);
    }

    public Storage<FluidVariant> getStorage(Direction side) {
        return storage;
    }

    
    @Override
    public net.minecraft.text.Text getDisplayName() {
        return net.minecraft.text.Text.literal("Tank");
    }
    @Override
    public net.minecraft.screen.ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity player) {
        return new net.mod.buildcraft.fabric.screen.TankBlockScreenHandler(syncId, inv);
    }
    
    public void serverTick() {
        if (!(world instanceof ServerWorld sw)) return;
        // no-op for now
    }
}
