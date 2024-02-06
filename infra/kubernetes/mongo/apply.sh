#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-mongodb"

echo "Criando MongoDB ..."

deployment_name=$(kubectl apply -f "$script_dir"/mongodb-deployment.yaml --dry-run=client -o json |
  jq -r '.metadata.name')

kubectl get namespace "$namespace" || kubectl create namespace "$namespace"
kubectl apply -n "$namespace" -f "$script_dir"/mongodb-storage-class.yaml
kubectl apply -n "$namespace" -f "$script_dir"/mongodb-persistent-volume.yaml
kubectl apply -n "$namespace" -f "$script_dir"/mongodb-persistent-volume-claim.yaml
kubectl apply -n "$namespace" -f "$script_dir"/mongodb-secret.yaml
kubectl apply -n "$namespace" -f "$script_dir"/mongodb-deployment.yaml

if ! kubectl -n "$namespace" rollout status -f "$script_dir"/mongodb-deployment.yaml; then
  echo "Rollout falhou devido ao timeout. Realizando rollback..."
  kubectl -n "$namespace" rollout undo deployment "$deployment_name"
  exit 1
fi

sleep 10

kubectl apply -n "$namespace" -f "$script_dir"/mongodb-service.yaml

echo "MongoDB criado com sucesso"
echo "Porta MongoDB: $(kubectl get service mongodb -n "$namespace" -o jsonpath='{.spec.ports[0].nodePort}')"
