package net.mod.buildcraft.fabric.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.mod.buildcraft.fabric.worldgen.OilShaftFeature;

import java.util.List;

public class BCWorldgen {
    public static Feature<DefaultFeatureConfig> OIL_SHAFT_FEATURE;
    public static RegistryKey<ConfiguredFeature<?, ?>> OIL_SHAFT_CONFIGURED;
    public static RegistryKey<PlacedFeature> OIL_SHAFT_PLACED;

    public static void register() {
        OIL_SHAFT_FEATURE = Registry.register(Registries.FEATURE, id("oil_shaft"), new OilShaftFeature(DefaultFeatureConfig.CODEC));

        OIL_SHAFT_CONFIGURED = RegistryKey.of(Registries.CONFIGURED_FEATURE.getKey(), id("oil_shaft_configured"));
        OIL_SHAFT_PLACED = RegistryKey.of(Registries.PLACED_FEATURE.getKey(), id("oil_shaft_placed"));

        // Register configured & placed feature at runtime using bootstrap callbacks
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            var cf = new ConfiguredFeature<>(OIL_SHAFT_FEATURE, new DefaultFeatureConfig());
            Registry.register(server.getRegistryManager().getMutable(Registries.CONFIGURED_FEATURE), OIL_SHAFT_CONFIGURED.getValue(), cf);

            List<PlacementModifier> modifiers = List.of(
                // Rarity handled again in feature via config; this rarity is a secondary coarse filter (1 in 8 chunks)
                RarityFilterPlacementModifier.of(8),
                BiomePlacementModifier.of()
            );
            var pf = new PlacedFeature(RegistryEntry.of(cf), modifiers);
            Registry.register(server.getRegistryManager().getMutable(Registries.PLACED_FEATURE), OIL_SHAFT_PLACED.getValue(), pf);
        });

        // Add to all overworld biomes during underground structures step
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.LAKES,
                OIL_SHAFT_PLACED
        );
    }

    private static Identifier id(String p){ return new Identifier("buildcraft", p); }
}
