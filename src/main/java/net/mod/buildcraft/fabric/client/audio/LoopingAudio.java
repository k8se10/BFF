package net.mod.buildcraft.fabric.client.audio;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.mod.buildcraft.fabric.registry.BCSounds;

public class LoopingAudio {
    private static PositionedSoundInstance engineLoop;
    private static PositionedSoundInstance refineryLoop;

    public static void clientTick(){
        var mc = MinecraftClient.getInstance();
        if(mc.world==null) return;
        // naive loop check: if any engine is active nearby, ensure loop is playing
        // NOTE: simplified; a proper impl would attach per-BE sounds
    }
}