package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.item.BlueprintItem;
import net.mod.buildcraft.fabric.registry.BCContent;

public class ArchitectTableEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory {public ArchitectTableEntity(BlockPos pos, BlockState state){ super(BCContent.ARCHITECT_TABLE_BE, pos, state); }

    public boolean tryWriteBlueprint(ItemStack held, net.minecraft.entity.player.PlayerEntity player){
        if (!(held.getItem() instanceof BlueprintItem)) { player.sendMessage(Text.literal("Hold a Blueprint"), true); return false; }

        // Try to detect two opposite Landmarks within 16 blocks to define a cuboid
        BlockPos a = null, b = null;
        int r = 16;
        for (int dx=-r; dx<=r; dx++){
            for (int dy=-r; dy<=r; dy++){
                for (int dz=-r; dz<=r; dz++){
                    BlockPos p = pos.add(dx, dy, dz);
                    var st = world.getBlockState(p);
                    if (Registries.BLOCK.getId(st.getBlock()).toString().endsWith("landmark")){
                        if (a == null) a = p; else if (b == null) { b = p; break; }
                    }
                }
            }
        }
        BlockPos min, max;
        if (a != null && b != null){
            min = new BlockPos(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
            max = new BlockPos(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
        } else {
            // Fallback 11x11x11 centered on table base
            min = pos.add(-5, 0, -5);
            max = pos.add(5, 10, 5);
        }

        NbtCompound bp = capture(world, min, max);
        BlueprintItem.setData(held, bp);
        player.sendMessage(Text.literal("Blueprint captured: "+bp.getInt("sx")+"×"+bp.getInt("sy")+"×"+bp.getInt("sz")+" ("+bp.getInt("count")+" blocks)"), true);
        return true;
    }

    private NbtCompound capture(World world, BlockPos min, BlockPos max){
        NbtCompound tag = new NbtCompound();
        int sx = (max.getX()-min.getX())+1;
        int sy = (max.getY()-min.getY())+1;
        int sz = (max.getZ()-min.getZ())+1;
        tag.putInt("sx", sx); tag.putInt("sy", sy); tag.putInt("sz", sz);
        net.minecraft.nbt.NbtList palette = new net.minecraft.nbt.NbtList();
        java.util.Map<String,Integer> index = new java.util.HashMap<>();
        java.util.List<Integer> blocks = new java.util.ArrayList<>();

        int maxBlocks = 4096;
        int count = 0;
        for (int y=0; y<sy && count<maxBlocks; y++){
            for (int z=0; z<sz && count<maxBlocks; z++){
                for (int x=0; x<sx && count<maxBlocks; x++){
                    BlockPos p = min.add(x,y,z);
                    var st = world.getBlockState(p);
                    if (st.isAir()) { blocks.add(-1); continue; }
                    String id = Registries.BLOCK.getId(st.getBlock()).toString();
                    Integer idx = index.get(id);
                    if (idx == null) {
                        idx = index.size();
                        index.put(id, idx);
                        NbtCompound pe = new NbtCompound();
                        pe.putString("id", id);
                        palette.add(pe);
                    }
                    blocks.add(idx);
                    count++;
                }
            }
        }
        net.minecraft.nbt.NbtIntArray arr = new net.minecraft.nbt.NbtIntArray(blocks.stream().mapToInt(i->i).toArray());
        tag.put("palette", palette);
        tag.put("blocks", arr);
        tag.putInt("count", count);
        return tag;
    }
@Override
public net.minecraft.text.Text getDisplayName(){ return net.minecraft.text.Text.literal("Architect Table"); }
@Override
public net.minecraft.screen.ScreenHandler createMenu(int id, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity p){ return new net.mod.buildcraft.fabric.screen.ArchitectTableScreenHandler(id, inv); }
}
