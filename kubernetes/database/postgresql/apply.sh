#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-postgresql"

echo "Criando Postgresql ..."

kubectl get namespace "$namespace" || kubectl create namespace "$namespace"
kubectl apply -n "$namespace" -f "$script_dir"/ --wait

echo "Postgresql criado com sucesso"
echo "Porta PostgreSQL: $(kubectl get service postgresql -n "$namespace" -o jsonpath='{.spec.ports[0].nodePort}')"
