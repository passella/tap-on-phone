#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

echo "Removendo Traefik Ingress Controller ..."

kubectl delete -f "$script_dir"/ --wait

echo "Traefik Ingress Controller removido com sucesso"
