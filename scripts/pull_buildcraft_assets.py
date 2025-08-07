#!/usr/bin/env python3
"""
Copy OG BuildCraft textures/models into this project.
Usage:
  python3 scripts/pull_buildcraft_assets.py --src /path/to/BuildCraft
It will copy:
  - assets/buildcraft/textures/block/*   -> this mod assets
  - assets/buildcraft/textures/item/*    -> this mod assets
  - assets/buildcraft/models/block/*     -> (optional) copy or map into our multipart scheme
  - assets/buildcraft/blockstates/*      -> (optional) review vs our custom multipart
This script does not download; it just copies from a local clone.
"""
import argparse, os, shutil, sys

def copytree(src, dst):
    os.makedirs(dst, exist_ok=True)
    for root, _, files in os.walk(src):
        for name in files:
            s = os.path.join(root, name)
            rel = os.path.relpath(s, src)
            d = os.path.join(dst, rel)
            os.makedirs(os.path.dirname(d), exist_ok=True)
            shutil.copy2(s, d)

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--src', required=True, help='Path to local BuildCraft repo (with assets/buildcraft)')
    args = ap.parse_args()
    src_assets = os.path.join(args.src, 'buildcraft_resources', 'assets', 'buildcraft')
    if not os.path.isdir(src_assets):
        print('Could not find buildcraft assets in', src_assets, file=sys.stderr); sys.exit(1)
    dst_assets = os.path.join('src','main','resources','assets','buildcraft')
    # Copy textures (block & item)
    for sub in ['textures/block','textures/item']:
        s = os.path.join(src_assets, sub)
        d = os.path.join(dst_assets, sub)
        if os.path.isdir(s): copytree(s, d)
    print('Copied textures. Now map model/texture names in blockstate JSONs if needed.')

if __name__ == '__main__':
    main()
