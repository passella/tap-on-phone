#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

namespace="tap-on-phone-services"

kubectl delete -n "$namespace" -f "$script_dir"/motor-pagamento-deployment.yaml
kubectl delete -n "$namespace" -f "$script_dir"/motor-pagamento-autoscaling.yaml
kubectl delete -n "$namespace" -f "$script_dir"/motor-pagamento-ingress.yaml
kubectl delete -n "$namespace" -f "$script_dir"/motor-pagamento-env-config.yaml
kubectl delete -n "$namespace" -f "$script_dir"/mongodb-motor-pagamento-secret.yaml
