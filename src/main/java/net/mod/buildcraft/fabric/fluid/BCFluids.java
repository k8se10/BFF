package net.mod.buildcraft.fabric.fluid;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.block.FluidBlock;

public class BCFluids {
    public static FlowableFluid OIL;
    public static FlowableFluid OIL_FLOWING;
    public static FluidBlock OIL_BLOCK;
    public static Item OIL_BUCKET;

    public static FlowableFluid FUEL;
    public static FlowableFluid FUEL_FLOWING;
    public static FluidBlock FUEL_BLOCK;
    public static Item FUEL_BUCKET;

    public static void register() {
        OIL = Registry.register(Registries.FLUID, id("oil"), new SimpleFluid.Still());
        OIL_FLOWING = Registry.register(Registries.FLUID, id("oil_flowing"), new SimpleFluid.Flowing());
        OIL_BLOCK = Registry.register(Registries.BLOCK, id("oil_block"), new FluidBlock(OIL, FabricBlockSettings.copyOf(Blocks.WATER)){ });
        OIL_BUCKET = Registry.register(Registries.ITEM, id("oil_bucket"), new BucketItem(OIL, new Item.Settings().maxCount(1)));

        FUEL = Registry.register(Registries.FLUID, id("fuel"), new SimpleFluid.Still());
        FUEL_FLOWING = Registry.register(Registries.FLUID, id("fuel_flowing"), new SimpleFluid.Flowing());
        FUEL_BLOCK = Registry.register(Registries.BLOCK, id("fuel_block"), new FluidBlock(FUEL, FabricBlockSettings.copyOf(Blocks.WATER)){ });
        FUEL_BUCKET = Registry.register(Registries.ITEM, id("fuel_bucket"), new BucketItem(FUEL, new Item.Settings().maxCount(1)));
    }

    private static Identifier id(String p){ return new Identifier("buildcraft", p); }
}