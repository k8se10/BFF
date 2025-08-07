#!/usr/bin/env python3
import os, json, argparse, shutil, sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "src" / "main" / "resources" / "assets" / "buildcraft"

def load_json(p):
    with open(p, "r", encoding="utf-8") as f:
        return json.load(f)

def save_json(p, obj):
    p.parent.mkdir(parents=True, exist_ok=True)
    with open(p, "w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2)

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--map", required=True, help="Path to asset_map.json")
    ap.add_argument("--src", required=True, help="Path to local BuildCraft repo assets/buildcraft")
    args = ap.parse_args()

    mapping = load_json(args.map)
    texmap = mapping.get("textures", {})
    src_assets = Path(args.src)
    if not (src_assets / "textures").exists():
        print("Expected textures/ under", src_assets, file=sys.stderr)
        sys.exit(1)

    # 1) Copy textures to our assets path with their original names
    for key, rel in texmap.items():
        src_file = src_assets / rel
        if not src_file.exists():
            print("[WARN] Missing in source:", src_file, file=sys.stderr)
            continue
        dst_file = ASSETS / rel
        dst_file.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(src_file, dst_file)

    # 2) Update our models to point at the new texture names (namespace buildcraft:)
    # We scan models/block recursively and rewrite "textures": {"all":"buildcraft:block/<name>"} to the closest mapped path.
    def rewrite_model_json(p: Path):
        try:
            obj = load_json(p)
        except Exception:
            return
        if "textures" not in obj: 
            return
        changed = False
        for k, v in list(obj["textures"].items()):
            if isinstance(v, str) and v.startswith("buildcraft:block/"):
                tail = v.replace("buildcraft:block/", "")
                # look up matching mapping target path by basename
                cand = [rel for (name, rel) in texmap.items() if name.endswith(tail)]
                if cand:
                    # convert textures path to "buildcraft:<rel-without-prefix-textures/>"
                    rel_path = cand[0]
                    rp = rel_path.replace("textures/","")
                    obj["textures"][k] = "buildcraft:" + rp.replace("\\\\","/")
                    changed = True
        if changed:
            save_json(p, obj)

    for p in (ASSETS / "models" / "block").rglob("*.json"):
        rewrite_model_json(p)
    for p in (ASSETS / "models" / "item").rglob("*.json"):
        rewrite_model_json(p)

    print("Asset map applied. Verify blockstates/models in-game.")

if __name__ == "__main__":
    main()
