package net.mod.buildcraft.fabric.client.audio;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.mod.buildcraft.fabric.registry.BCSounds;

/** A simple looping sound bound to a BlockPos that can be stopped by setting active=false. */
public class LoopingTileSound extends AbstractSoundInstance {
    private final BlockPos posBlock;
    private boolean active = true;

    public LoopingTileSound(Identifier soundId, BlockPos pos) {
        super(soundId, SoundCategory.BLOCKS, SoundInstance.createRandom());
        this.repeat = true;
        this.repeatDelay = 0;
        this.posBlock = pos;
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;
        this.volume = 0.6f;
        this.pitch = 1.0f;
    }

    public void stopLoop() {
        this.active = false;
    }

    @Override
    public boolean isDone() {
        return !active;
    }

    @Override
    public void tick() {
        // Follow the block position; nothing else needed here.
    }
}
