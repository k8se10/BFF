package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.energy.MjReceiver;
import net.mod.buildcraft.fabric.energy.SimpleMjStorage;
import net.mod.buildcraft.fabric.fluid.BCFluids;
import net.mod.buildcraft.fabric.registry.BCContent;

public class RefineryEntity extends BlockEntity implements net.minecraft.screen.NamedScreenHandlerFactory, MjReceiver {public class RefineryEntity extends BlockEntity
        implements net.minecraft.screen.NamedScreenHandlerFactory, MjReceiver {

    private final SimpleMjStorage buffer = new SimpleMjStorage(5_000_000); // 5 MJ

    public RefineryEntity(BlockPos pos, BlockState state){ super(BCContent.REFINERY_BE, pos, state); }

    @Override public long receiveMicroMJ(long amount){ return buffer.receiveMicroMJ(amount); }
    @Override public boolean canReceiveMJ(){ return true; }

    public void serverTick(){ if(world==null||world.isClient) return;
        if (!(world instanceof ServerWorld sw)) return;
        if (buffer.getStoredMicroMJ() < 300_000) return; // need energy
        // pull 1000 mB oil and output 1000 mB fuel
        FluidVariant oil = FluidVariant.of(BCFluids.OIL);
        FluidVariant fuel = FluidVariant.of(BCFluids.FUEL);
        // find input and output storages around
        for (Direction di : Direction.values()){
            var in = FluidStorage.SIDED.find(sw, pos.offset(di), di.getOpposite());
            if (in == null) continue;
            try (Transaction t = Transaction.openOuter()){
                long ex = in.extract(oil, FluidConstants.BUCKET, t);
                if (ex == FluidConstants.BUCKET){
                    // output
                    for (Direction doo : Direction.values()){
                        var out = FluidStorage.SIDED.find(sw, pos.offset(doo), doo.getOpposite());
                        if (out == null) continue;
                        long acc = out.insert(fuel, FluidConstants.BUCKET, t);
                        if (acc == FluidConstants.BUCKET){
                            buffer.extractMicroMJ(300_000);
                            t.commit();
                            return;
                        }
                    }
                }
            }
        }
    }
}

    @Override public net.minecraft.text.Text getDisplayName(){ return net.minecraft.text.Text.literal("Refinery"); }
    @Override public net.minecraft.screen.ScreenHandler createMenu(int id, net.minecraft.entity.player.PlayerInventory inv, net.minecraft.entity.player.PlayerEntity p){ return new net.mod.buildcraft.fabric.screen.RefineryScreenHandler(id, inv); }


    public long getIn1() { return in1; }
    public long getIn2() { return in2; }
    public long getOut() { return out; }
    public long getCapacity() { return FluidConstants.BUCKET * 4; } // 4 bucket capacity per tank

    public boolean isActiveClient(){ return buffer.getStoredMicroMJ() > 0; }
}
