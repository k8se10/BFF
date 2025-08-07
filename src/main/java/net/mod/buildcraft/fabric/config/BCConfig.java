package net.mod.buildcraft.fabric.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.Path;

public class BCConfig {
    public static java.util.Map<String, Double> OIL_BIOME_MULT = new java.util.HashMap<>();
    public static double KINESIS_LINE_LOSS = 0.01;
    public static double QUARRY_COST_MJ   = 0.20;
    public static double OIL_RARITY_SCALE_DESERT = 1.5;
    public static double OIL_RARITY_SCALE_FOREST = 0.5;

    public static void load(Path gameDir){
        try {
            Path cfg = gameDir.resolve("config/buildcraft.json");
            if (Files.exists(cfg)) {
                String s = Files.readString(cfg);
                JsonObject j = new Gson().fromJson(s, JsonObject.class);
                if (j.has("kinesis_line_loss")) KINESIS_LINE_LOSS = j.get("kinesis_line_loss").getAsDouble();
                if (j.has("quarry_cost_mj")) QUARRY_COST_MJ = j.get("quarry_cost_mj").getAsDouble();
                if (j.has("oil_rarity_desert")) OIL_RARITY_SCALE_DESERT = j.get("oil_rarity_desert").getAsDouble();
                if (j.has("oil_rarity_forest")) OIL_RARITY_SCALE_FOREST = j.get("oil_rarity_forest").getAsDouble();
                if (j.has("oil_biome_multipliers")) {
                    var obj = j.getAsJsonObject("oil_biome_multipliers");
                    for (var e : obj.entrySet()) { OIL_BIOME_MULT.put(e.getKey(), e.getValue().getAsDouble()); }
                }
            }
        } catch (Exception ignored) {}
    }
}
