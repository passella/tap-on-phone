#!/bin/bash

echo "Removendo solução ..."

kubectl delete -f database/pgpool/pgpool-service.yaml
kubectl delete -f database/pgpool/pgpool-stateful-set.yaml

kubectl delete -f database/postgresql/postgresql-service.yaml
kubectl delete -f database/postgresql/postgresql-deployment.yaml
kubectl delete -f database/postgresql/postgresql-password.yaml

kubectl delete -f database/volume/database-persistent-volume-claim.yaml
kubectl delete -f database/volume/database-persistent-volume.yaml
kubectl delete -f database/volume/database-storage-class.yaml

kubectl delete -f database/tap-on-phone-database-namespace.yaml

echo "Solução removida com sucesso"
