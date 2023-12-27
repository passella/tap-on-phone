#!/bin/bash
set -e

echo "Subindo solução ..."

kubectl apply -f database/tap-on-phone-database-namespace.yaml

kubectl apply -f database/volume/database-storage-class.yaml
kubectl apply -f database/volume/database-persistent-volume.yaml
kubectl apply -f database/volume/database-persistent-volume-claim.yaml

kubectl apply -f database/postgresql/postgresql-password.yaml
kubectl apply -f database/postgresql/postgresql-deployment.yaml
kubectl apply -f database/postgresql/postgresql-autoscaling.yaml
kubectl apply -f database/postgresql/postgresql-service.yaml

echo "Porta PostgreSQL: $(kubectl get service postgresql -n tap-on-phone-database -o jsonpath='{.spec.ports[0].nodePort}')"
echo "Solução pronta para uso"
