package net.mod.buildcraft.fabric.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.mod.buildcraft.fabric.logic.TriggerRegistry;
import net.mod.buildcraft.fabric.logic.ActionRegistry;
import net.mod.buildcraft.fabric.logic.GateLogic;

public class GateEntity extends BlockEntity {
    private String triggerId = "pipe_has_items";
    private String actionId = "emit_redstone";
    private boolean active = false;

    public GateEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.GATE, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient) return;
        active = GateLogic.evaluate(this, world, pos, triggerId);
        GateLogic.execute(this, world, pos, actionId, active);
    }

    public void setTrigger(String id) { this.triggerId = id; }
    public void setAction(String id) { this.actionId = id; }
    public String getTrigger() { return triggerId; }
    public String getAction() { return actionId; }
    public boolean isActive() { return active; }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putString("Trigger", triggerId);
        tag.putString("Action", actionId);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        triggerId = tag.getString("Trigger");
        actionId = tag.getString("Action");
    }
}
