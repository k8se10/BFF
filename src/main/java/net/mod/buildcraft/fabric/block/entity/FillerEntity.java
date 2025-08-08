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

public class FillerEntity extends BlockEntity implements MjReceiver {private final SimpleMjStorage buffer = new SimpleMjStorage(5_000_000); // 5 MJ
    private int x=-2,z=-2; private boolean done=false; private int tick;
    public FillerEntity(BlockPos pos, BlockState state){ super(BCContent.FILLER_BE, pos, state); }
@Override
public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
@Override
public boolean canReceiveMJ(){ return true; }

    public void serverTick(){
        if (done) return;
        if (!(world instanceof ServerWorld sw)) return;
        if (buffer.getStoredMicroMJ() < 200_000) return;
        tick++; if (tick % 2 != 0) return;

        BlockPos target = pos.add(x, 0, z);
        BlockState st = sw.getBlockState(target);
        if (st.getBlock() == Blocks.AIR) {
            // try place dirt from adjacent inventories
            ItemStack dirt = new ItemStack(Blocks.DIRT.asItem(), 1);
            if (pullOne(sw, dirt)) {
                sw.setBlockState(target, Blocks.DIRT.getDefaultState());
                buffer.extractMicroMJ(200_000);
            }
        } else if (st.getBlock() != Blocks.BEDROCK && target.getY() > pos.getY()) {
            // cut down blocks above to flatten
            sw.removeBlock(target, false);
            // output drops to inventory
            ItemStack drop = new ItemStack(st.getBlock().asItem());
            pushDrop(sw, drop);
            buffer.extractMicroMJ(200_000);
        }
        // advance
        x += 1
        ;
        if (x>2){ x=-2; z+=1; }
        if (z>2){ done=true; }
    }

    private boolean pullOne(ServerWorld sw, ItemStack want){
        for (Direction d: Direction.values()){
            var st = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
            if (st == null) continue;
            try (Transaction t = Transaction.openOuter()){
                for (var slot: st){
                    var res = slot.getResource();
                    if (!res.isBlank() && res.getItem() == want.getItem()){
                        long ex = slot.extract(res, 1, t);
                        if (ex == 1){ t.commit(); return true; }
                    }
                }
            }
        }
        return false;
    }

    private void pushDrop(ServerWorld sw, ItemStack drop){
        for (Direction d: Direction.values()){
            var st = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
            if (st == null) continue;
            try (Transaction t = Transaction.openOuter()){
                long acc = st.insert(ItemVariant.of(drop), drop.getCount(), t);
                if (acc == drop.getCount()){ t.commit(); return; }
            }
        }
        BlockEntity.dropStack(sw, pos, drop);
    }
}