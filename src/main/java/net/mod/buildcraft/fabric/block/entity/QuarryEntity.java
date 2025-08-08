package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;

import net.minecraft.text.Text;
import net.mod.buildcraft.fabric.config.BCConfig;
import net.mod.buildcraft.fabric.block.entity.LandmarkEntity;

public class QuarryEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory, net.mod.buildcraft.fabric.energy.MjReceiver {, public void cycleSize(){
        if (!initialized) return;
        if (world == null || world.isClient) return;
        int next = size + 2; // grow by 2 to keep odd sizes
        if (next > net.mod.buildcraft.fabric.config.BCConfig.QUARRY_MAX_SIZE) next = net.mod.buildcraft.fabric.config.BCConfig.QUARRY_MIN_SIZE;
        size = next;
        int r = size/2;
        minX = pos.getX()-r; maxX = pos.getX()+r;
        minZ = pos.getZ()-r; maxZ = pos.getZ()+r;
        x = minX - pos.getX(); z = minZ - pos.getZ();
    }

    private final SimpleMjStorage buffer = new SimpleMjStorage(10_000_000); // 10 MJ buffer
    private int x, z, y;
    private int size = 9; // side length, odd preferred
    private int minX, minZ, maxX, maxZ;
    private boolean initialized = false;
    private int tick;

    public QuarryEntity(BlockPos pos, BlockState state) { super(BCContent.QUARRY_BE, pos, state); }
@Override
public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
@Override
public boolean canReceiveMJ(){ return true; }

    private void init(ServerWorld sw){
        // Try to read landmarks in range; if any have an area set, use bbox intersection
        int radius = 64;
        BlockPos lmMin = null, lmMax = null;
        for (int dx=-radius; dx<=radius; dx++){
            for (int dz=-radius; dz<=radius; dz++){
                BlockPos p = pos.add(dx, 0, dz);
                var be = sw.getBlockEntity(p);
                if (be instanceof LandmarkEntity lm){
                    if (lm.getAreaMin()!=null && lm.getAreaMax()!=null){
                        lmMin = lm.getAreaMin();
                        lmMax = lm.getAreaMax();
                        break;
                    }
                }
            }
            if (lmMin != null) break;
        }
        if (lmMin != null && lmMax != null){
            minX = Math.min(lmMin.getX(), lmMax.getX());
            minZ = Math.min(lmMin.getZ(), lmMax.getZ());
            maxX = Math.max(lmMin.getX(), lmMax.getX());
            maxZ = Math.max(lmMin.getZ(), lmMax.getZ());
            size = Math.max(BCConfig.QUARRY_MIN_SIZE, Math.min(BCConfig.QUARRY_MAX_SIZE, Math.max(maxX-minX+1, maxZ-minZ+1)));
        } else {
            // default centered square
            size = Math.max(BCConfig.QUARRY_MIN_SIZE, Math.min(BCConfig.QUARRY_MAX_SIZE, size));
            int r = size/2;
            minX = pos.getX()-r; maxX = pos.getX()+r;
            minZ = pos.getZ()-r; maxZ = pos.getZ()+r;
        }
        x = minX - pos.getX(); z = minZ - pos.getZ();
        y = pos.getY() - 1;
        initialized = true;
    }
@Override
public net.minecraft.text.Text getDisplayName() {
        return net.minecraft.text.Text.literal("Quarry");
    }
@Override
public net.minecraft.screen.ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity player) {
        return new net.mod.buildcraft.fabric.screen.QuarryScreenHandler(syncId, inv);
    }
    
    public void serverTick() { if(world==null||world.isClient) return; if(!running && world.getTime()%10!=0) return;
        if (!(world instanceof ServerWorld sw)) return;
        if (!initialized) init(sw);
        if (buffer.getStoredMicroMJ() < 500_000) return; // need 0.5 MJ per operation baseline

        tick++;
        if (tick % 5 != 0) return; // mine every 5 ticks

        BlockPos target = new BlockPos(minX + (x + (pos.getX()-minX)), y, minZ + (z + (pos.getZ()-minZ)));
        if (target.getY() <= sw.getBottomY()) return;

        BlockState state = sw.getBlockState(target);
        if (!state.isAir() && state.getBlock() != Blocks.BEDROCK) {
            ItemStack drop = new ItemStack(state.getBlock().asItem());
            boolean inserted = false;
            for (Direction d : Direction.values()) {
                var storage = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
                if (storage == null) continue;
                try (Transaction t = Transaction.openOuter()) {
                    long acc = storage.insert(ItemVariant.of(drop), drop.getCount(), t);
                    if (acc == drop.getCount()) { t.commit(); inserted = true; break; }
                }
            }
            if (!inserted) BlockEntity.dropStack(sw, pos, drop);
            sw.removeBlock(target, false);
            buffer.extractMicroMJ(500_000);
        }

        // Advance scan
        x++;
        if (x > 4) { x = -4; z++; }
        if (z > 4) { z = -4; y--; } // move down a layer
    }


    public int getMinX(){ return minX; } public int getMaxX(){ return maxX; }
    public int getMinZ(){ return minZ; } public int getMaxZ(){ return maxZ; }
    private void sync(){ if (world != null && !world.isClient) world.updateListeners(pos, getCachedState(), getCachedState(), 3); }
@Override
public Packet<ClientPlayPacketListener> toUpdatePacket(){ return BlockEntityUpdateS2CPacket.create(this); }
@Override
public NbtCompound toInitialChunkDataNbt(){ NbtCompound t = new NbtCompound(); writeNbt(t); return t; }
@Override
public void writeNbt(NbtCompound n){ super.writeNbt(n); n.putInt("minX",minX); n.putInt("maxX",maxX); n.putInt("minZ",minZ); n.putInt("maxZ",maxZ); }
@Override
public void readNbt(NbtCompound n){ super.readNbt(n); if (n.contains("minX")){ minX=n.getInt("minX"); maxX=n.getInt("maxX"); minZ=n.getInt("minZ"); maxZ=n.getInt("maxZ"); } }
    
    public int[] getCursorXZ(){ return new int[]{pos.getX()+x, pos.getZ()+z}; }


    public double getDrillX() { return this.miningCursor.getX(); }
    public double getDrillY() { return this.miningCursor.getY(); }
    public double getDrillZ() { return this.miningCursor.getZ(); }
    public boolean isActive() { return this.running; }

public void tick() { if (running) spawnParticles(); }


    private void spawnParticles() {
        if (world == null || world.isClient) return;
        world.addParticle(net.minecraft.particle.ParticleTypes.SMOKE,
            miningCursor.getX() + 0.5,
            miningCursor.getY() + 1.0,
            miningCursor.getZ() + 0.5,
            0, 0.05, 0);
    }

    private boolean collectToChest = true;
    private int sizeMJ = 1;
    public void setCollectToChest(boolean c){ collectToChest=c; }
    public void onBlockMined(net.minecraft.item.ItemStack drop){
        if(!collectToChest){ world.spawnEntity(new net.minecraft.entity.ItemEntity(world, pos.getX()+0.5,pos.getY()+1,pos.getZ()+0.5, drop)); return; }
        // find chest nearby and insert (simple)
    }
    private long costPerBlock(){
        return (long)((long)(net.mod.buildcraft.fabric.config.BCConfig.QUARRY_COST_MJ * 1_000_000L) * Math.max(1, sizeMJ)); // 0.2 MJ scaled
    }
}