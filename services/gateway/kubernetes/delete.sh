#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-services"

kubectl delete -n "$namespace" -f "$script_dir"/gateway-ingress.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/gateway-service.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/gateway-autoscaling.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/gateway-deployment.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/gateway-env-config.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-secret.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-gateway-secret.yaml --wait
