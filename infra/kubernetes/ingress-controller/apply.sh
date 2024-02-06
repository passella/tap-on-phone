#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Criando Traefik Ingress Controller ..."

kubectl apply -f "$script_dir"/

echo "Traefik Ingress Controller criado com sucesso"
