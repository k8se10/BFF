package net.mod.buildcraft.fabric.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.mod.buildcraft.fabric.block.PipeBlock;
import net.mod.buildcraft.fabric.block.CobblePipeBlock;
import net.mod.buildcraft.fabric.block.StonePipeBlock;
import net.mod.buildcraft.fabric.block.IronPipeBlock;
import net.mod.buildcraft.fabric.block.GoldPipeBlock;
import net.mod.buildcraft.fabric.block.TankBlock;
import net.mod.buildcraft.fabric.block.KinesisPipeBlock;
import net.mod.buildcraft.fabric.block.RedstoneEngineBlock;
import net.mod.buildcraft.fabric.block.FluidPipeBlock;
import net.mod.buildcraft.fabric.block.PumpBlock;
import net.mod.buildcraft.fabric.block.MiningWellBlock;
import net.mod.buildcraft.fabric.block.CobblestonePipeBlock;
import net.mod.buildcraft.fabric.block.StonePipeBlock;
import net.mod.buildcraft.fabric.block.GoldPipeBlock;
import net.mod.buildcraft.fabric.block.IronPipeBlock;
import net.mod.buildcraft.fabric.block.GoldFluidPipeBlock;
import net.mod.buildcraft.fabric.block.QuarryBlock;
import net.mod.buildcraft.fabric.block.CobbleFluidPipeBlock;
import net.mod.buildcraft.fabric.block.StoneFluidPipeBlock;
import net.mod.buildcraft.fabric.block.IronFluidPipeBlock;
import net.mod.buildcraft.fabric.block.GoldFluidPipeBlock;
import net.mod.buildcraft.fabric.block.DiamondFluidPipeBlock;

import net.mod.buildcraft.fabric.block.CobbleKinesisPipeBlock;
import net.mod.buildcraft.fabric.block.StoneKinesisPipeBlock;
import net.mod.buildcraft.fabric.block.GoldKinesisPipeBlock;
import net.mod.buildcraft.fabric.block.DiamondKinesisPipeBlock;

import net.mod.buildcraft.fabric.block.SteamEngineBlock;
import net.mod.buildcraft.fabric.block.CombustionEngineBlock;
import net.mod.buildcraft.fabric.block.ArchitectTableBlock;
import net.mod.buildcraft.fabric.block.BuilderBlock;
import net.mod.buildcraft.fabric.block.entity.ArchitectTableEntity;
import net.mod.buildcraft.fabric.block.entity.BuilderEntity;
import net.mod.buildcraft.fabric.item.BlueprintItem;

import net.mod.buildcraft.fabric.block.DiamondPipeBlock;
import net.mod.buildcraft.fabric.block.ObsidianPipeBlock;
import net.mod.buildcraft.fabric.block.VoidPipeBlock;
import net.mod.buildcraft.fabric.block.RefineryBlock;
import net.mod.buildcraft.fabric.block.entity.RefineryEntity;
import net.mod.buildcraft.fabric.block.entity.DiamondPipeEntity;
import net.mod.buildcraft.fabric.block.entity.ObsidianPipeEntity;
import net.mod.buildcraft.fabric.block.entity.VoidPipeEntity;


import net.mod.buildcraft.fabric.block.entity.SteamEngineEntity;
import net.mod.buildcraft.fabric.block.entity.CombustionEngineEntity;

import net.mod.buildcraft.fabric.block.LaserBlock;
import net.mod.buildcraft.fabric.block.AssemblyTableBlock;
import net.mod.buildcraft.fabric.block.FillerBlock;
import net.mod.buildcraft.fabric.block.LandmarkBlock;
import net.mod.buildcraft.fabric.block.WoodenPipeBlock;
import net.mod.buildcraft.fabric.block.entity.PipeBlockEntity;
import net.mod.buildcraft.fabric.block.entity.TankBlockEntity;
import net.mod.buildcraft.fabric.block.entity.KinesisPipeEntity;
import net.mod.buildcraft.fabric.block.entity.RedstoneEngineEntity;
import net.mod.buildcraft.fabric.block.entity.FluidPipeEntity;
import net.mod.buildcraft.fabric.block.entity.PumpEntity;
import net.mod.buildcraft.fabric.block.entity.MiningWellEntity;
import net.mod.buildcraft.fabric.block.entity.QuarryEntity;
import net.mod.buildcraft.fabric.block.entity.LaserEntity;
import net.mod.buildcraft.fabric.block.entity.AssemblyTableEntity;
import net.mod.buildcraft.fabric.block.entity.FillerEntity;
import net.mod.buildcraft.fabric.block.entity.LandmarkEntity;
import net.mod.buildcraft.fabric.item.WrenchItem;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

