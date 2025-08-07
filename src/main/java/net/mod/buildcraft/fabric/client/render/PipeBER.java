package net.mod.buildcraft.fabric.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.block.entity.PipeBlockEntity;

public class PipeBER implements BlockEntityRenderer<PipeBlockEntity> {
    public PipeBER(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(PipeBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vcp, int light, int overlay) {
        if (be.getWorld() == null) return;
        var items = be.getClientItems();
        for (var ri : items) {
            if (ri.path.isEmpty()) continue;
            int idx = Math.min(ri.segIndex, ri.path.size()-1);
            var dir = ri.path.get(idx);
            float p = ri.segProgress;
            matrices.push();
            // start at face and move to center based on progress
            double ox=0, oy=0, oz=0;
            double dist = 0.45 * (1.0 - p);
            switch(dir){
                case NORTH -> oz = -dist;
                case SOUTH -> oz = dist;
                case EAST  -> ox = dist;
                case WEST  -> ox = -dist;
                case UP    -> oy = dist;
                case DOWN  -> oy = -dist;
            }
            matrices.translate(0.5 + ox, 0.5 + oy, 0.5 + oz);
            matrices.scale(0.35f, 0.35f, 0.35f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(ri.variant.toStack(), net.minecraft.client.render.model.json.ModelTransformationMode.GROUND, light, overlay, matrices, vcp, be.getWorld(), 0);
            matrices.pop();
        }
    }
}
