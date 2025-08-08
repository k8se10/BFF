package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.mod.buildcraft.fabric.registry.BCContent;

/**
 * Very simple marker: players place up to 3 around where the quarry will be.
 * On redstone power, this marker will try to find two others in range and form a rectangle.
 * We just store the min/max corners for later quarry discovery.
 */
public class LandmarkEntity extends BlockEntity {
    private BlockPos areaMin = null;
    private BlockPos areaMax = null;
    private int cooldown = 0;

    public LandmarkEntity(BlockPos pos, BlockState state) { super(BCContent.LANDMARK_BE, pos, state); }

    public void serverTick(){
        if (!(world instanceof ServerWorld sw)) return;
        if (cooldown-- > 0) return;
        // If powered, attempt to define an area using neighbors
        if (sw.isReceivingRedstonePower(pos)) {
            BlockPos[] found = findOtherMarkers(sw, 64);
            if (found != null) {
                BlockPos min = new BlockPos(
                    Math.min(Math.min(pos.getX(), found[0].getX()), found[1].getX()),
                    pos.getY(), // clamp to current Y for simplicity
                    Math.min(Math.min(pos.getZ(), found[0].getZ()), found[1].getZ())
                );
                BlockPos max = new BlockPos(
                    Math.max(Math.max(pos.getX(), found[0].getX()), found[1].getX()),
                    pos.getY(),
                    Math.max(Math.max(pos.getZ(), found[0].getZ()), found[1].getZ())
                );
                this.areaMin = min;
                this.areaMax = max;
                markDirty();
            }
            cooldown = 20;
        }
    }

    private BlockPos[] findOtherMarkers(ServerWorld sw, int radius){
        BlockPos a=null, b=null;
        BlockPos.Mutable m = new BlockPos.Mutable();
        for (int dx=-radius; dx<=radius; dx++){
            for (int dz=-radius; dz<=radius; dz++){
                if (dx==0 && dz==0) continue;
                m.set(pos.getX()+dx, pos.getY(), pos.getZ()+dz);
                var be = sw.getBlockEntity(m);
                if (be instanceof LandmarkEntity lm && lm != this) {
                    if (a == null) {
                        a = m.toImmutable();
                    } else if (b == null) {
                        b = m.toImmutable();
                        break;
                    }
                }
            }
            if (a != null && b != null) break;
        }
        if (a != null && b != null) return new BlockPos[]{a,b};
        return null;
    }

    public BlockPos getAreaMin(){ return areaMin; }
    public BlockPos getAreaMax(){ return areaMax; }

    @Override
    public void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        if (areaMin != null){
            nbt.putInt("minX", areaMin.getX());
            nbt.putInt("minY", areaMin.getY());
            nbt.putInt("minZ", areaMin.getZ());
        }
        if (areaMax != null){
            nbt.putInt("maxX", areaMax.getX());
            nbt.putInt("maxY", areaMax.getY());
            nbt.putInt("maxZ", areaMax.getZ());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        if (nbt.contains("minX")){
            areaMin = new BlockPos(nbt.getInt("minX"), nbt.getInt("minY"), nbt.getInt("minZ"));
        }
        if (nbt.contains("maxX")){
            areaMax = new BlockPos(nbt.getInt("maxX"), nbt.getInt("maxY"), nbt.getInt("maxZ"));
        }
    }
}
