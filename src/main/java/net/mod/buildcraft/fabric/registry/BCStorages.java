package net.mod.buildcraft.fabric.registry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.block.entity.TankBlockEntity;

public class BCStorages {
    public static void init() {
        FluidStorage.SIDED.registerForBlockEntity((TankBlockEntity be, Direction side) -> be.getStorage(side), BCContent.TANK_BE);
    }
}