public class BCContent {
    // Blocks
    public static Block PIPE_BLOCK;
    public static Block COBBLE_PIPE_BLOCK;
    public static Block STONE_PIPE_BLOCK;
    public static Block IRON_PIPE_BLOCK;
    public static Block GOLD_PIPE_BLOCK;
    public static Block WOODEN_PIPE_BLOCK;
    public static Block TANK_BLOCK;
    public static Block KINESIS_PIPE_BLOCK;
    public static Block REDSTONE_ENGINE_BLOCK;
    public static Block FLUID_PIPE_BLOCK;
    public static Block PUMP_BLOCK;
    public static Block MINING_WELL_BLOCK;
    public static Block QUARRY_BLOCK;
    public static Block LASER_BLOCK;
    public static Block ASSEMBLY_TABLE_BLOCK;
    public static Block FILLER_BLOCK;
    public static Block LANDMARK_BLOCK;
    public static Block FLUID_PIPE_COBBLE_BLOCK;
    public static Block FLUID_PIPE_STONE_BLOCK;
    public static Block FLUID_PIPE_IRON_BLOCK;
    public static Block FLUID_PIPE_GOLD_BLOCK;
    public static Block FLUID_PIPE_DIAMOND_BLOCK;

    public static Block KINESIS_PIPE_COBBLE_BLOCK;
    public static Block KINESIS_PIPE_STONE_BLOCK;
    public static Block KINESIS_PIPE_GOLD_BLOCK;
    public static Block KINESIS_PIPE_DIAMOND_BLOCK;

    public static Block STEAM_ENGINE_BLOCK;
    public static Block COMBUSTION_ENGINE_BLOCK;
    public static Block ARCHITECT_TABLE_BLOCK;
    public static Block BUILDER_BLOCK;
    public static Item BLUEPRINT_ITEM;

    public static Block DIAMOND_PIPE_BLOCK;
    public static Block OBSIDIAN_PIPE_BLOCK;
    public static Block VOID_PIPE_BLOCK;
    public static Block REFINERY_BLOCK;


    public static Block COBBLESTONE_PIPE_BLOCK;
    public static Block STONE_PIPE_BLOCK;
    public static Block GOLD_PIPE_BLOCK;
    public static Block IRON_PIPE_BLOCK;
    public static Block GOLD_FLUID_PIPE_BLOCK;

    // Block entities
    public static BlockEntityType<PipeBlockEntity> PIPE_BE;
    public static BlockEntityType<TankBlockEntity> TANK_BE;
    public static BlockEntityType<KinesisPipeEntity> KINESIS_BE;
    public static BlockEntityType<RedstoneEngineEntity> REDSTONE_ENGINE_BE;
    public static BlockEntityType<FluidPipeEntity> FLUID_PIPE_BE;
    public static BlockEntityType<PumpEntity> PUMP_BE;
    public static BlockEntityType<MiningWellEntity> MINING_WELL_BE;
    public static BlockEntityType<QuarryEntity> QUARRY_BE;
    public static BlockEntityType<LaserEntity> LASER_BE;
    public static BlockEntityType<AssemblyTableEntity> ASSEMBLY_TABLE_BE;
    public static BlockEntityType<FillerEntity> FILLER_BE;
    public static BlockEntityType<LandmarkEntity> LANDMARK_BE;
    public static BlockEntityType<SteamEngineEntity> STEAM_ENGINE_BE;
    public static BlockEntityType<CombustionEngineEntity> COMBUSTION_ENGINE_BE;
    public static BlockEntityType<ArchitectTableEntity> ARCHITECT_TABLE_BE;
    public static BlockEntityType<BuilderEntity> BUILDER_BE;
    public static BlockEntityType<RefineryEntity> REFINERY_BE;
    public static BlockEntityType<DiamondPipeEntity> DIAMOND_PIPE_BE;
    public static BlockEntityType<ObsidianPipeEntity> OBSIDIAN_PIPE_BE;
    public static BlockEntityType<VoidPipeEntity> VOID_PIPE_BE;



