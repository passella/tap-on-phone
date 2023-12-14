#!/bin/bash

echo "Removendo pgpool service..."
kubectl delete -f pgpool-service.yaml
echo "Pgpool service removido com sucesso"

echo "Removendo service..."
kubectl delete -f postgresql-service.yaml
echo "Service removido com sucesso"

echo "Removendo pgpool..."
kubectl delete -f pgpool.yaml
echo "Pgpool removido com sucesso"

echo "Removendo database..."
kubectl delete -f postgresql.yaml
echo "Database removido com sucesso"

echo "Removendo volumne..."
kubectl delete -f volume.yaml
echo "Volume removido com sucesso"

echo "Removendo secrect..."
kubectl delete -f database-secret.yaml
echo "Secret removido com sucesso"


echo "Removendo namespace..."
kubectl delete -f namespace.yaml
echo "Namespace removido com sucesso"


echo "Ambiente limpo"
