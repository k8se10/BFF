package net.mod.buildcraft.fabric.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.mod.buildcraft.fabric.block.entity.RedstoneEngineEntity;

public class EngineBER implements BlockEntityRenderer<RedstoneEngineEntity> {
    public EngineBER(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(RedstoneEngineEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vcp, int light, int overlay) {
        if (be.getWorld() == null) return;
        float t = (be.getWorld().getTime()+tickDelta)%20/20f;
        float stroke = (float)(Math.sin(t*2*Math.PI)*0.2f);
        VertexConsumer vc = vcp.getBuffer(RenderLayer.getSolid());
        matrices.push();
        matrices.translate(0.5, 0.5+stroke, 0.5);
        matrices.scale(0.3f, 0.3f, 0.3f);
        var m = matrices.peek().getPositionMatrix();
        // simple cube piston head
        for (float x=-0.5f; x<=0.5f; x+=1.0f){
            for (float y=-0.5f; y<=0.5f; y+=1.0f){
                for (float z=-0.5f; z<=0.5f; z+=1.0f){
                    vc.vertex(m, x,y,z).color(255,0,0,200).normal(0,1,0).next();
                }
            }
        }
        matrices.pop();
    }
}