    // Items
    public static Item PIPE_ITEM;
    public static Item COBBLE_PIPE_ITEM;
    public static Item STONE_PIPE_ITEM;
    public static Item IRON_PIPE_ITEM;
    public static Item GOLD_PIPE_ITEM;
    public static Item WOODEN_PIPE_ITEM;
    public static Item WRENCH_ITEM;
    public static Item TANK_ITEM;
    public static Item KINESIS_PIPE_ITEM;
    public static Item REDSTONE_ENGINE_ITEM;
    public static Item FLUID_PIPE_ITEM;
    public static Item PUMP_ITEM;
    public static Item MINING_WELL_ITEM;
    public static Item QUARRY_ITEM;
    public static Item LASER_ITEM;
    public static Item ASSEMBLY_TABLE_ITEM;
    public static Item FILLER_ITEM;
    public static Item LANDMARK_ITEM;
    public static Item FLUID_PIPE_COBBLE_ITEM;
    public static Item FLUID_PIPE_STONE_ITEM;
    public static Item FLUID_PIPE_IRON_ITEM;
    public static Item FLUID_PIPE_GOLD_ITEM;
    public static Item FLUID_PIPE_DIAMOND_ITEM;

    public static Item KINESIS_PIPE_COBBLE_ITEM;
    public static Item KINESIS_PIPE_STONE_ITEM;
    public static Item KINESIS_PIPE_GOLD_ITEM;
    public static Item KINESIS_PIPE_DIAMOND_ITEM;

    public static Item STEAM_ENGINE_ITEM;
    public static Item COMBUSTION_ENGINE_ITEM;
    public static Item DIAMOND_PIPE_ITEM;
    public static Item OBSIDIAN_PIPE_ITEM;
    public static Item VOID_PIPE_ITEM;
    public static Item REFINERY_ITEM;


    public static Item COBBLESTONE_PIPE_ITEM;
    public static Item STONE_PIPE_ITEM;
    public static Item GOLD_PIPE_ITEM;
    public static Item IRON_PIPE_ITEM;
    public static Item GOLD_FLUID_PIPE_ITEM;

    // IDs
    public static final Identifier PIPE_ID = id("pipe_basic");
    public static final Identifier COBBLE_PIPE_ID = id("pipe_cobble");
    public static final Identifier STONE_PIPE_ID = id("pipe_stone");
    public static final Identifier IRON_PIPE_ID = id("pipe_iron");
    public static final Identifier GOLD_PIPE_ID = id("pipe_gold");
    public static final Identifier WOODEN_PIPE_ID = id("pipe_wooden");
    public static final Identifier WRENCH_ID = id("wrench");
    public static final Identifier TANK_ID = id("tank");
    public static final Identifier KINESIS_ID = id("kinesis_pipe");
    public static final Identifier REDSTONE_ENGINE_ID = id("redstone_engine");
    public static final Identifier FLUID_PIPE_ID = id("fluid_pipe");
    public static final Identifier PUMP_ID = id("pump");
    public static final Identifier MINING_WELL_ID = id("mining_well");
    public static final Identifier QUARRY_ID = id("quarry");
    public static final Identifier LASER_ID = id("laser");
    public static final Identifier ASSEMBLY_TABLE_ID = id("assembly_table");
    public static final Identifier FILLER_ID = id("filler");
    public static final Identifier LANDMARK_ID = id("landmark");
    public static final Identifier FLUID_PIPE_COBBLE_ID = id("fluid_pipe_cobble");
    public static final Identifier FLUID_PIPE_STONE_ID = id("fluid_pipe_stone");
    public static final Identifier FLUID_PIPE_IRON_ID = id("fluid_pipe_iron");
    public static final Identifier FLUID_PIPE_GOLD_ID = id("fluid_pipe_gold");
    public static final Identifier FLUID_PIPE_DIAMOND_ID = id("fluid_pipe_diamond");

