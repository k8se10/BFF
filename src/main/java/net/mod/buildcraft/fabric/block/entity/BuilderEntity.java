package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.item.BlueprintItem;
import net.mod.buildcraft.fabric.registry.BCContent;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;

public class BuilderEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory, net.mod.buildcraft.fabric.energy.MjReceiver {
    private ItemStack blueprint = ItemStack.EMPTY;
    private final SimpleMjStorage buffer = new SimpleMjStorage(5_000_000);
    private int ticks;
    private int cursorX = 0, cursorY = 0, cursorZ = 0;

    public BuilderEntity(BlockPos pos, BlockState state){ super(BCContent.BUILDER_BE, pos, state); }

    public void setBlueprint(ItemStack bp){ this.blueprint = bp; markDirty(); }
    public ItemStack ejectBlueprint(){ ItemStack out = blueprint; blueprint = ItemStack.EMPTY; markDirty(); return out; }

    public void serverTick(){
        if (!(world instanceof ServerWorld sw)) return;
        if (blueprint.isEmpty() || !BlueprintItem.hasSchematic(blueprint)) return;

        NbtCompound bp = BlueprintItem.getData(blueprint);
        int sx = bp.getInt("sx"), sy = bp.getInt("sy"), sz = bp.getInt("sz");
        var palette = bp.getList("palette", net.minecraft.nbt.NbtElement.COMPOUND_TYPE);
        int[] blocks = bp.getIntArray("blocks");

        
        // Energy check: require 250k microMJ per block (0.25 MJ)
        if (buffer.getStoredMicroMJ() < 250_000) { 
            // preview particles so the player sees the target box while it waits
            if (sw.getTime() % 10 == 0) spawnPreviewParticles(bp, pos);
            return; 
        }

        // Simple scan-place loop: one block per tick
        for (int y=cursorY; y<sy; y++){
            for (int z=(y==cursorY?cursorZ:0); z<sz; z++){
                for (int x=(y==cursorY&&z==cursorZ?cursorX:0); x<sx; x++){
                    int idx = y*sz*sx + z*sx + x;
                    int pid = blocks[idx];
                    cursorX = x; cursorZ = z; cursorY = y;
                    if (pid < 0) continue; // air
                    String id = ((net.minecraft.nbt.NbtCompound)palette.get(pid)).getString("id");
                    Block b = Registries.BLOCK.get(new net.minecraft.util.Identifier(id));
                    BlockPos placeAt = pos.add(x+1, y+1, z+1);
                    if (!world.getBlockState(placeAt).isAir()) continue;

                    // Try to find the corresponding item in adjacent inventories
                    ItemStack needed = new ItemStack(b.asItem());
                    boolean has = false;
                    for (Direction d : Direction.values()){
                        var inv = ItemStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
                        if (inv == null) continue;
                        try (Transaction t = Transaction.openOuter()){
                            long ex = inv.extract(ItemVariant.of(needed), 1, t);
                            if (ex == 1){
                                t.commit();
                                has = true;
                                break;
                            }
                        }
                    }
                    if (!has) return; // wait for materials

                    // Place block
                    world.setBlockState(placeAt, b.getDefaultState());
                    buffer.extractMicroMJ(250_000);
                    world.playSound(null, placeAt, net.minecraft.sound.SoundEvents.BLOCK_STONE_PLACE, net.minecraft.sound.SoundCategory.BLOCKS, 0.6f, 1.0f);
                    // Move cursor forward
                    cursorX++;
                    if (cursorX >= sx) { cursorX=0; cursorZ++; }
                    if (cursorZ >= sz) { cursorZ=0; cursorY++; }
                    return;
                }
                cursorX = 0;
            }
            cursorZ = 0;
        }
        // Done
    }

    @Override public void writeNbt(NbtCompound n){ super.writeNbt(n); if (!blueprint.isEmpty()) n.put("bp", blueprint.writeNbt(new NbtCompound())); n.putInt("cx",cursorX); n.putInt("cy",cursorY); n.putInt("cz",cursorZ); }
    @Override public void readNbt(NbtCompound n){ super.readNbt(n); if (n.contains("bp")) blueprint = ItemStack.fromNbt(n.getCompound("bp")); cursorX=n.getInt("cx"); cursorY=n.getInt("cy"); cursorZ=n.getInt("cz"); }
}


    @Override public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
    @Override public boolean canReceiveMJ(){ return true; }


    @Override public net.minecraft.text.Text getDisplayName(){ return net.minecraft.text.Text.literal("Builder"); }
    @Override public net.minecraft.screen.ScreenHandler createMenu(int id, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity p){ return new net.mod.buildcraft.fabric.screen.BuilderScreenHandler(id, inv); }