package net.mod.buildcraft.fabric.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.mod.buildcraft.fabric.block.entity.QuarryEntity;
import net.mod.buildcraft.fabric.config.BCConfig;

public class QuarryBER implements BlockEntityRenderer<QuarryEntity> {
    public QuarryBER(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(QuarryEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vcp, int light, int overlay) {
        if (be.getWorld()==null) return;
        // Shader-safe: draw translucent quads for posts/rails; no lines unless debug
        if (!BCConfig.SHADER_COMPAT && !BCConfig.DEBUG_WIREFRAMES) return;
        int minX = be.getMinX() - be.getPos().getX();
        int maxX = be.getMaxX() - be.getPos().getX();
        int minZ = be.getMinZ() - be.getPos().getZ();
        int maxZ = be.getMaxZ() - be.getPos().getZ();

        float height = 3.0f;
        float thick = 0.0625f * 2; // 2px-ish beams
        int a = 140;
        int r = 255, g = 255, b = 0; // yellow frame

        VertexConsumer vc = vcp.getBuffer(RenderLayer.getTranslucent());
        matrices.push();
        // posts
        drawBox(matrices, vc, minX+0.5f - thick/2, 0, minZ+0.5f - thick/2, thick, height, thick, r,g,b,a, light, overlay);
        drawBox(matrices, vc, maxX+0.5f - thick/2, 0, minZ+0.5f - thick/2, thick, height, thick, r,g,b,a, light, overlay);
        drawBox(matrices, vc, minX+0.5f - thick/2, 0, maxZ+0.5f - thick/2, thick, height, thick, r,g,b,a, light, overlay);
        drawBox(matrices, vc, maxX+0.5f - thick/2, 0, maxZ+0.5f - thick/2, thick, height, thick, r,g,b,a, light, overlay);
        // rails (top edges)
        drawBox(matrices, vc, minX+0.5f, height-0.05f, minZ+0.5f - thick/2, (maxX-minX), 0.1f, thick, r,g,b,a, light, overlay);
        drawBox(matrices, vc, minX+0.5f - thick/2, height-0.05f, minZ+0.5f, thick, 0.1f, (maxZ-minZ), r,g,b,a, light, overlay);
        matrices.pop();
    }

    private void drawBox(MatrixStack matrices, VertexConsumer vc, float x, float y, float z, float dx, float dy, float dz, int r,int g,int b,int a, int light, int overlay){
        var m = matrices.peek().getPositionMatrix();
        // 6 faces of a box
        // Bottom
        vc.vertex(m, x, y, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,-1,0).next();
        vc.vertex(m, x+dx, y, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,-1,0).next();
        vc.vertex(m, x+dx, y, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,-1,0).next();
        vc.vertex(m, x, y, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,-1,0).next();
        // Top
        vc.vertex(m, x, y+dy, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,1,0).next();
        vc.vertex(m, x+dx, y+dy, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,1,0).next();
        vc.vertex(m, x+dx, y+dy, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,1,0).next();
        vc.vertex(m, x, y+dy, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,1,0).next();
        // Sides
        // +X
        vc.vertex(m, x+dx, y, z).color(r,g,b,a).overlay(overlay).light(light).normal(1,0,0).next();
        vc.vertex(m, x+dx, y+dy, z).color(r,g,b,a).overlay(overlay).light(light).normal(1,0,0).next();
        vc.vertex(m, x+dx, y+dy, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(1,0,0).next();
        vc.vertex(m, x+dx, y, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(1,0,0).next();
        // -X
        vc.vertex(m, x, y, z).color(r,g,b,a).overlay(overlay).light(light).normal(-1,0,0).next();
        vc.vertex(m, x, y+dy, z).color(r,g,b,a).overlay(overlay).light(light).normal(-1,0,0).next();
        vc.vertex(m, x, y+dy, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(-1,0,0).next();
        vc.vertex(m, x, y, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(-1,0,0).next();
        // +Z
        vc.vertex(m, x, y, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,1).next();
        vc.vertex(m, x, y+dy, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,1).next();
        vc.vertex(m, x+dx, y+dy, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,1).next();
        vc.vertex(m, x+dx, y, z+dz).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,1).next();
        // -Z
        vc.vertex(m, x, y, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,-1).next();
        vc.vertex(m, x, y+dy, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,-1).next();
        vc.vertex(m, x+dx, y+dy, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,-1).next();
        vc.vertex(m, x+dx, y, z).color(r,g,b,a).overlay(overlay).light(light).normal(0,0,-1).next();
    }
}
