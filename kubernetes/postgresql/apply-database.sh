#!/bin/bash
set -e

echo "Criando namespace database..."
kubectl apply -f tap-on-phone-database-namespace.yaml
echo "Namespace database criado com sucesso"
echo "Criando storage class..."
kubectl apply -f storage-class.yaml
echo "Storage class criado com sucesso"
echo "Criando persistent volume claim..."
kubectl apply -f postgresql-persistentvolumeclaim.yaml
echo "Persistent volume claim criado com sucesso"
echo "Criando postgresql..."
kubectl apply -f postgresql
echo "Postgresql criado com sucesso"
echo "Criando pgpool..."
kubectl apply -f pgpool
echo "Pgpool criado com sucesso"

echo "Database pronto"

echo "Porta Externa PostgreSQL: $(kubectl get service pgpool-service-external -n tap-on-phone-database -o jsonpath='{.spec.ports[0].nodePort}')"
