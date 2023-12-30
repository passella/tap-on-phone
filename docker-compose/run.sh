#!/bin/bash
set -e
echo "Subindo solução..."
docker compose --env-file ambiente.env -f docker-compose.yml -p tap-on-phone up --remove-orphans -d --build
