package net.mod.buildcraft.fabric.client.screen;
import net.minecraft.client.gui.DrawContext; import net.minecraft.client.gui.screen.ingame.HandledScreen; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.text.Text;
public class RefineryScreen extends GlassHandledScreen<net.mod.buildcraft.fabric.screen.RefineryScreenHandler> {
    public RefineryScreen(net.mod.buildcraft.fabric.screen.RefineryScreenHandler h, PlayerInventory inv, Text t){ super(handler, inventory, title, "refinery"); this.backgroundWidth=176; this.backgroundHeight=90; }
    @Override protected void drawBackground(DrawContext ctx,float delta,int mouseX,int mouseY){ int x=(this.width-this.backgroundWidth)/2; int y=(this.height-this.backgroundHeight)/2; ctx.drawText(this.textRenderer,this.title,x+8,y+8,0xFFFFFF,false); }
}


    private void drawFluidTank(DrawContext ctx, int x, int y, long amount, long max, int color) {
        if (amount <= 0 || max <= 0) return;
        int height = (int)(46 * Math.min(1.0, (double)amount / max));
        int drawY = y + 46 - height;
        ctx.fill(x, drawY, x + 10, y + 46, color);
    }