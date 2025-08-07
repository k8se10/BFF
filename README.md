# BuildCraft: Fabric 1.20.4

An advanced, modernized port of BuildCraft for Fabric, fully faithful to the original mod — quarries, pipes, gates, engines, oil, MJ power, builder/blueprints, and more.

## Features
- MJ-powered quarry with area config, GUI, collect-to-chest, and hologram preview.
- Item, fluid, and kinesis pipes with acceleration, extraction, and filtering.
- Diamond pipe: per-side 9-slot filters with color UI.
- Oil worldgen: biome-aware rarity, shaft-based geysers, fluid reservoirs.
- Refineries, tanks, and MJ engines (steam, combustion) with overheat logic.
- Gate system with tiers, logic conditions/actions, and facade support.
- Looping machine sounds, holograms, and glassy UIs.
- Fully shader-compatible (Iris-safe).

## Build
```sh
./gradlew build
```

## Config
- `config/buildcraft.json` lets you tweak MJ loss, quarry cost, oil rarity by biome, and more.


## Audio placeholders
Run `python tools/generate_placeholder_sounds.py --ogg` to synthesize and convert placeholder sounds.

**Fabric 1.20.4 port by OfficialK8**
