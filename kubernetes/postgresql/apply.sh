#!/bin/bash
set -e

echo "Criando namespace database..."
kubectl apply -f namespace.yaml
echo "Namespace database criado com sucesso"

echo "Criando volume..."
kubectl apply -f volume.yaml
echo "Volume criado com sucesso"

echo "Criando secret..."
kubectl apply -f database-secret.yaml
echo "Secret criado com sucesso"

echo "Criando database..."
kubectl apply -f postgresql.yaml
echo "Database criado com sucesso"

echo "Criando service..."
kubectl apply -f postgresql-service.yaml
echo "Service criado com sucesso"

echo "Criando pgpool..."
kubectl apply -f pgpool.yaml
echo "Pgpool criado com sucesso"

echo "Criando pgpool service..."
kubectl apply -f pgpool-service.yaml
echo "Pgpool service criado com sucesso"

echo "Database pronto"

echo "Porta Externa PostgreSQL: $(kubectl get service pgpool -n tap-on-phone-database -o jsonpath='{.spec.ports[0].nodePort}')"