    public static final Identifier KINESIS_PIPE_COBBLE_ID = id("kinesis_pipe_cobble");
    public static final Identifier KINESIS_PIPE_STONE_ID = id("kinesis_pipe_stone");
    public static final Identifier KINESIS_PIPE_GOLD_ID = id("kinesis_pipe_gold");
    public static final Identifier KINESIS_PIPE_DIAMOND_ID = id("kinesis_pipe_diamond");

    public static final Identifier STEAM_ENGINE_ID = id("steam_engine");
    public static final Identifier COMBUSTION_ENGINE_ID = id("combustion_engine");
    public static final Identifier ARCHITECT_TABLE_ID = id("architect_table");
    public static final Identifier BUILDER_ID = id("builder");
    public static final Identifier BLUEPRINT_ID = id("blueprint");

    public static final Identifier DIAMOND_PIPE_ID = id("pipe_diamond");
    public static final Identifier OBSIDIAN_PIPE_ID = id("pipe_obsidian");
    public static final Identifier VOID_PIPE_ID = id("pipe_void");
    public static final Identifier REFINERY_ID = id("refinery");


    public static final Identifier COBBLESTONE_PIPE_ID = id("pipe_cobblestone");
    public static final Identifier STONE_PIPE_ID = id("pipe_stone");
    public static final Identifier GOLD_PIPE_ID = id("pipe_gold");
    public static final Identifier IRON_PIPE_ID = id("pipe_iron");
    public static final Identifier GOLD_FLUID_PIPE_ID = id("pipe_gold_fluid");

    // Creative tab
    public static final net.minecraft.item.ItemGroup ITEM_GROUP = FabricItemGroup.builder(id("group"))
            .icon(() -> new ItemStack(PIPE_ITEM))
            .displayName(Text.literal("BuildCraft"))
            .build();

