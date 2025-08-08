package net.mod.buildcraft.fabric.energy;

interface MjReceiver {
    /** Try to accept up to 'amount' microMJ, return accepted. 1 MJ = 1_000_000 microMJ. */
    long receiveMicroMJ(long amount);
    boolean canReceiveMJ();
}

interface MjProvider {
    long extractMicroMJ(long max);
    boolean canProvideMJ();
}

interface MjStorage extends MjReceiver, MjProvider {
    long getStoredMicroMJ();
    long getCapacityMicroMJ();
}