#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-services"

kubectl delete -n "$namespace" -f "$script_dir"/cadastro-ingress.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/cadastro-service.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/cadastro-autoscaling.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/cadastro-deployment.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/cadastro-env-config.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-secret.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-cadastro-secret.yaml --wait
