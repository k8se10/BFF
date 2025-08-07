package net.mod.buildcraft.fabric.worldgen)*biomeMultiplier;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.mod.buildcraft.fabric.config.BCConfig;
import net.mod.buildcraft.fabric.fluid.BCFluids;

public class OilShaftFeature extends Feature<DefaultFeatureConfig> { private static final java.util.Random LOCAL_RAND = new java.util.Random();
    private static final BlockState OIL = BCFluids.OIL_BLOCK.getDefaultState();
    public OilShaftFeature(Codec<DefaultFeatureConfig> codec){ super(codec); }

    private double getRarityMultiplier(StructureWorldAccess world, BlockPos pos){
        RegistryEntry<Biome> b = world.getBiome(pos);
        // Hard-key checks for common vanilla biomes; fall back to tags/temperature where sensible
        if (b.matchesKey(BiomeKeys.DESERT)) return BCConfig.OIL_RARITY_DESERT;
        if (b.matchesKey(BiomeKeys.BADLANDS) || b.matchesKey(BiomeKeys.ERODED_BADLANDS) || b.matchesKey(BiomeKeys.WOODED_BADLANDS)) return BCConfig.OIL_RARITY_BADLANDS;
        if (b.matchesKey(BiomeKeys.SAVANNA) || b.matchesKey(BiomeKeys.SAVANNA_PLATEAU) || b.matchesKey(BiomeKeys.WINDSWEPT_SAVANNA)) return BCConfig.OIL_RARITY_SAVANNA;
        if (b.matchesKey(BiomeKeys.PLAINS) || b.matchesKey(BiomeKeys.SUNFLOWER_PLAINS)) return BCConfig.OIL_RARITY_PLAINS;
        if (b.isIn(BiomeTags.IS_OCEAN)) return BCConfig.OIL_RARITY_OCEAN;
        if (b.matchesKey(BiomeKeys.SWAMP) || b.matchesKey(BiomeKeys.MANGROVE_SWAMP)) return BCConfig.OIL_RARITY_SWAMP;
        if (b.matchesKey(BiomeKeys.TAIGA) || b.matchesKey(BiomeKeys.OLD_GROWTH_PINE_TAIGA) || b.matchesKey(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA)) return BCConfig.OIL_RARITY_TAIGA;
        if (b.isIn(BiomeTags.IS_FOREST)) return BCConfig.OIL_RARITY_FOREST;
        if (b.isIn(BiomeTags.IS_SNOWY)) return BCConfig.OIL_RARITY_SNOWY;
        return BCConfig.OIL_RARITY_DEFAULT;
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx){
        StructureWorldAccess world = ctx.getWorld();
        BlockPos origin = ctx.getOrigin();
        Random rand = ctx.getRandom()!=null?ctx.getRandom():net.minecraft.util.math.random.Random.create(LOCAL_RAND.nextLong());

        // 1 in N chance (configurable) with biome multiplier
        double mult = getRarityMultiplier(world, origin);
        int n = (int)Math.max(1, Math.round(BCConfig.OIL_WORLDGEN_CHANCE * mult));
        if (rand.nextInt(n) != 0) return false;

        // Find ground height at origin within worldgen pass
        int groundY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
        if (groundY <= world.getBottomY()+8) return false;

        // Cap the shaft top to min(Y95, ground+2)
        int topYCap = Math.min(BCConfig.OIL_SHAFT_MAX_TOP_Y, groundY + BCConfig.OIL_SHAFT_MAX_ABOVE_GROUND);
        int topY = Math.min(topYCap, origin.getY());
        if (topY < groundY - 2) topY = groundY - 2; // keep near surface if sampled low

        // Choose depth
        int depth = rand.nextBetween(BCConfig.OIL_SHAFT_MIN_DEPTH, BCConfig.OIL_SHAFT_MAX_DEPTH);
        int bottomY = Math.max(world.getBottomY()+1, topY - depth);

        // Place a 1x shaft, plus a small 3x3 surface pool
        boolean placed = false;
        for (int y = topY; y >= bottomY; y--) {
            BlockPos p = new BlockPos(origin.getX(), y, origin.getZ());
            world.setBlockState(p, OIL, 2);
            placed = true;
        }

        // Surface pool 3x3 centered at (x, z) at height min(topY, groundY+1)
        int poolY = Math.min(topY, groundY + 1);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = new BlockPos(origin.getX() + dx, poolY, origin.getZ() + dz);
                // only place if air or replaceable (avoid breaking non-replaceables)
                if (world.isAir(p) || world.getBlockState(p).isIn(BlockTags.REPLACEABLE)) {
                    world.setBlockState(p, OIL, 2);
                }
            }
        }
        return placed;
    }
}
