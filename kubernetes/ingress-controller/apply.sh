#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Criando Traefik Ingress Controller ..."

kubectl apply -f "$script_dir"/cluster-wide-ingress-ingress-class.yaml
kubectl apply -f "$script_dir"/traefik-ingress-controller-cluster-role.yml
kubectl apply -f "$script_dir"/traefik-ingress-controller-cluster-role-binding.yml
kubectl apply -f "$script_dir"/traefik-ingress-controller-daemon-set.yml
kubectl apply -f "$script_dir"/traefik-ingress-controller-service-account.yml

echo "Traefik Ingress Controller criado com sucesso"
