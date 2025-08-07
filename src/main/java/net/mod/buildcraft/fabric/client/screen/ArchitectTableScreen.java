package net.mod.buildcraft.fabric.client.screen;
import net.minecraft.client.gui.DrawContext; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.text.Text;
import net.mod.buildcraft.fabric.screen.ArchitectTableScreenHandler;
public class ArchitectTableScreen extends GlassHandledScreen<ArchitectTableScreenHandler> {
    public ArchitectTableScreen(ArchitectTableScreenHandler h, PlayerInventory inv, Text t){ super(h, inv, t, "architect_table"); this.backgroundWidth=176; this.backgroundHeight=120; }
    @Override protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY){ super.drawBackground(ctx, delta, mouseX, mouseY);
        int x=(this.width-this.backgroundWidth)/2, y=(this.height-this.backgroundHeight)/2;
        ctx.drawText(textRenderer, "Use with Blueprint in hand to capture area", x+10, y+20, 0xFFFFFF, true);
    }
}