#!/usr/bin/env python3
"""
Generate placeholder WAV loops for BuildCraft sounds, and (optionally) convert to OGG with ffmpeg.
- engine_chug.wav            mechanical chug loop
- engine_steam_loop.wav      steam hiss + ticking
- engine_combustion_loop.wav low combustion hum
- refinery_hum.wav           industrial hum
- quarry_loop.wav            gear grind loop
- laser_zap.wav              short zap
- pump_loop.wav              pump thrum

Usage:
  python tools/generate_placeholder_sounds.py
  # Optional: convert to OGG if ffmpeg is installed
  python tools/generate_placeholder_sounds.py --ogg
"""
import argparse, os, math, wave, struct, shutil, subprocess
from pathlib import Path
RATE = 44100

def write_wav(path, samples):
    Path(path).parent.mkdir(parents=True, exist_ok=True)
    with wave.open(path, "w") as w:
        w.setnchannels(1)
        w.setsampwidth(2)
        w.setframerate(RATE)
        # clamp and write
        frames = b''.join(struct.pack('<h', max(-32768, min(32767, int(s*32767)))) for s in samples)
        w.writeframes(frames)

def tone(freq, length, vol=0.2):
    n = int(RATE*length)
    return [vol*math.sin(2*math.pi*freq*t/RATE) for t in range(n)]

def noise(length, vol=0.1, seed=1):
    import random
    r = random.Random(seed)
    n = int(RATE*length)
    return [vol*(r.random()*2-1) for _ in range(n)]

def env(samples, attack=0.05, release=0.05):
    n = len(samples)
    a = int(RATE*attack)
    r = int(RATE*release)
    out = []
    for i, s in enumerate(samples):
        if i < a:
            g = i/a
        elif i > n-r:
            g = max(0.0, (n-i)/r)
        else:
            g = 1.0
        out.append(s*g)
    return out

def mix(*tracks):
    n = max(len(t) for t in tracks)
    out = [0.0]*n
    for t in tracks:
        for i, s in enumerate(t):
            out[i] += s
    # soft clip
    m = max(1.0, max(abs(x) for x in out))
    return [x/m*0.9 for x in out]

def loopify(samples, cross=0.05):
    k = int(RATE*cross)
    if k*2 >= len(samples): return samples
    head = samples[:k]
    tail = samples[-k:]
    # crossfade
    blended = [(tail[i]*(i/k) + head[i]*(1 - i/k)) for i in range(k)]
    return samples[:-k] + blended

def save(name, samples, ogg):
    wav_path = Path(f"src/main/resources/assets/buildcraft/sounds/{name}.wav")
    write_wav(wav_path, samples)
    if ogg:
        ogg_path = wav_path.with_suffix(".ogg")
        # Use ffmpeg if available
        cmd = ["ffmpeg", "-y", "-loglevel", "error", "-i", str(wav_path), "-acodec", "libvorbis", "-qscale:a", "4", str(ogg_path)]
        try:
            subprocess.check_call(cmd)
            wav_path.unlink(missing_ok=True)
        except Exception:
            print("ffmpeg not found or failed; WAV left in place.")

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--ogg", action="store_true", help="Convert to OGG with ffmpeg if available")
    args = ap.parse_args()

    # 3–5s seamless loops
    chug = loopify(mix(tone(5, 4, 0.3), tone(40,4,0.05)))
    steam = loopify(mix(noise(4,0.08), tone(3,4,0.1), tone(1200,4,0.01)))
    combust = loopify(mix(tone(60,4,0.12), tone(120,4,0.05)))
    refinery = loopify(mix(tone(50,4,0.08), noise(4,0.03)))
    quarry = loopify(mix(tone(8,4,0.15), tone(100,4,0.05)))
    # 0.2s zap
    zap = env(mix(tone(1800,0.2,0.6), tone(5000,0.2,0.3)), 0.005, 0.05)
    pump = loopify(mix(tone(2,4,0.25), tone(70,4,0.05)))

    save("engine_chug", chug, args.ogg)
    save("engine_steam_loop", steam, args.ogg)
    save("engine_combustion_loop", combust, args.ogg)
    save("refinery_hum", refinery, args.ogg)
    save("quarry_loop", quarry, args.ogg)
    save("laser_zap", zap, args.ogg)
    save("pump_loop", pump, args.ogg)
    print("Generated placeholder audio in assets/buildcraft/sounds/. Use --ogg to convert to OGG.")
if __name__ == "__main__":
    main()
