package net.mod.buildcraft.fabric.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueprintItem extends Item {
    public BlueprintItem(Settings settings){ super(settings.maxCount(1)); }

    public static boolean hasSchematic(ItemStack stack){
        return stack.hasNbt() && stack.getNbt().contains("blueprint");
    }

    public static NbtCompound getData(ItemStack stack){
        return stack.getOrCreateNbt().getCompound("blueprint");
    }

    public static void setData(ItemStack stack, NbtCompound data){
        stack.getOrCreateNbt().put("blueprint", data);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (hasSchematic(stack)){
            var bp = getData(stack);
            int sx = bp.getInt("sx"), sy = bp.getInt("sy"), sz = bp.getInt("sz");
            tooltip.add(Text.literal("Size: "+sx+"×"+sy+"×"+sz));
            tooltip.add(Text.literal("Blocks: "+bp.getInt("count")));
        } else {
            tooltip.add(Text.literal("Empty blueprint. Use at Architect Table."));
        }
    }
}
