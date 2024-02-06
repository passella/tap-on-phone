#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-redis"

echo "Criando redis ..."

deployment_name=$(kubectl apply -f "$script_dir"/redis-deployment.yaml --dry-run=client -o json |
  jq -r '.metadata.name')

kubectl get namespace "$namespace" || kubectl create namespace "$namespace"
kubectl apply -n "$namespace" -f "$script_dir"/redis-deployment.yaml

if ! kubectl -n "$namespace" rollout status -f "$script_dir"/redis-deployment.yaml; then
  echo "Rollout falhou devido ao timeout. Realizando rollback..."
  kubectl -n "$namespace" rollout undo deployment "$deployment_name"
  exit 1
fi

sleep 10

kubectl apply -n "$namespace" -f "$script_dir"/redis-service.yaml

echo "Redis criado com sucesso"
echo "Porta redis: $(kubectl get service redis -n "$namespace" -o jsonpath='{.spec.ports[0].nodePort}')"
