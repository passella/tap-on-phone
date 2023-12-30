#!/bin/bash
set -e
echo "Removendo solução..."
docker compose --env-file ambiente.env -f docker-compose.yml -p tap-on-phone down --remove-orphans
