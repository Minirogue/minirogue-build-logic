#!/usr/bin/env bash
set -euo pipefail

# Release tag script: checkout main, pull, tag with user-provided label, push tag

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <tag-label>"
  echo "  tag-label: e.g. 1.0.0 or release/2024-02"
  exit 1
fi

TAG_LABEL="$1"

echo "Checking out main..."
git checkout main

echo "Pulling latest..."
git pull

echo "Creating tag: $TAG_LABEL"
git tag "$TAG_LABEL"

echo "Pushing tag $TAG_LABEL..."
git push origin "$TAG_LABEL"

echo "Done. Tag $TAG_LABEL has been created and pushed."
