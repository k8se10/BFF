#!/usr/bin/env bash
set -euo pipefail
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Gradle is not installed. Install Gradle 8.7 (or use \".github/workflows/build.yml\" CI) or generate wrapper via 'gradle wrapper'." >&2
  exit 1
fi
