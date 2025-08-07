package net.mod.buildcraft.fabric.client.screen;
import net.minecraft.client.gui.DrawContext; import net.minecraft.client.gui.screen.ingame.HandledScreen; import net.minecraft.entity.player.PlayerInventory; import net.minecraft.text.Text;
public class CombustionEngineScreen extends GlassHandledScreen<net.mod.buildcraft.fabric.screen.CombustionEngineScreenHandler> {
    public CombustionEngineScreen(net.mod.buildcraft.fabric.screen.CombustionEngineScreenHandler h, PlayerInventory inv, Text t){ super(handler, inventory, title, "combustionengine"); this.backgroundWidth=176; this.backgroundHeight=90; }
    @Override protected void drawBackground(DrawContext ctx,float delta,int mouseX,int mouseY){ int x=(this.width-this.backgroundWidth)/2; int y=(this.height-this.backgroundHeight)/2; ctx.drawText(this.textRenderer,this.title,x+8,y+8,0xFFFFFF,false); }
}
