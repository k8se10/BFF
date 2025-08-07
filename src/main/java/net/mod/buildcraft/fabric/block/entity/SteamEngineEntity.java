package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.mod.buildcraft.fabric.energy.MjProvider;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;
import net.mod.buildcraft.fabric.config.BCConfig;

public class SteamEngineEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory implements MjProvider, MjReceiver {
    private final SimpleMjStorage buffer = new SimpleMjStorage(5_000_000);
    private int burnTime = 0;

    public SteamEngineEntity(BlockPos pos, BlockState state){ super(BCContent.STEAM_ENGINE_BE, pos, state); }

    public void serverTick(){ if(world==null||world.isClient) return; if(buffer.getStoredMicroMJ()==0 && world.getTime()%20!=0) return;
        if (!(world instanceof ServerWorld sw)) return;
        // Burn furnace fuel
        if (burnTime <= 0){
            ItemStack fuel = new ItemStack(net.minecraft.item.Items.COAL);
            int time = net.minecraft.recipe.CookingRecipe.getCookTime(RecipeType.SMELTING) ; // not used; fallback
            time = net.minecraft.recipe.BlastingRecipeType.INSTANCE == null ? 1600 : 1600; // default coal 1600 ticks
            burnTime = time;
        } else {
            burnTime--;
            buffer.receiveMicroMJ(BCConfig.ENGINE_STEAM);
        }
        // Push to neighbors
        long send = Math.min(buffer.getStoredMicroMJ(), BCConfig.ENGINE_STEAM*2);
        if (send > 0){
            for (var d : net.minecraft.util.math.Direction.values()){
                var be = sw.getBlockEntity(pos.offset(d));
                if (be instanceof MjReceiver r && r.canReceiveMJ()){
                    long acc = r.receiveMicroMJ(send);
                    buffer.extractMicroMJ(acc);
                    if (buffer.getStoredMicroMJ() <= 0) break;
                }
            }
        }
    }
    @Override public long extractMicroMJ(long max){ return buffer.extractMicroMJ(max); }
    @Override public boolean canProvideMJ(){ return buffer.getStoredMicroMJ()>0; }
    @Override public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
    @Override public boolean canReceiveMJ(){ return true; }
}

    @Override public net.minecraft.text.Text getDisplayName(){ return net.minecraft.text.Text.literal("Steam Engine"); }
    @Override public net.minecraft.screen.ScreenHandler createMenu(int id, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity p){ return new net.mod.buildcraft.fabric.screen.SteamEngineScreenHandler(id, inv); }


    public int getHeat() { return heat; }

    public boolean isRunningClient(){ return buffer.getStoredMicroMJ() > 0; }
