#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-postgresql"

echo "Criando Postgresql ..."

deployment_name=$(kubectl apply -f "$script_dir"/postgresql-deployment.yaml --dry-run=client -o json |
  jq -r '.metadata.name')

kubectl get namespace "$namespace" || kubectl create namespace "$namespace"
kubectl apply -n "$namespace" -f "$script_dir"/postgresql-storage-class.yaml
kubectl apply -n "$namespace" -f "$script_dir"/postgresql-persistent-volume.yaml
kubectl apply -n "$namespace" -f "$script_dir"/postgresql-persistent-volume-claim.yaml
kubectl apply -n "$namespace" -f "$script_dir"/postgresql-secret.yaml
kubectl apply -n "$namespace" -f "$script_dir"/postgresql-deployment.yaml

if ! kubectl -n "$namespace" rollout status -f "$script_dir"/postgresql-deployment.yaml; then
  echo "Rollout falhou devido ao timeout. Realizando rollback..."
  kubectl -n "$namespace" rollout undo deployment "$deployment_name"
  exit 1
fi

sleep 10

kubectl apply -n "$namespace" -f "$script_dir"/postgresql-service.yaml

echo "Postgresql criado com sucesso"
echo "Porta PostgreSQL: $(kubectl get service postgresql -n "$namespace" -o jsonpath='{.spec.ports[0].nodePort}')"