    public static void registerAll() {
        // Blocks + items
        PIPE_BLOCK = Registry.register(Registries.BLOCK, PIPE_ID, new PipeBlock());
        PIPE_ITEM = Registry.register(Registries.ITEM, PIPE_ID, new BlockItem(PIPE_BLOCK, new Item.Settings()));

        COBBLE_PIPE_BLOCK = Registry.register(Registries.BLOCK, COBBLE_PIPE_ID, new CobblePipeBlock());
        COBBLE_PIPE_ITEM = Registry.register(Registries.ITEM, COBBLE_PIPE_ID, new BlockItem(COBBLE_PIPE_BLOCK, new Item.Settings()));

        STONE_PIPE_BLOCK = Registry.register(Registries.BLOCK, STONE_PIPE_ID, new StonePipeBlock());
        STONE_PIPE_ITEM = Registry.register(Registries.ITEM, STONE_PIPE_ID, new BlockItem(STONE_PIPE_BLOCK, new Item.Settings()));

        IRON_PIPE_BLOCK = Registry.register(Registries.BLOCK, IRON_PIPE_ID, new IronPipeBlock());
        IRON_PIPE_ITEM = Registry.register(Registries.ITEM, IRON_PIPE_ID, new BlockItem(IRON_PIPE_BLOCK, new Item.Settings()));

        GOLD_PIPE_BLOCK = Registry.register(Registries.BLOCK, GOLD_PIPE_ID, new GoldPipeBlock());
        GOLD_PIPE_ITEM = Registry.register(Registries.ITEM, GOLD_PIPE_ID, new BlockItem(GOLD_PIPE_BLOCK, new Item.Settings()));

        WOODEN_PIPE_BLOCK = Registry.register(Registries.BLOCK, WOODEN_PIPE_ID, new WoodenPipeBlock());
        WOODEN_PIPE_ITEM = Registry.register(Registries.ITEM, WOODEN_PIPE_ID, new BlockItem(WOODEN_PIPE_BLOCK, new Item.Settings()));

        TANK_BLOCK = Registry.register(Registries.BLOCK, TANK_ID, new TankBlock());
        TANK_ITEM = Registry.register(Registries.ITEM, TANK_ID, new BlockItem(TANK_BLOCK, new Item.Settings()));

        KINESIS_PIPE_BLOCK = Registry.register(Registries.BLOCK, KINESIS_ID, new KinesisPipeBlock());
        KINESIS_PIPE_ITEM = Registry.register(Registries.ITEM, KINESIS_ID, new BlockItem(KINESIS_PIPE_BLOCK, new Item.Settings()));

        REDSTONE_ENGINE_BLOCK = Registry.register(Registries.BLOCK, REDSTONE_ENGINE_ID, new RedstoneEngineBlock());
        REDSTONE_ENGINE_ITEM = Registry.register(Registries.ITEM, REDSTONE_ENGINE_ID, new BlockItem(REDSTONE_ENGINE_BLOCK, new Item.Settings()));

        FLUID_PIPE_BLOCK = Registry.register(Registries.BLOCK, FLUID_PIPE_ID, new FluidPipeBlock());
        FLUID_PIPE_ITEM = Registry.register(Registries.ITEM, FLUID_PIPE_ID, new BlockItem(FLUID_PIPE_BLOCK, new Item.Settings()));

        PUMP_BLOCK = Registry.register(Registries.BLOCK, PUMP_ID, new PumpBlock());
        PUMP_ITEM = Registry.register(Registries.ITEM, PUMP_ID, new BlockItem(PUMP_BLOCK, new Item.Settings()));

        // Fluid pipe tiers
        FLUID_PIPE_COBBLE_BLOCK = Registry.register(Registries.BLOCK, FLUID_PIPE_COBBLE_ID, new CobbleFluidPipeBlock());
        FLUID_PIPE_COBBLE_ITEM  = Registry.register(Registries.ITEM,  FLUID_PIPE_COBBLE_ID, new BlockItem(FLUID_PIPE_COBBLE_BLOCK, new Item.Settings()));

        FLUID_PIPE_STONE_BLOCK = Registry.register(Registries.BLOCK, FLUID_PIPE_STONE_ID, new StoneFluidPipeBlock());
        FLUID_PIPE_STONE_ITEM  = Registry.register(Registries.ITEM,  FLUID_PIPE_STONE_ID, new BlockItem(FLUID_PIPE_STONE_BLOCK, new Item.Settings()));

        FLUID_PIPE_IRON_BLOCK = Registry.register(Registries.BLOCK, FLUID_PIPE_IRON_ID, new IronFluidPipeBlock());
        FLUID_PIPE_IRON_ITEM  = Registry.register(Registries.ITEM,  FLUID_PIPE_IRON_ID, new BlockItem(FLUID_PIPE_IRON_BLOCK, new Item.Settings()));

        FLUID_PIPE_GOLD_BLOCK = Registry.register(Registries.BLOCK, FLUID_PIPE_GOLD_ID, new GoldFluidPipeBlock());
        FLUID_PIPE_GOLD_ITEM  = Registry.register(Registries.ITEM,  FLUID_PIPE_GOLD_ID, new BlockItem(FLUID_PIPE_GOLD_BLOCK, new Item.Settings()));

        FLUID_PIPE_DIAMOND_BLOCK = Registry.register(Registries.BLOCK, FLUID_PIPE_DIAMOND_ID, new DiamondFluidPipeBlock());
        FLUID_PIPE_DIAMOND_ITEM  = Registry.register(Registries.ITEM,  FLUID_PIPE_DIAMOND_ID, new BlockItem(FLUID_PIPE_DIAMOND_BLOCK, new Item.Settings()));

        // Kinesis tiers
        KINESIS_PIPE_COBBLE_BLOCK = Registry.register(Registries.BLOCK, KINESIS_PIPE_COBBLE_ID, new CobbleKinesisPipeBlock());
        KINESIS_PIPE_COBBLE_ITEM  = Registry.register(Registries.ITEM,  KINESIS_PIPE_COBBLE_ID, new BlockItem(KINESIS_PIPE_COBBLE_BLOCK, new Item.Settings()));

        KINESIS_PIPE_STONE_BLOCK = Registry.register(Registries.BLOCK, KINESIS_PIPE_STONE_ID, new StoneKinesisPipeBlock());
        KINESIS_PIPE_STONE_ITEM  = Registry.register(Registries.ITEM,  KINESIS_PIPE_STONE_ID, new BlockItem(KINESIS_PIPE_STONE_BLOCK, new Item.Settings()));

        KINESIS_PIPE_GOLD_BLOCK = Registry.register(Registries.BLOCK, KINESIS_PIPE_GOLD_ID, new GoldKinesisPipeBlock());
        KINESIS_PIPE_GOLD_ITEM  = Registry.register(Registries.ITEM,  KINESIS_PIPE_GOLD_ID, new BlockItem(KINESIS_PIPE_GOLD_BLOCK, new Item.Settings()));

        KINESIS_PIPE_DIAMOND_BLOCK = Registry.register(Registries.BLOCK, KINESIS_PIPE_DIAMOND_ID, new DiamondKinesisPipeBlock());
        KINESIS_PIPE_DIAMOND_ITEM  = Registry.register(Registries.ITEM,  KINESIS_PIPE_DIAMOND_ID, new BlockItem(KINESIS_PIPE_DIAMOND_BLOCK, new Item.Settings()));

        // Engine variants
        STEAM_ENGINE_BLOCK = Registry.register(Registries.BLOCK, STEAM_ENGINE_ID, new SteamEngineBlock());
        STEAM_ENGINE_ITEM  = Registry.register(Registries.ITEM,  STEAM_ENGINE_ID, new BlockItem(STEAM_ENGINE_BLOCK, new Item.Settings()));

        COMBUSTION_ENGINE_BLOCK = Registry.register(Registries.BLOCK, COMBUSTION_ENGINE_ID, new CombustionEngineBlock());
        COMBUSTION_ENGINE_ITEM  = Registry.register(Registries.ITEM,  COMBUSTION_ENGINE_ID, new BlockItem(COMBUSTION_ENGINE_BLOCK, new Item.Settings()));

        ARCHITECT_TABLE_BLOCK = Registry.register(Registries.BLOCK, ARCHITECT_TABLE_ID, new ArchitectTableBlock());
        Registry.register(Registries.ITEM, ARCHITECT_TABLE_ID, new BlockItem(ARCHITECT_TABLE_BLOCK, new Item.Settings()));
        BUILDER_BLOCK = Registry.register(Registries.BLOCK, BUILDER_ID, new BuilderBlock());
        Registry.register(Registries.ITEM, BUILDER_ID, new BlockItem(BUILDER_BLOCK, new Item.Settings()));
        BLUEPRINT_ITEM = Registry.register(Registries.ITEM, BLUEPRINT_ID, new BlueprintItem(new Item.Settings()));


        DIAMOND_PIPE_BLOCK = Registry.register(Registries.BLOCK, DIAMOND_PIPE_ID, new DiamondPipeBlock());
        DIAMOND_PIPE_ITEM = Registry.register(Registries.ITEM, DIAMOND_PIPE_ID, new BlockItem(DIAMOND_PIPE_BLOCK, new Item.Settings()));

        OBSIDIAN_PIPE_BLOCK = Registry.register(Registries.BLOCK, OBSIDIAN_PIPE_ID, new ObsidianPipeBlock());
        OBSIDIAN_PIPE_ITEM = Registry.register(Registries.ITEM, OBSIDIAN_PIPE_ID, new BlockItem(OBSIDIAN_PIPE_BLOCK, new Item.Settings()));

        VOID_PIPE_BLOCK = Registry.register(Registries.BLOCK, VOID_PIPE_ID, new VoidPipeBlock());
        VOID_PIPE_ITEM = Registry.register(Registries.ITEM, VOID_PIPE_ID, new BlockItem(VOID_PIPE_BLOCK, new Item.Settings()));

        REFINERY_BLOCK = Registry.register(Registries.BLOCK, REFINERY_ID, new RefineryBlock());
        REFINERY_ITEM = Registry.register(Registries.ITEM, REFINERY_ID, new BlockItem(REFINERY_BLOCK, new Item.Settings()));



        MINING_WELL_BLOCK = Registry.register(Registries.BLOCK, MINING_WELL_ID, new MiningWellBlock());
        MINING_WELL_ITEM = Registry.register(Registries.ITEM, MINING_WELL_ID, new BlockItem(MINING_WELL_BLOCK, new Item.Settings()));

        QUARRY_BLOCK = Registry.register(Registries.BLOCK, QUARRY_ID, new QuarryBlock());
        QUARRY_ITEM = Registry.register(Registries.ITEM, QUARRY_ID, new BlockItem(QUARRY_BLOCK, new Item.Settings()));

        LASER_BLOCK = Registry.register(Registries.BLOCK, LASER_ID, new LaserBlock());
        LASER_ITEM = Registry.register(Registries.ITEM, LASER_ID, new BlockItem(LASER_BLOCK, new Item.Settings()));

        ASSEMBLY_TABLE_BLOCK = Registry.register(Registries.BLOCK, ASSEMBLY_TABLE_ID, new AssemblyTableBlock());
        ASSEMBLY_TABLE_ITEM = Registry.register(Registries.ITEM, ASSEMBLY_TABLE_ID, new BlockItem(ASSEMBLY_TABLE_BLOCK, new Item.Settings()));

        FILLER_BLOCK = Registry.register(Registries.BLOCK, FILLER_ID, new FillerBlock());
        FILLER_ITEM = Registry.register(Registries.ITEM, FILLER_ID, new BlockItem(FILLER_BLOCK, new Item.Settings()));

        LANDMARK_BLOCK = Registry.register(Registries.BLOCK, LANDMARK_ID, new LandmarkBlock());
        LANDMARK_ITEM = Registry.register(Registries.ITEM, LANDMARK_ID, new BlockItem(LANDMARK_BLOCK, new Item.Settings()));

COBBLESTONE_PIPE_BLOCK = Registry.register(Registries.BLOCK, COBBLESTONE_PIPE_ID, new CobblestonePipeBlock());
COBBLESTONE_PIPE_ITEM = Registry.register(Registries.ITEM, COBBLESTONE_PIPE_ID, new BlockItem(COBBLESTONE_PIPE_BLOCK, new Item.Settings()));

STONE_PIPE_BLOCK = Registry.register(Registries.BLOCK, STONE_PIPE_ID, new StonePipeBlock());
STONE_PIPE_ITEM = Registry.register(Registries.ITEM, STONE_PIPE_ID, new BlockItem(STONE_PIPE_BLOCK, new Item.Settings()));

GOLD_PIPE_BLOCK = Registry.register(Registries.BLOCK, GOLD_PIPE_ID, new GoldPipeBlock());
GOLD_PIPE_ITEM = Registry.register(Registries.ITEM, GOLD_PIPE_ID, new BlockItem(GOLD_PIPE_BLOCK, new Item.Settings()));

IRON_PIPE_BLOCK = Registry.register(Registries.BLOCK, IRON_PIPE_ID, new IronPipeBlock());
IRON_PIPE_ITEM = Registry.register(Registries.ITEM, IRON_PIPE_ID, new BlockItem(IRON_PIPE_BLOCK, new Item.Settings()));

GOLD_FLUID_PIPE_BLOCK = Registry.register(Registries.BLOCK, GOLD_FLUID_PIPE_ID, new GoldFluidPipeBlock());
GOLD_FLUID_PIPE_ITEM = Registry.register(Registries.ITEM, GOLD_FLUID_PIPE_ID, new BlockItem(GOLD_FLUID_PIPE_BLOCK, new Item.Settings()));


        // Block entities
        PIPE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("pipe_be"),
                BlockEntityType.Builder.create(PipeBlockEntity::new, PIPE_BLOCK, WOODEN_PIPE_BLOCK).build(null));

