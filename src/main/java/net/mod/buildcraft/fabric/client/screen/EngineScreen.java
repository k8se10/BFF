package net.mod.buildcraft.fabric.client.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.mod.buildcraft.fabric.screen.EngineScreenHandler;

public class EngineScreen extends HandledScreen<EngineScreenHandler> {
    public EngineScreen(EngineScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 90;
    }
    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        // simple text UI
        ctx.drawText(this.textRenderer, this.title, this.x + 8, this.y + 8, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "v1: " + this.handler.getV1(), this.x + 8, this.y + 28, 0xA0A0A0, false);
        ctx.drawText(this.textRenderer, "v2: " + this.handler.getV2(), this.x + 8, this.y + 44, 0xA0A0A0, false);
    }
}