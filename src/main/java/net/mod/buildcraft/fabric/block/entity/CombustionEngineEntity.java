package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjProvider;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.registry.BCContent;
import net.mod.buildcraft.fabric.config.BCConfig;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.mod.buildcraft.fabric.fluid.BCFluids;
import net.mod.buildcraft.fabric.config.BCConfig;

public class CombustionEngineEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory, MjProvider, MjReceiver {, private final SimpleMjStorage buffer = new SimpleMjStorage(10_000_000);
    private int heat = 0;
    private long fuelMb = 0; // in mB
    private long coolantMb = 0; // in mB

    public CombustionEngineEntity(BlockPos pos, BlockState state){ super(BCContent.COMBUSTION_ENGINE_BE, pos, state); }

    public void serverTick(){ if(world==null||world.isClient) return; if(buffer.getStoredMicroMJ()==0 && heat<50 && world.getTime()%20!=0) return;
        if (!(world instanceof ServerWorld sw)) return;
        // Pull fluids from adjacent storages
        try (Transaction t = Transaction.openOuter()){
            for (var d : net.minecraft.util.math.Direction.values()){
                var fs = FluidStorage.SIDED.find(sw, pos.offset(d), d.getOpposite());
                if (fs == null) continue;
                // pull fuel (oil or fuel) up to a bucket
                long got = fs.extract(FluidVariant.of(BCFluids.FUEL), FluidConstants.BUCKET - fuelMb, t); fuelMb += got;
                if (got == 0) { got = fs.extract(FluidVariant.of(BCFluids.OIL), FluidConstants.BUCKET - fuelMb, t); fuelMb += got; }
                // pull coolant (water) up to a bucket
                long cool = fs.extract(FluidVariant.of(net.minecraft.fluid.Fluids.WATER), FluidConstants.BUCKET - coolantMb, t); coolantMb += cool;
            }
            t.commit();
        }
        if (!(world instanceof ServerWorld sw)) return;
        if (fuelMb > 0) {
            // consume 1 mB per tick; fuel type defines energy per mB
            int perMb = (fuelMb > 0 && world != null && world.getTime()%1==0 && true) ? BCConfig.FUEL_ENERGY_PER_MB : BCConfig.OIL_ENERGY_PER_MB;
            // Choose perMb by checking if we have FUEL first (prefer)
            perMb = (fuelMb > 0) ? (fuelMb >= 1 ? BCConfig.FUEL_ENERGY_PER_MB : BCConfig.OIL_ENERGY_PER_MB) : BCConfig.OIL_ENERGY_PER_MB;
            long use = Math.min(1, fuelMb);
            fuelMb -= use;
            buffer.receiveMicroMJ(perMb * use);
            // cooling / heating
            if (coolantMb >= 1) { coolantMb -= 1; heat = Math.max(0, heat - BCConfig.COOLANT_COOLING_PER_MB); }
            else { heat += BCConfig.ENGINE_HEAT_PER_TICK; }
        } else {
            heat = Math.max(0, heat - BCConfig.ENGINE_COOLING_PER_TICK);
        }

        if (heat > BCConfig.ENGINE_OVERHEAT_THRESHOLD) { sw.createExplosion(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 3.0f, World.ExplosionSourceType.BLOCK); sw.removeBlock(pos, false); return; }
        long send = Math.min(buffer.getStoredMicroMJ(), BCConfig.ENGINE_COMBUSTION*2);
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
@Override
public long extractMicroMJ(long max){ return buffer.extractMicroMJ(max); }
@Override
public boolean canProvideMJ(){ return buffer.getStoredMicroMJ()>0; }
@Override
public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
@Override
public boolean canReceiveMJ(){ return true; }
}
@Override
public net.minecraft.text.Text getDisplayName(){ return net.minecraft.text.Text.literal("Combustion Engine"); }
@Override
public net.minecraft.screen.ScreenHandler createMenu(int id, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity p){ return new net.mod.buildcraft.fabric.screen.CombustionEngineScreenHandler(id, inv); }


    public int getHeat() { return heat; }

    public boolean isRunningClient(){ return buffer.getStoredMicroMJ() > 0 && heat > 100; }

    private void overheatExplode(){
        if(world instanceof net.minecraft.server.world.ServerWorld sw){
            sw.spawnParticles(net.minecraft.particle.ParticleTypes.LAVA, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 30, 0.3,0.3,0.3, 0.1);
        }
        world.createExplosion(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 3.0f, net.minecraft.world.explosion.Explosion.DestructionType.BREAK);
    }