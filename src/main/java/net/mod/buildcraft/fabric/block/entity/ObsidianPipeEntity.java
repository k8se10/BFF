package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.registry.BCContent;

import java.util.List;

public class ObsidianPipeEntity extends PipeBlockEntity {
    public ObsidianPipeEntity(BlockPos pos, BlockState state){ super(pos, state); }

    public void serverTick(){
        if (!(world instanceof ServerWorld sw)) return;
        List<ItemEntity> items = sw.getEntitiesByClass(ItemEntity.class, new Box(pos).expand(2.0), e -> e.isAlive());
        for (ItemEntity e : items){
            var stack = e.getStack();
            if (stack.isEmpty()) continue;
            // try to push directly into adjacent inventory first
            for (Direction d : Direction.values()){
                var storage = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
                if (storage == null) continue;
                try (Transaction t = Transaction.openOuter()){
                    long acc = storage.insert(ItemVariant.of(stack), stack.getCount(), t);
                    if (acc > 0){ stack.decrement((int)acc); t.commit(); if (stack.isEmpty()) e.discard(); return; }
                }
            }
            // else, inject into pipe network (treat as coming from world toward closest inventory)
            if (tryAccept(ItemVariant.of(stack), Math.min(8, stack.getCount()), Direction.UP)){
                stack.decrement(Math.min(8, stack.getCount()));
                if (stack.isEmpty()) e.discard();
                return;
            }
        }
    }
}