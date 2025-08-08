package net.mod.buildcraft.fabric.transport;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

/** Minimal routing utilities for item pipes. */
public class PipeAlgorithms {
    /** Try to find the nearest insertable inventory reachable via adjacent pipes. Returns null if none. */
    public static BlockPos bfsNearestInsert(ServerWorld world, BlockPos start, ItemVariant variant) {
        ArrayDeque<BlockPos> q = new ArrayDeque<>();
        Set<BlockPos> seen = new HashSet<>();
        q.add(start);
        seen.add(start);
        while(!q.isEmpty()) {
            BlockPos pos = q.removeFirst();
            // Check 6 neighbors
            for (Direction d : Direction.values()) {
                BlockPos n = pos.offset(d);
                if (seen.contains(n)) continue;
                seen.add(n);
                // If neighbor is a pipe, continue search through it
                BlockEntity be = world.getBlockEntity(n);
                if (be instanceof net.mod.buildcraft.fabric.block.entity.PipeBlockEntity) {
                    q.addLast(n);
                } else {
                    // Try to insert into inventory at neighbor from the opposite side
                    var storage = ItemStorage.SIDED.find(world, n, d.getOpposite());
                    if (storage != null) {
                        try (Transaction t = Transaction.openOuter()) {
                            long inserted = storage.insert(variant, 1, t); // probe: 1 item
                            if (inserted > 0) {
                                return n;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /** Move as many of the given variant into the inventory at pos, returning how many were accepted. */
    public static int insertAll(ServerWorld world, BlockPos to, Direction fromSide, ItemVariant variant, int amount) {
        var storage = ItemStorage.SIDED.find(world, to, fromSide);
        if (storage == null) return 0;
        try (Transaction t = Transaction.openOuter()) {
            long inserted = storage.insert(variant, amount, t);
            if (inserted > 0) {
                t.commit();
            }
            return (int) inserted;
        }
    }

    /** Extract up to 'amount' items of any kind from an inventory at 'from', facing 'fromSide'. */
    public static int extractSome(ServerWorld world, BlockPos from, Direction fromSide, ItemVariant[] outVariantHolder, int amount) {
        var storage = ItemStorage.SIDED.find(world, from, fromSide);
        if (storage == null) return 0;
        try (Transaction t = Transaction.openOuter()) {
            for (SingleSlotStorage<ItemVariant> slot : storage) {
                ItemVariant v = slot.getResource();
                long avail = slot.getAmount();
                if (!v.isBlank() && avail > 0) {
                    long extracted = slot.extract(v, Math.min(avail, amount), t);
                    if (extracted > 0) {
                        outVariantHolder[0] = v;
                        t.commit();
                        return (int) extracted;
                    }
                }
            }
        }
        return 0;
    }
}