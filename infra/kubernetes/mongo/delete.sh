#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-mongodb"

echo "Removendo MongoDB ..."

kubectl delete -n "$namespace" -f "$script_dir"/mongodb-service.yaml
kubectl delete -n "$namespace" -f "$script_dir"/mongodb-deployment.yaml
kubectl delete -n "$namespace" -f "$script_dir"/mongodb-secret.yaml
kubectl delete -n "$namespace" -f "$script_dir"/mongodb-persistent-volume-claim.yaml
kubectl delete -n "$namespace" -f "$script_dir"/mongodb-persistent-volume.yaml
kubectl delete -n "$namespace" -f "$script_dir"/mongodb-storage-class.yaml

echo "MongoDB removido com sucesso"
