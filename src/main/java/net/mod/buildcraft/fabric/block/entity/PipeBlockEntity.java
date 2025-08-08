package net.mod.buildcraft.fabric.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.mod.buildcraft.fabric.registry.BCContent;
import net.mod.buildcraft.fabric.transport.PipeAlgorithms;
import net.mod.buildcraft.fabric.transport.TravelingItem;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

public class PipeBlockEntity extends BlockEntity {
    private int clientAnimTick = 0;
    private final ArrayDeque<TravelingItem> items = new ArrayDeque<>();
    private final List<RenderItem> clientItems = new ArrayList<>();
    private int itemsPerTickCache = 1;
    private int cooldown = 0;
    private int syncCooldown = 0;

    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(BCContent.PIPE_BE, pos, state);
    }

    public static class RenderItem {
        public List<Direction> path = new ArrayList<>();
        public int segIndex = 0;
        public float segProgress = 0f;
        public ItemVariant variant;
        public float progress; // 0..1
        public Direction dir;

        public RenderItem(ItemVariant v, float p, Direction d) {
            this.variant = v;
            this.progress = p;
            this.dir = d;
        }
    }

    // Called every server tick by the block ticker.
    private List<Direction> findPipePath(ServerWorld sw, BlockPos start, BlockPos goal) {
        ArrayDeque<BlockPos> q = new ArrayDeque<>();
        HashMap<BlockPos, BlockPos> prev = new HashMap<>();
        HashSet<BlockPos> seen = new HashSet<>();
        q.add(start);
        seen.add(start);

        while (!q.isEmpty()) {
            BlockPos p = q.removeFirst();
            if (p.equals(goal)) break;
            for (Direction d : Direction.values()) {
                BlockPos n = p.offset(d);
                if (!seen.add(n)) continue;
                var be = sw.getBlockEntity(n);
                if (be instanceof PipeBlockEntity || n.equals(goal)) {
                    prev.put(n, p);
                    q.addLast(n);
                }
            }
        }

        if (!prev.containsKey(goal)) return List.of();

        // Convert to directions between nodes (face to next)
        List<Direction> dirs = new ArrayList<>();
        BlockPos cur = goal;
        while (!cur.equals(start)) {
            cur = prev.get(cur);
            for (Direction d : Direction.values()) {
                if (cur.offset(d).equals(prev.get(cur))) {
                    dirs.add(d);
                    break;
                }
            }
        }
        return dirs;
    }

    public void serverTick() {
        // Cache speed per block type
        if (world != null && world.getBlockState(pos).getBlock() instanceof net.mod.buildcraft.fabric.transport.SpeedTieredPipe stp) {
            itemsPerTickCache = Math.max(1, stp.getItemsPerTick());
        }
        if (!(world instanceof ServerWorld sw)) return;

        // Move a few packets per tick depending on pipe speed
        for (int moved = 0; moved < itemsPerTickCache; moved++) {
            TravelingItem ti = items.peekFirst();
            if (ti == null) break;

            // If we have a target, try to insert; if successful remove from queue
            int accepted = PipeAlgorithms.insertAll(sw, ti.target, Direction.DOWN, ti.variant, ti.amount);
            if (accepted >= ti.amount) {
                items.removeFirst();
            } else if (accepted > 0) {
                ti.amount -= accepted;
            } else {
                // couldn't insert; re-route in a few ticks
                if (cooldown-- <= 0) {
                    BlockPos newTarget = PipeAlgorithms.bfsNearestInsert(sw, pos, ti.variant);
                    if (newTarget != null) {
                        ti.target = newTarget;
                    }
                    cooldown = 10;
                }
            }
        }

        if (syncCooldown-- <= 0) {
            sync();
            syncCooldown = 5;
        }
    }

    /** Try to push an item into the pipe from another block. */
    public boolean tryAccept(ItemVariant variant, int amount, Direction from) {
        if (!(world instanceof ServerWorld sw)) return false;
        BlockPos target = PipeAlgorithms.bfsNearestInsert(sw, pos, variant);
        if (target == null) return false;
        items.addLast(new TravelingItem(variant, amount, target, from));
        addClientItem(variant, from.getOpposite());

        if (world instanceof ServerWorld) {
            List<Direction> dirs = findPipePath(sw, pos, target);
            if (!clientItems.isEmpty()) {
                RenderItem ri = clientItems.get(clientItems.size() - 1);
                ri.path.clear();
                ri.path.addAll(dirs);
                ri.segIndex = 0;
                ri.segProgress = 0f;
            }
        }
        sync();
        markDirty();
        return true;
    }

    /** Wooden pipes: pass in a direction to extract from that side if powered. */
    public void tryAutoExtract(Direction from) {
        if (!(world instanceof ServerWorld sw)) return;
        if (!world.isReceivingRedstonePower(pos)) return;

        ItemVariant[] holder = new ItemVariant[1];
        int extracted = PipeAlgorithms.extractSome(sw, pos.offset(from), from.getOpposite(), holder, 8);
        if (extracted > 0 && holder[0] != null) {
            tryAccept(holder[0], extracted, from);
        }
    }

    public void clientTick() {
        clientAnimTick++;
        for (var it : new ArrayList<>(clientItems)) {
            float speed = getVisualSpeed();
            it.segProgress += speed;
            if (it.segProgress >= 1f) {
                it.segProgress = 0f;
                it.segIndex++;
            }
            if (it.segIndex >= it.path.size()) {
                clientItems.remove(it);
            }
        }
    }

    private void addClientItem(ItemVariant v, Direction dir) {
        if (clientItems.size() < 8) {
            clientItems.add(new RenderItem(v, 0f, dir));
        }
    }

    private void sync() {
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = new NbtCompound();
        writeNbt(tag);
        return tag;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        // server -> client clientItems snapshot
        var list = new net.minecraft.nbt.NbtList();
        for (RenderItem ri : clientItems) {
            NbtCompound t = new NbtCompound();
            t.put("v", ri.variant.toNbt());
            t.putInt("idx", ri.segIndex);
            t.putFloat("prog", ri.segProgress);
            t.putIntArray("path", ri.path.stream().mapToInt(Direction::getId).toArray());
            list.add(t);
        }
        nbt.put("clientItems", list);

        var itemsList = new net.minecraft.nbt.NbtList();
        for (var ti : items) {
            NbtCompound t = new NbtCompound();
            t.put("v", ti.variant.toNbt());
            t.putInt("amt", ti.amount);
            t.putInt("lx", ti.target.getX());
            t.putInt("ly", ti.target.getY());
            t.putInt("lz", ti.target.getZ());
            t.putInt("from", ti.lastMove.getId());
            itemsList.add(t);
        }
        nbt.put("items", itemsList);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        clientItems.clear();
        if (nbt.contains("clientItems", NbtElement.LIST_TYPE)) {
            var list = nbt.getList("clientItems", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                var t = list.getCompound(i);
                ItemVariant v = ItemVariant.fromNbt(t.getCompound("v"));
                RenderItem ri = new RenderItem(v, 0f, Direction.NORTH);
                ri.segIndex = t.getInt("idx");
                ri.segProgress = t.getFloat("prog");
                int[] arr = t.getIntArray("path");
                for (int id : arr) {
                    ri.path.add(Direction.byId(id));
                }
                clientItems.add(ri);
            }
        }

        items.clear();
        if (nbt.contains("items", NbtElement.LIST_TYPE)) {
            var list = nbt.getList("items", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                var t = list.getCompound(i);
                ItemVariant v = ItemVariant.fromNbt(t.getCompound("v"));
                int amt = t.getInt("amt");
                BlockPos target = new BlockPos(t.getInt("lx"), t.getInt("ly"), t.getInt("lz"));
                Direction from = Direction.byId(t.getInt("from"));
                items.add(new TravelingItem(v, amt, target, from));
            }
        }
    }

    public List<RenderItem> getClientItems() {
        return clientItems;
    }

    private float getVisualSpeed() {
        float base = 0.05f;
        if (world != null) {
            var bs = world.getBlockState(pos);
            var b = bs.getBlock();
            if (b instanceof net.mod.buildcraft.fabric.transport.SpeedTieredPipe st) {
                int ipt = Math.max(1, st.getItemsPerTick());
                return base + 0.01f * ipt; // gold (8) -> 0.13f, etc.
            }
        }
        return base;
    }
}