#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

kubectl delete -f "$script_dir"/cadastro-deployment.yaml
kubectl delete -f "$script_dir"/cadastro-autoscaling.yaml
kubectl delete -f "$script_dir"/cadastro-service.yaml
kubectl delete -f "$script_dir"/cadastro-ingress.yaml

kubectl delete -f "$script_dir"/postgresql-postgres-secret.yaml
kubectl delete -f "$script_dir"/postgresql-cadastro-secret.yaml
