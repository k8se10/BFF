package net.mod.buildcraft.fabric.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.mod.buildcraft.fabric.block.entity.LaserEntity;
import net.mod.buildcraft.fabric.config.BCConfig;

public class LaserBER implements BlockEntityRenderer<LaserEntity> {
    public LaserBER(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(LaserEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vcp, int light, int overlay) {
        if (be.getWorld()==null) return;
        if (BCConfig.SHADER_COMPAT) {
            // Shader compat: draw a translucent quad beam (no line layers)
            BlockPos t = be.getTargetPos();
            if (t == null) return;
            double dx = (t.getX() + 0.5) - (be.getPos().getX() + 0.5);
            double dy = (t.getY() + 0.6) - (be.getPos().getY() + 0.6);
            double dz = (t.getZ() + 0.5) - (be.getPos().getZ() + 0.5);
            float len = (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
            if (len <= 0.001f) return;

            float yaw = (float)Math.atan2(dz, dx);
            float pitch = (float)Math.asin(dy/len);

            float pulse = 0.6f + 0.4f*(float)Math.sin(((be.getWorld().getTime()+tickDelta)%20)/20f * (float)(2*Math.PI));
            VertexConsumer vc = vcp.getBuffer(RenderLayer.getTranslucent());

            matrices.push();
            matrices.translate(0.5, 0.6, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-yaw));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotation(pitch));
            float half = 0.03f;
            // draw 2 crossed quads to fake a round beam
            for (int i=0;i<2;i++){
                matrices.push();
                matrices.multiply(RotationAxis.POSITIVE_X.rotation((float)(i*Math.PI/2)));
                var m = matrices.peek().getPositionMatrix();
                int r=255,g=0,b=0,a=(int)(180*pulse);
                vc.vertex(m, 0, -half, 0).color(r,g,b,a).texture(0,0).overlay(overlay).light(light).normal(0,0,1).next();
                vc.vertex(m, len, -half, 0).color(r,g,b,a).texture(1,0).overlay(overlay).light(light).normal(0,0,1).next();
                vc.vertex(m, len,  half, 0).color(r,g,b,a).texture(1,1).overlay(overlay).light(light).normal(0,0,1).next();
                vc.vertex(m, 0,  half, 0).color(r,g,b,a).texture(0,1).overlay(overlay).light(light).normal(0,0,1).next();
                matrices.pop();
            }
            matrices.pop();
        }
    }
}
