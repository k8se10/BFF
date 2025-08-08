package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.MjProvider;
import net.mod.buildcraft.fabric.registry.BCContent;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class KinesisPipeEntity extends BlockEntity {
    private long tierPerTickCache = 20_000;
    public KinesisPipeEntity(BlockPos pos, BlockState state) { super(BCContent.KINESIS_BE, pos, state); }

    public void serverTick() {
        if (world != null) {
            var b = world.getBlockState(pos).getBlock();
            if (b instanceof net.mod.buildcraft.fabric.energy.KinesisTier t) tierPerTickCache = Math.max(10_000, t.getMicroMjPerTick());
        }
        if (!(world instanceof ServerWorld sw)) return;
        // Pull a little from adjacent providers and push to nearest receivers.
        for (Direction d : Direction.values()) {
            BlockEntity be = sw.getBlockEntity(pos.offset(d));
            if (be instanceof MjProvider provider && provider.canProvideMJ()) {
                long pulled = provider.extractMicroMJ(tierPerTickCache);
                if (pulled > 0) {
                    // BFS to nearest receiver
                    BlockPos target = bfsNearestReceiver(sw, pos);
                    if (target != null) {
                        BlockEntity rbe = sw.getBlockEntity(target);
                        if (rbe instanceof MjReceiver recv) {
                            long notused = pulled - recv.receiveMicroMJ(pulled);
                            if (notused > 0 && provider.canReceiveMJ()) {
                                // push back if provider is storage
                                if (provider instanceof net.mod.buildcraft.fabric.energy.MjReceiver rcv) {
                                    rcv.receiveMicroMJ(notused);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockPos bfsNearestReceiver(ServerWorld world, BlockPos start) {
        ArrayDeque<BlockPos> q = new ArrayDeque<>();
        Set<BlockPos> seen = new HashSet<>();
        q.add(start); seen.add(start);
        while(!q.isEmpty()){
            BlockPos p = q.removeFirst();
            for (Direction d: Direction.values()){
                BlockPos n = p.offset(d);
                if (!seen.add(n)) continue;
                BlockEntity be = world.getBlockEntity(n);
                if (be instanceof KinesisPipeEntity) {
                    q.addLast(n);
                } else if (be instanceof MjReceiver rec && rec.canReceiveMJ()) {
                    return n;
                }
            }
        }
        return null;
    }
    // --- energy loss & tick ---
    private float lineLoss = (float)net.mod.buildcraft.fabric.config.BCConfig.KINESIS_LINE_LOSS; // 1% per tick in pipe
    @Override public void serverTick(){
        super.serverTick();
        if (buffer.getStoredMicroMJ()>0){
            buffer.extractMicroMJ((long)(buffer.getStoredMicroMJ()*lineLoss));
        }
    }
}
