#!/bin/bash
set -e
echo "Subindo solução no kubernetes..."
echo "Criando namespace..."
kubectl apply -f kubernetes/namespace.yml
echo "Criando postgresql..."
kubectl apply -f kubernetes/postgresql.yaml
echo "Ambiente pronto"
