package net.mod.buildcraft.fabric.screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class BuilderScreenHandler extends ScreenHandler {
    private final PropertyDelegate props;
    public BuilderScreenHandler(int id, PlayerInventory inv, PropertyDelegate props){
        super(net.mod.buildcraft.fabric.registry.BCUI.BUILDER_HANDLER, id);
        this.props = props;
        this.addProperties(props);
    }
    @Override public boolean onButtonClick(PlayerEntity player, int id){
        if (player.getWorld().isClient) return true;
        var be = player.getWorld().getBlockEntity(player.getBlockPos());
        if (be instanceof net.mod.buildcraft.fabric.block.entity.BuilderEntity b) b.toggleRunning();
        return true;
    }
    @Override public boolean canUse(PlayerEntity player){ return true; }
    public PropertyDelegate getProps(){ return props; }
}