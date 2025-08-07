package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.registry.BCContent;

public class FluidPipeEntity extends BlockEntity {
    private int mbPerTickCache = net.mod.buildcraft.fabric.config.BCConfig.FLUID_PIPE_MB_PER_TICK;
    public FluidPipeEntity(BlockPos pos, BlockState state) { super(BCContent.FLUID_PIPE_BE, pos, state); }

    public void serverTick() {
        if (world != null) {
            var b = world.getBlockState(pos).getBlock();
            if (b instanceof net.mod.buildcraft.fabric.fluid.FluidSpeedTier t) mbPerTickCache = Math.max(50, t.getMbPerTick());
        }
        if (!(world instanceof ServerWorld sw)) return;
        // Pull a little fluid from adjacent storages and push along the pipe to the nearest storage.
        for (Direction d : Direction.values()) {
            var from = FluidStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
            if (from == null) continue;
            try (Transaction t = Transaction.openOuter()) {
                for (var slot : from) {
                    var res = slot.getResource();
                    if (res.isBlank()) continue;
                    long moved = slot.extract(res, mbPerTickCache, t);
                    if (moved > 0) {
                        BlockPos target = bfsNearestStorage(sw, pos, res);
                        if (target != null) {
                            var to = FluidStorage.SIDED.find(sw, target, Direction.DOWN);
                            if (to != null) {
                                long accepted = to.insert(res, moved, t);
                                if (accepted > 0) {
                                    t.commit();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockPos bfsNearestStorage(ServerWorld world, BlockPos start, FluidVariant variant) {
        java.util.ArrayDeque<BlockPos> q = new java.util.ArrayDeque<>();
        java.util.HashSet<BlockPos> seen = new java.util.HashSet<>();
        q.add(start); seen.add(start);
        while(!q.isEmpty()){
            BlockPos p = q.removeFirst();
            for (Direction d: Direction.values()){
                BlockPos n = p.offset(d);
                if (!seen.add(n)) continue;
                BlockEntity be = world.getBlockEntity(n);
                if (be instanceof FluidPipeEntity) {
                    q.addLast(n);
                } else {
                    var storage = FluidStorage.SIDED.find(world, n, d.getOpposite());
                    if (storage != null) return n;
                }
            }
        }
        return null;
    }
}
