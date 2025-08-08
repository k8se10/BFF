package net.mod.buildcraft.fabric.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class GlassHandledScreen<T extends ScreenHandler> extends HandledScreen<T> {
    private final Identifier backgroundTexture;

    protected GlassHandledScreen(T handler, PlayerInventory inventory, Text title, String textureName) {
        super(handler, inventory, title);
        this.backgroundTexture = new Identifier("buildcraft", "textures/gui/" + textureName + ".png");
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Modern glass-like translucent panel effect
        ctx.getMatrices().push();
        ctx.getMatrices().translate(0, 0, -10); // Push behind widgets

        // Iris-compatible semi-transparent backdrop (using blur if available)
        ctx.fillGradient(x - 4, y - 4, x + backgroundWidth + 4, y + backgroundHeight + 4, 0xC0101010, 0xC0181818);

        ctx.getMatrices().pop();

        // Fallback GUI background (OG BC texture)
        try {
            ctx.drawTexture(backgroundTexture, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        } catch (Exception ignored) {}
    }
}