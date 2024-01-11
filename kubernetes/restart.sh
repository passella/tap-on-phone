#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Reiniciando solução ..."
kubectl rollout restart -f "$script_dir"/../services/cadastro/kubernetes/cadastro-deployment.yaml
kubectl rollout restart -f "$script_dir"/database/postgresql/postgresql-deployment.yaml
echo "Solução pronta para uso"
