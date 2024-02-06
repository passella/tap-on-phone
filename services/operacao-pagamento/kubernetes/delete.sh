#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-services"

kubectl delete -n "$namespace" -f "$script_dir"/operacao-pagamento-ingress.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/operacao-pagamento-service.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/operacao-pagamento-autoscaling.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/operacao-pagamento-deployment.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/operacao-pagamento-env-config.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-secret.yaml --wait
kubectl delete -n "$namespace" -f "$script_dir"/postgresql-operacao-pagamento-secret.yaml --wait
