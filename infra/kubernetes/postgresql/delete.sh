#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-postgresql"

echo "Removendo Postgresql ..."

kubectl delete -n "$namespace" -f "$script_dir"/postgresql-service.yaml
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-deployment.yaml
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-secret.yaml
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-persistent-volume-claim.yaml
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-persistent-volume.yaml
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-storage-class.yaml

echo "Postgresql removido com sucesso"