        TANK_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("tank_be"),
                BlockEntityType.Builder.create(TankBlockEntity::new, TANK_BLOCK).build(null));

        KINESIS_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("kinesis_be"),
                BlockEntityType.Builder.create(KinesisPipeEntity::new, KINESIS_PIPE_BLOCK).build(null));

        REDSTONE_ENGINE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("redstone_engine_be"),
                BlockEntityType.Builder.create(RedstoneEngineEntity::new, REDSTONE_ENGINE_BLOCK).build(null));

        FLUID_PIPE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("fluid_pipe_be"),
                BlockEntityType.Builder.create(FluidPipeEntity::new, FLUID_PIPE_BLOCK).build(null));

        PUMP_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("pump_be"),
                BlockEntityType.Builder.create(PumpEntity::new, PUMP_BLOCK).build(null));

        MINING_WELL_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("mining_well_be"),
                BlockEntityType.Builder.create(MiningWellEntity::new, MINING_WELL_BLOCK).build(null));

        QUARRY_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("quarry_be"),
                BlockEntityType.Builder.create(QuarryEntity::new, QUARRY_BLOCK).build(null));

        LASER_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("laser_be"),
                BlockEntityType.Builder.create(LaserEntity::new, LASER_BLOCK).build(null));

        ASSEMBLY_TABLE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("assembly_table_be"),
                BlockEntityType.Builder.create(AssemblyTableEntity::new, ASSEMBLY_TABLE_BLOCK).build(null));

        FILLER_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("filler_be"),
                BlockEntityType.Builder.create(FillerEntity::new, FILLER_BLOCK).build(null));

        LANDMARK_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("landmark_be"),
                BlockEntityType.Builder.create(FillerEntity::new, FILLER_BLOCK).build(null));

        // Simple wrench item placeholder
        WRENCH_ITEM = Registry.register(Registries.ITEM, WRENCH_ID, new WrenchItem(new Item.Settings().maxCount(1)));

        // Register the item group after items exist
        Registry.register(Registries.ITEM_GROUP, id("group"), ITEM_GROUP);

        // Register storages
        BCStorages.init();
    }

    public static Identifier id(String path) {
        return new Identifier("buildcraft", path);
    }
}


        STEAM_ENGINE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("steam_engine_be"),
                BlockEntityType.Builder.create(SteamEngineEntity::new, STEAM_ENGINE_BLOCK).build(null));

        COMBUSTION_ENGINE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("combustion_engine_be"),
                BlockEntityType.Builder.create(CombustionEngineEntity::new, COMBUSTION_ENGINE_BLOCK).build(null));


        REFINERY_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("refinery_be"),
                BlockEntityType.Builder.create(RefineryEntity::new, REFINERY_BLOCK).build(null));
        DIAMOND_PIPE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("diamond_pipe_be"),
                BlockEntityType.Builder.create(DiamondPipeEntity::new, DIAMOND_PIPE_BLOCK).build(null));
        OBSIDIAN_PIPE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("obsidian_pipe_be"),
                BlockEntityType.Builder.create(ObsidianPipeEntity::new, OBSIDIAN_PIPE_BLOCK).build(null));
        VOID_PIPE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("void_pipe_be"),
                BlockEntityType.Builder.create(VoidPipeEntity::new, VOID_PIPE_BLOCK).build(null));


        ARCHITECT_TABLE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("architect_table_be"),
                BlockEntityType.Builder.create(ArchitectTableEntity::new, ARCHITECT_TABLE_BLOCK).build(null));
        BUILDER_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("builder_be"),
                BlockEntityType.Builder.create(BuilderEntity::new, BUILDER_BLOCK).build(null));