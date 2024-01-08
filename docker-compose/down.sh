#!/bin/bash
set -e
script_dir=$(dirname $(realpath "$0"))

echo "Removendo solução..."
docker compose --env-file "$script_dir"/ambiente.env -f "$script_dir"/docker-compose.yml -p tap-on-phone down --remove-orphans
