#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Subindo imagens ..."
docker tag service-cadastro:1.0.0 localhost:32000/service-cadastro:1.0.0
docker push localhost:32000/service-cadastro:1.0.0
echo "Imagens subidas com sucesso"

echo "Subindo solução ..."

kubectl apply -f "$script_dir"/database/tap-on-phone-database-namespace.yaml
kubectl apply -f "$script_dir"/services/tap-on-phone-namespace.yml

kubectl apply -f "$script_dir"/database/volume/database-storage-class.yaml
kubectl apply -f "$script_dir"/database/volume/database-persistent-volume.yaml
kubectl apply -f "$script_dir"/database/volume/database-persistent-volume-claim.yaml

kubectl apply -f "$script_dir"/database/postgresql/postgresql-secret.yaml
kubectl apply -f "$script_dir"/database/postgresql/postgresql-deployment.yaml
kubectl apply -f "$script_dir"/database/postgresql/postgresql-autoscaling.yaml
kubectl apply -f "$script_dir"/database/postgresql/postgresql-service.yaml

kubectl apply -f "$script_dir"/services/env-config.yaml

kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/cadastro-deployment.yaml
kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/cadastro-service.yaml
kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/cadastro-ingress.yaml

cp "$script_dir"/database/postgresql/postgresql-secret.yaml "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml
sed -i "s/namespace: tap-on-phone-database/namespace: tap-on-phone/g" "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml
sed -i "s/name: postgresql-secret/name: postgresql-postgres-secret/g" "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml

kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml
kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/postgresql-secret.yaml

echo "Porta PostgreSQL: $(kubectl get service postgresql -n tap-on-phone-database -o jsonpath='{.spec.ports[0].nodePort}')"
echo "Solução pronta para uso"
