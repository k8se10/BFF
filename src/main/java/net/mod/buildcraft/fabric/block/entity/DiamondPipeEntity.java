package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.registry.BCContent;

import java.util.EnumMap;
import java.util.Map;

public class DiamondPipeEntity extends PipeBlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory {
    private final Map<Direction, ItemVariant> filter = new EnumMap<>(Direction.class);

    public DiamondPipeEntity(BlockPos pos, BlockState state){ super(pos, state); }

    private final net.minecraft.inventory.SimpleInventory filters = new net.minecraft.inventory.SimpleInventory(54); // 6 sides * 3
    public net.minecraft.inventory.SimpleInventory getFilters(){ return filters; }
    

    @Override
    public boolean tryAccept(ItemVariant variant, int amount, Direction from){
        // If there's a filter set on any side, try to push towards a matching adjacent inventory first.
        Direction preferred = null;
        for (var e : filter.entrySet()){
            if (e.getValue() != null && !e.getValue().isBlank() && e.getValue().equals(variant)){
                preferred = e.getKey();
                break;
            }
        }
        if (preferred != null){
            if (world instanceof ServerWorld sw){
                var storage = ItemStorage.SIDED.find(sw, pos.offset(preferred), preferred.getOpposite());
                if (storage != null){
                    try (Transaction t = Transaction.openOuter()){
                        long acc = storage.insert(variant, amount, t);
                        if (acc == amount){ t.commit(); return true; }
                    }
                }
            }
        }
        return super.tryAccept(variant, amount, from);
    }

    public void setFilter(Direction side, ItemVariant v){ filter.put(side, v); markDirty(); if (world!=null) world.updateListeners(pos, getCachedState(), getCachedState(), 3); }

    @Override
    public void writeNbt(NbtCompound nbt){
        net.minecraft.inventory.Inventories.writeNbt(nbt, filters.getHeldStacks());
        super.writeNbt(nbt);
        for (Direction d: Direction.values()){
            ItemVariant v = filter.get(d);
            if (v != null && !v.isBlank()){
                nbt.put("f_"+d.getId(), v.toNbt());
            }
        }
    }
    @Override
    public void readNbt(NbtCompound nbt){
        net.minecraft.inventory.Inventories.readNbt(nbt, filters.getHeldStacks());
        super.readNbt(nbt);
        for (Direction d: Direction.values()){
            String k = "f_"+d.getId();
            if (nbt.contains(k)){
                filter.put(d, ItemVariant.fromNbt(nbt.getCompound(k)));
            }
        }
    }
}

    @Override public net.minecraft.text.Text getDisplayName(){ return net.minecraft.text.Text.literal("Diamond Pipe"); }
    @Override public net.minecraft.screen.ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity player){
        return new net.mod.buildcraft.fabric.screen.DiamondPipeScreenHandler(syncId, inv, this);
    }
    
    // Inventory layout: 6 sides * 9 slots each = 54.
    // Order: D,U,N,S,W,E (same as our GUI rows) each row 9 wide.
    public boolean matchesFilter(net.minecraft.util.math.Direction side, net.minecraft.item.ItemStack stack){
        if (stack.isEmpty()) return false;
        int base = switch(side){
            case DOWN -> 0;
            case UP -> 9;
            case NORTH -> 18;
            case SOUTH -> 27;
            case WEST -> 36;
            case EAST -> 45;
        };
        for (int i=0;i<9;i++){
            var s = this.inventory.getStack(base+i);
            if (!s.isEmpty() && net.minecraft.item.ItemStack.canCombine(s, stack)) return true;
        }
        return false;
    }