package net.mod.buildcraft.fabric.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.mod.buildcraft.fabric.block.entity.GateEntity;
import net.mod.buildcraft.fabric.logic.GateLogic;

public class GateScreen extends GlassHandledScreen<ScreenHandler> {
    private final GateEntity gate;

    private static final String[] TRIGGERS = { "pipe_has_items", "always_on", "redstone_on" };
    private static final String[] ACTIONS = { "emit_redstone", "extract_items", "toggle_signal" };

    private int triggerIndex = 0;
    private int actionIndex = 0;

    public GateScreen(ScreenHandler handler, PlayerInventory inventory, Text title, GateEntity gate) {
        super(handler, inventory, title, "gate");
        this.gate = gate;

        for (int i = 0; i < TRIGGERS.length; i++) {
            if (TRIGGERS[i].equals(gate.getTrigger())) {
                triggerIndex = i;
                break;
            }
        }

        for (int i = 0; i < ACTIONS.length; i++) {
            if (ACTIONS[i].equals(gate.getAction())) {
                actionIndex = i;
                break;
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        addDrawableChild(ButtonWidget.builder(Text.of("Trigger: " + TRIGGERS[triggerIndex]), btn -> {
            triggerIndex = (triggerIndex + 1) % TRIGGERS.length;
            gate.setTrigger(TRIGGERS[triggerIndex]);
            btn.setMessage(Text.of("Trigger: " + TRIGGERS[triggerIndex]));
        }).position(x + 20, y + 30).size(140, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.of("Action: " + ACTIONS[actionIndex]), btn -> {
            actionIndex = (actionIndex + 1) % ACTIONS.length;
            gate.setAction(ACTIONS[actionIndex]);
            btn.setMessage(Text.of("Action: " + ACTIONS[actionIndex]));
        }).position(x + 20, y + 60).size(140, 20).build());
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        super.drawBackground(ctx, delta, mouseX, mouseY);
    }
}