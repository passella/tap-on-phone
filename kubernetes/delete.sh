#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

echo "Removendo solução ..."

kubectl delete -f "$script_dir"/../services/cadastro/kubernetes/cadastro-ingress.yaml
kubectl delete -f "$script_dir"/../services/cadastro/kubernetes/cadastro-service.yaml
kubectl delete -f "$script_dir"/../services/cadastro/kubernetes/cadastro-deployment.yaml
kubectl delete -f "$script_dir"/../services/cadastro/kubernetes/postgresql-secret.yaml
kubectl delete -f "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml

kubectl delete -f "$script_dir"/services/env-config.yaml

kubectl delete -f "$script_dir"/database/postgresql/postgresql-service.yaml
kubectl delete -f "$script_dir"/database/postgresql/postgresql-autoscaling.yaml
kubectl delete -f "$script_dir"/database/postgresql/postgresql-deployment.yaml
kubectl delete -f "$script_dir"/database/postgresql/postgresql-secret.yaml

kubectl delete -f "$script_dir"/database/volume/database-persistent-volume-claim.yaml
kubectl delete -f "$script_dir"/database/volume/database-persistent-volume.yaml
kubectl delete -f "$script_dir"/database/volume/database-storage-class.yaml

kubectl delete -f "$script_dir"/database/tap-on-phone-database-namespace.yaml
kubectl delete -f "$script_dir"/services/tap-on-phone-namespace.yml

echo "Solução removida com sucesso"
