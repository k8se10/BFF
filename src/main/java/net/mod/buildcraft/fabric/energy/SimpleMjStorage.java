package net.mod.buildcraft.fabric.energy;

public class SimpleMjStorage implements MjStorage {
    private long stored;
    private final long capacity;

    public SimpleMjStorage(long capacity) {
        this.capacity = capacity;
    }

    @Override
    public long receiveMicroMJ(long amount) {
        if (amount <= 0) return 0;
        long can = capacity - stored;
        long accepted = Math.min(can, amount);
        stored += accepted;
        return accepted;
    }

    @Override
    public boolean canReceiveMJ() { return stored < capacity; }

    @Override
    public long extractMicroMJ(long max) {
        long taken = Math.min(max, stored);
        stored -= taken;
        return taken;
    }

    @Override
    public boolean canProvideMJ() { return stored > 0; }

    @Override
    public long getStoredMicroMJ() { return stored; }

    @Override
    public long getCapacityMicroMJ() { return capacity; }
}