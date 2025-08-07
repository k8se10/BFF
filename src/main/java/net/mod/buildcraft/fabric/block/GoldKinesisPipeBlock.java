package net.mod.buildcraft.fabric.block;
import net.mod.buildcraft.fabric.energy.KinesisTier;
public class GoldKinesisPipeBlock extends KinesisPipeBlock implements KinesisTier {
    @Override public long getMicroMjPerTick() { return net.mod.buildcraft.fabric.config.BCConfig.KINESIS_TIER_GOLD; }
}
