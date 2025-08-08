package net.mod.buildcraft.fabric.logic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mod.buildcraft.fabric.block.entity.GateEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public class GateLogic {
    private static final Map<String, BiFunction<World, BlockPos, Boolean>> triggers = new HashMap<>();
    private static final Map<String, Integer> tiers = new HashMap<>();
    private static final Map<String, BiConsumer<World, BlockPos>> actions = new HashMap<>();
    private static final Map<String, Integer> actionTier = new HashMap<>();

    static {
        actions.put("emit_redstone", (world, pos) -> {
            world.updateNeighbors(pos, world.getBlockState(pos).getBlock());
        });
        actions.put("open_pipe", (world, pos) -> {
            var be = world.getBlockEntity(pos.down());
            try { var m = be.getClass().getMethod("setExtractionEnabled", boolean.class); m.invoke(be, true); } catch (Exception ignored){}
        });
        actions.put("extract_items", (world, pos) -> {
            var be = world.getBlockEntity(pos.down());
            try { var m = be.getClass().getMethod("pulseExtract"); m.invoke(be); } catch (Exception ignored){}
        });
        actions.put("emit_wire_red", (world, pos) -> {});
        actions.put("emit_wire_blue", (world, pos) -> {});
        actions.put("emit_wire_green", (world, pos) -> {});
        actions.put("emit_wire_yellow", (world, pos) -> {});

        actionTier.put("emit_redstone",0);
        actionTier.put("open_pipe",1);
        actionTier.put("extract_items",2);
        actionTier.put("emit_wire_red",3);
        actionTier.put("emit_wire_blue",3);
        actionTier.put("emit_wire_green",3);
        actionTier.put("emit_wire_yellow",3);

        // Triggers (tiered)
        triggers.put("pipe_has_items", (world, pos) -> {
            var be = world.getBlockEntity(pos.down());
            try { var m = be.getClass().getMethod("hasAnyItems"); return (boolean)m.invoke(be); } catch (Exception e){ return false; }
        });
        triggers.put("redstone_on", (world, pos) -> world.getReceivedRedstonePower(pos) > 0);
        triggers.put("tank_full", (world, pos) -> {
            var be = world.getBlockEntity(pos.down());
            try { var m = be.getClass().getMethod("isTankFull"); return (boolean)m.invoke(be); } catch (Exception e){ return false; }
        });
        triggers.put("tank_empty", (world, pos) -> {
            var be = world.getBlockEntity(pos.down());
            try { var m = be.getClass().getMethod("isTankEmpty"); return (boolean)m.invoke(be); } catch (Exception e){ return false; }
        });
        triggers.put("mj_low", (world, pos) -> {
            var be = world.getBlockEntity(pos.down());
            try { var m = be.getClass().getMethod("getStoredMicroMJ"); long v=(long)m.invoke(be); return v < 500_000; } catch (Exception e){ return false; }
        });
        triggers.put("machine_running", (world, pos) -> {
            var be = world.getBlockEntity(pos.down());
            try { var m = be.getClass().getMethod("isActiveClient"); return (boolean)m.invoke(be); } catch (Exception e){ return false; }
        });

        // Tiers (0 basic, 1 iron, 2 gold, 3 diamond, 4 autarchic)
        tiers.put("pipe_has_items", 0);
        tiers.put("redstone_on", 0);
        tiers.put("tank_full", 1);
        tiers.put("tank_empty", 1);
        tiers.put("mj_low", 2);
        tiers.put("machine_running", 2);

        triggers.put("pipe_has_items", (world, pos) -> {
            BlockPos below = pos.down();
            return !world.getBlockState(below).isAir();
        });
        triggers.put("always_on", (world, pos) -> true);
        triggers.put("redstone_on", (world, pos) ->
            world.getReceivedRedstonePower(pos) > 0
        );

        actions.put("emit_redstone", (world, pos) -> {
            for (Direction dir : Direction.values()) {
                BlockPos side = pos.offset(dir);
                if (world.getBlockState(side).isOf(Blocks.REDSTONE_WIRE)) {
                    world.setBlockState(side, Blocks.REDSTONE_WIRE.getDefaultState()
                        .with(RedstoneWireBlock.POWER, 15));
                }
            }
        });

        actions.put("extract_items", (world, pos) -> {
            // placeholder – call pipe entity extraction next to this gate
        });

        actions.put("toggle_signal", (world, pos) -> {
            // placeholder – toggle pipe wire signal
        });
    }

    public static boolean evaluate(GateEntity gate, World world, BlockPos pos, String triggerId) {
        return triggers.getOrDefault(triggerId, (w, p) -> false).apply(world, pos);
    }

    public static void execute(GateEntity gate, World world, BlockPos pos, String actionId, boolean condition) {
        if (condition) actions.getOrDefault(actionId, (w, p) -> {}).accept(world, pos);
    }
}