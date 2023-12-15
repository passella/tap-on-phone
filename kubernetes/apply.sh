#!/bin/bash
set -e

echo "Subindo solução ..."

kubectl apply -f database/tap-on-phone-database-namespace.yaml

kubectl apply -f database/volume/database-storage-class.yaml
kubectl apply -f database/volume/database-persistent-volume.yaml
kubectl apply -f database/volume/database-persistent-volume-claim.yaml

kubectl apply -f database/postgresql/postgresql-password.yaml
kubectl apply -f database/postgresql/postgresql-stateful-set.yaml
kubectl apply -f database/postgresql/postgresql-service.yaml

kubectl apply -f database/pgpool/pgpool-stateful-set.yaml
kubectl apply -f database/pgpool/pgpool-service.yaml

echo "Porta PostgreSQL: $(kubectl get service pgpool -n tap-on-phone-database -o jsonpath='{.spec.ports[0].nodePort}')"
echo "Solução pronta para uso"
