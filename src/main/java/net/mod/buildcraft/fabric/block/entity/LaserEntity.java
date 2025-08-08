package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.MjProvider;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.Direction;

public class LaserEntity extends BlockEntity implements MjReceiver, MjProvider {, private final SimpleMjStorage buffer = new SimpleMjStorage(2_000_000); // 2 MJ
    private BlockPos targetPos = null;
    private int findCooldown = 0;
    public LaserEntity(BlockPos pos, BlockState state){ super(BCContent.LASER_BE, pos, state); }
@Override
public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
@Override
public boolean canReceiveMJ(){ return true; }
@Override
public long extractMicroMJ(long max){ return buffer.extractMicroMJ(max); }
@Override
public boolean canProvideMJ(){ return buffer.getStoredMicroMJ() > 0; }

    public void serverTick(){ if(world==null||world.isClient) return; if(buffer.getStoredMicroMJ()==0 && world.getTime()%5!=0) return;
        if (findCooldown-- <= 0){ findCooldown = 20; findTarget(); sync(); }
        if (!(world instanceof ServerWorld sw)) return;
        // Push a bit of MJ forward
        long toSend = Math.min(50_000, buffer.getStoredMicroMJ());
        if (toSend <= 0) return;
        for (Direction d: Direction.values()){
            var be = sw.getBlockEntity(pos.offset(d));
            if (be instanceof MjReceiver r && r.canReceiveMJ()){
                long acc = r.receiveMicroMJ(toSend);
                buffer.extractMicroMJ(acc);
                if (acc > 0) break;
            }
        }
    }


    private void findTarget(){
        if (!(world instanceof net.minecraft.server.world.ServerWorld sw)) return;
        int radius = 32;
        java.util.ArrayDeque<BlockPos> q = new java.util.ArrayDeque<>();
        java.util.HashSet<BlockPos> seen = new java.util.HashSet<>();
        q.add(pos); seen.add(pos);
        while(!q.isEmpty()){
            BlockPos p = q.removeFirst();
            for (Direction d : Direction.values()){
                BlockPos n = p.offset(d);
                if (!seen.add(n)) continue;
                var be = sw.getBlockEntity(n);
                if (be instanceof net.mod.buildcraft.fabric.block.entity.AssemblyTableEntity){ targetPos = n; return; }
                if (be instanceof net.mod.buildcraft.fabric.block.entity.KinesisPipeEntity || be instanceof LaserEntity){ q.addLast(n); }
            }
        }
        targetPos = null;
    }
    public BlockPos getTargetPos(){ return targetPos; }
    private void sync(){ if (world != null && !world.isClient) world.updateListeners(pos, getCachedState(), getCachedState(), 3); }
@Override
public Packet<ClientPlayPacketListener> toUpdatePacket(){ return BlockEntityUpdateS2CPacket.create(this); }
@Override
public NbtCompound toInitialChunkDataNbt(){ NbtCompound tag = new NbtCompound(); writeNbt(tag); return tag; }
@Override
public void writeNbt(NbtCompound nbt){ super.writeNbt(nbt); if (targetPos != null){ nbt.putInt("tx", targetPos.getX()); nbt.putInt("ty", targetPos.getY()); nbt.putInt("tz", targetPos.getZ()); } }
@Override
public void readNbt(NbtCompound nbt){ super.readNbt(nbt); if (nbt.contains("tx")) targetPos = new BlockPos(nbt.getInt("tx"), nbt.getInt("ty"), nbt.getInt("tz")); }
}
