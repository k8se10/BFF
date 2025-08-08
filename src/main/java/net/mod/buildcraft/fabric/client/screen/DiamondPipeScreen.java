package net.mod.buildcraft.fabric.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.mod.buildcraft.fabric.screen.DiamondPipeScreenHandler;

public class DiamondPipeScreen extends GlassHandledScreen<DiamondPipeScreenHandler> {
    public DiamondPipeScreen(DiamondPipeScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inventory, title, "diamondpipe");
        this.backgroundWidth = 176;
        this.backgroundHeight = 18 + 6*18 + 14 + 76;
    }

    @Override private static final int[] rowColors = {0x66AAAAAA,0x668888FF,0x66FF8888,0x6688FF88,0x66FFFF88,0x66999999};
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        int x=(this.width-this.backgroundWidth)/2; int y=(this.height-this.backgroundHeight)/2;
        var tex = net.mod.buildcraft.fabric.client.util.Assets.gui("diamond_pipe.png");
        try{ ctx.drawTexture(tex, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight); } catch(Exception ignored){}
        int x = (this.width - this.backgroundWidth)/2;
        int y = (this.height - this.backgroundHeight)/2;
        // simple frame + side labels
        ctx.drawText(this.textRenderer, this.title, x+8, y+4, 0xFFFFFF, false);
        String[] sides = {"Down","Up","North","South","West","East"};
        for (int row=0; row<6; row++) {
            ctx.drawText(this.textRenderer, sides[row], x+8+3*18+8, y+18 + row*18 + 6, 0xA0A0A0, false);
        }
    }
}