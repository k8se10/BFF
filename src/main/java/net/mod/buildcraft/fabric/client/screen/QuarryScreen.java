package net.mod.buildcraft.fabric.client.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.mod.buildcraft.fabric.screen.QuarryScreenHandler;

public class QuarryScreen extends GlassHandledScreen<QuarryScreenHandler> {
    public QuarryScreen(QuarryScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inventory, title, "quarry");
        this.backgroundWidth = 176;
        this.backgroundHeight = 90;
    }
    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        int x=(this.width-this.backgroundWidth)/2; int y=(this.height-this.backgroundHeight)/2;
        var tex = net.mod.buildcraft.fabric.client.util.Assets.gui("quarry.png");
        try{ ctx.drawTexture(tex, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight); } catch(Exception ignored){}
        // simple text UI
        ctx.drawText(this.textRenderer, this.title, this.x + 8, this.y + 8, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "v1: " + this.handler.getV1(), this.x + 8, this.y + 28, 0xA0A0A0, false);
        ctx.drawText(this.textRenderer, "v2: " + this.handler.getV2(), this.x + 8, this.y + 44, 0xA0A0A0, false);
    }
}
