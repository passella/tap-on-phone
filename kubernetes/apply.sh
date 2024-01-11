#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Subindo solução ..."

echo "Subindo namespaces ..."
kubectl apply -f "$script_dir"/database/tap-on-phone-database-namespace.yaml
kubectl apply -f "$script_dir"/services/tap-on-phone-namespace.yml
echo "Namespaces subidos com sucesso"

echo "Subindo volumes e PVCs ..."
kubectl apply -f "$script_dir"/database/volume/database-storage-class.yaml
kubectl apply -f "$script_dir"/database/volume/database-persistent-volume.yaml
kubectl apply -f "$script_dir"/database/volume/database-persistent-volume-claim.yaml
echo "Volumes e PVCs subidos com sucesso"

echo "Subindo PostgreSQL..."
kubectl apply -f "$script_dir"/database/postgresql/postgresql-secret.yaml
kubectl apply -f "$script_dir"/database/postgresql/postgresql-deployment.yaml
kubectl apply -f "$script_dir"/database/postgresql/postgresql-autoscaling.yaml
kubectl apply -f "$script_dir"/database/postgresql/postgresql-service.yaml
echo "Postgresql subido com sucesso"

echo "Subindo env-config ..."
kubectl apply -f "$script_dir"/services/env-config.yaml
echo "Env-config subido com sucesso"

CADASTRO_VERSION=$(cat "$script_dir"/../services/cadastro/versao.txt)
echo "Subindo imagem service-cadastro para o registry ..."
docker tag service-cadastro:"$CADASTRO_VERSION" localhost:32000/service-cadastro:"$CADASTRO_VERSION"
docker push localhost:32000/service-cadastro:"$CADASTRO_VERSION"
echo "Imagem service-cadastro publicada com sucesso"

echo "Subindo deploy service-cadastro ..."
CADASTRO_VERSION=$(cat "$script_dir"/../services/cadastro/versao.txt) envsubst < "$script_dir"/../services/cadastro/kubernetes/cadastro-deployment.yaml | kubectl apply -f -
kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/cadastro-service.yaml
kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/cadastro-ingress.yaml
echo "Deploy service-cadastro subido com sucesso"

cp "$script_dir"/database/postgresql/postgresql-secret.yaml "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml
sed -i "s/namespace: tap-on-phone-database/namespace: tap-on-phone/g" "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml
sed -i "s/name: postgresql-secret/name: postgresql-postgres-secret/g" "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml

kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/postgresql-postgres-secret.yaml
kubectl apply -f "$script_dir"/../services/cadastro/kubernetes/postgresql-cadastro-secret.yaml

echo "Porta PostgreSQL: $(kubectl get service postgresql -n tap-on-phone-database -o jsonpath='{.spec.ports[0].nodePort}')"
echo "Solução pronta para uso"
