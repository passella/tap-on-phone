#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-services"

kubectl delete -n "$namespace" -f "$script_dir"/pagamento-ingress.yaml
kubectl delete -n "$namespace" -f "$script_dir"/pagamento-service.yaml
kubectl delete -n "$namespace" -f "$script_dir"/pagamento-autoscaling.yaml
kubectl delete -n "$namespace" -f "$script_dir"/pagamento-deployment.yaml
kubectl delete -n "$namespace" -f "$script_dir"/mongodb-pagamento-secret.yaml
kubectl delete -n "$namespace" -f "$script_dir"/pagamento-env-config.yaml
