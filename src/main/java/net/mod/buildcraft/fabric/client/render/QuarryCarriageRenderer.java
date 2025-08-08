package net.mod.buildcraft.fabric.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.mod.buildcraft.fabric.block.entity.QuarryEntity;

public class QuarryCarriageRenderer implements BlockEntityRenderer<QuarryEntity> {
    public QuarryCarriageRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(QuarryEntity quarry, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay) {
        if (!quarry.isActive()) return;

        double dx = quarry.getDrillX() - quarry.getPos().getX();
        double dy = quarry.getDrillY() - quarry.getPos().getY();
        double dz = quarry.getDrillZ() - quarry.getPos().getZ();

        matrices.push();
        float bounce = (float)Math.sin((quarry.getWorld().getTime() + tickDelta) * 0.3) * 0.1f;
        matrices.translate(dx + 0.5, dy + 0.5 + bounce, dz + 0.5);
        matrices.scale(0.2f, 1.0f, 0.2f);

        // Placeholder render logic (e.g., cube for piston)
        // You would normally bind a texture + draw a model here
        matrices.pop();
    }
}