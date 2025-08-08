package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;

public class AssemblyTableEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory, net.mod.buildcraft.fabric.energy.MjReceiver{private final SimpleMjStorage buffer = new SimpleMjStorage(20_000_000); // 20 MJ
    private int progress;
    private ItemStack output = ItemStack.EMPTY;

    public AssemblyTableEntity(BlockPos pos, BlockState state){ super(BCContent.ASSEMBLY_TABLE_BE, pos, state); }
@Override
public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
@Override
public boolean canReceiveMJ(){ return true; }

    public void serverTick(){
        if (!(world instanceof ServerWorld sw)) return;

        // Minimal recipe: 1 redstone + 1 gold ingot -> "golden chipset" (use gold_ingot item as placeholder output)
        if (output.isEmpty()){
            // pull one redstone + one gold from adjacent inventories
            ItemStack redstone = new ItemStack(Items.REDSTONE, 1);
            ItemStack gold = new ItemStack(Items.GOLD_INGOT, 1);
            boolean gotR = pullOne(sw, redstone);
            boolean gotG = pullOne(sw, gold);
            if (gotR && gotG) {
                output = new ItemStack(Items.GOLD_INGOT); // placeholder
                progress = 0;
            }
        } else {
            // craft over time using MJ
            if (buffer.getStoredMicroMJ() >= 200_000) {
                buffer.extractMicroMJ(200_000); // 0.2 MJ per tick of work
                progress += 10;
            }
            if (progress >= 1000) { // 100 ticks of work
                // try output to adjacent inventory
                for (Direction d : Direction.values()) {
                    var storage = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
                    if (storage == null) continue;
                    try (Transaction t = Transaction.openOuter()) {
                        long acc = storage.insert(ItemVariant.of(output), output.getCount(), t);
                        if (acc == output.getCount()) {
                            t.commit();
                            output = ItemStack.EMPTY;
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean pullOne(ServerWorld sw, ItemStack want) {
        for (Direction d : Direction.values()) {
            var st = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
            if (st == null) continue;
            try (Transaction t = Transaction.openOuter()) {
                for (var slot : st) {
                    var res = slot.getResource();
                    if (!res.isBlank() && res.getItem() == want.getItem()) {
                        long ex = slot.extract(res, 1, t);
                        if (ex == 1) { t.commit(); return true; }
                    }
                }
            }
        }
        return false;
    }
@Override
public net.minecraft.text.Text getDisplayName() {
        return net.minecraft.text.Text.literal("Assembly Table");
    }
@Override
public net.minecraft.screen.ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity player) {
        return new net.mod.buildcraft.fabric.screen.AssemblyTableScreenHandler(syncId, inv);
    }
}
