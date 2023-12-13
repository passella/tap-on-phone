#!/bin/bash
set -e

echo "Removendo pgpool..."
kubectl delete -f pgpool
echo "Pgpool removido com sucesso"

echo "Removendo postgresql..."
kubectl delete -f postgresql
echo "Postgresql removido com sucesso"

echo "Removendo persistent volume claim..."
kubectl delete -f postgresql-persistentvolumeclaim.yaml
echo "Persistent volume claim removido com sucesso"

echo "Removendo storage class..."
kubectl delete -f storage-class.yaml
echo "Storage class removido com sucesso"

echo "Removendo namespace database..."
kubectl delete -f tap-on-phone-database-namespace.yaml
echo "Namespace database removido com sucesso"

echo "Database removido"
