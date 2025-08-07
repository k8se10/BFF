package net.mod.buildcraft.fabric.client.audio;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.mod.buildcraft.fabric.block.entity.CombustionEngineEntity;
import net.mod.buildcraft.fabric.block.entity.SteamEngineEntity;
import net.mod.buildcraft.fabric.block.entity.RefineryEntity;
import net.mod.buildcraft.fabric.block.entity.QuarryEntity;
import net.mod.buildcraft.fabric.registry.BCSounds;

public class LoopingAudioManager {
    private static final Long2ObjectOpenHashMap<LoopingTileSound> loops = new Long2ObjectOpenHashMap<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(mc -> tick(mc));
    }

    private static void tick(MinecraftClient mc) {
        if (mc.world == null || mc.player == null) return;
        var world = mc.world;
        SoundManager sm = mc.getSoundManager();

        // Iterate nearby loaded tiles in a small radius around player (performance-safe)
        BlockPos p = mc.player.getBlockPos();
        int r = 32;
        BlockPos.Mutable m = new BlockPos.Mutable();
        for (int dx = -r; dx <= r; dx += 8) {
            for (int dy = -r; dy <= r; dy += 8) {
                for (int dz = -r; dz <= r; dz += 8) {
                    m.set(p.getX() + dx, p.getY() + dy, p.getZ() + dz);
                    var be = world.getBlockEntity(m);
                    if (be == null) continue;
                    Identifier snd = null;
                    if (be instanceof CombustionEngineEntity ce && ce.isRunningClient()) snd = BCSounds.ENGINE_CHUG.getId();
                    else if (be instanceof SteamEngineEntity se && se.isRunningClient()) snd = BCSounds.ENGINE_CHUG.getId();
                    else if (be instanceof RefineryEntity re && re.isActiveClient()) snd = BCSounds.REFINERY_HUM.getId();
                    else if (be instanceof QuarryEntity qe && qe.isActive()) snd = BCSounds.ENGINE_CHUG.getId(); // reuse chug for quarry

                    long key = m.asLong();
                    if (snd != null) {
                        if (!loops.containsKey(key)) {
                            LoopingTileSound l = new LoopingTileSound(snd, m.toImmutable());
                            loops.put(key, l);
                            sm.play(l);
                        }
                    } else {
                        LoopingTileSound l = loops.remove(key);
                        if (l != null) l.stopLoop();
                    }
                }
            }
        }
        // Cleanup far loops
        loops.long2ObjectEntrySet().removeIf(e -> {
            BlockPos bp = BlockPos.fromLong(e.getLongKey());
            if (bp.getSquaredDistance(p) > (r+8)*(r+8)) {
                e.getValue().stopLoop(); return true;
            }
            return false;
        });
    }
}
