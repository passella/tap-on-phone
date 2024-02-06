#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-redis"

echo "Removendo Redis ..."

kubectl delete -n "$namespace" -f "$script_dir"/redis-service.yaml
kubectl delete -n "$namespace" -f "$script_dir"/redis-deployment.yaml

echo "redis removido com sucesso"
