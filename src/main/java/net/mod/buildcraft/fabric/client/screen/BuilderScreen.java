package net.mod.buildcraft.fabric.client.screen;
import net.minecraft.client.gui.DrawContext; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.text.Text;
import net.mod.buildcraft.fabric.screen.BuilderScreenHandler;
public class BuilderScreen extends GlassHandledScreen<BuilderScreenHandler> {
    public BuilderScreen(BuilderScreenHandler h, PlayerInventory inv, Text t){ super(h, inv, t, "builder"); this.backgroundWidth=176; this.backgroundHeight=120; }
    @Override protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY){ super.drawBackground(ctx, delta, mouseX, mouseY);
        int x=(this.width-this.backgroundWidth)/2, y=(this.height-this.backgroundHeight)/2;
        // Placeholder bars for power + progress
        var be = net.minecraft.client.MinecraftClient.getInstance().world.getBlockEntity(this.handler.getSyncId()); // placeholder; proper sync would use packets
        ctx.drawText(textRenderer, "Power & Progress", x+10, y+20, 0xFFFFFF, true);
    }
}