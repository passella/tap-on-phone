#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

echo "Removendo Traefik Ingress Controller ..."

kubectl delete -f "$script_dir"/cluster-wide-ingress-ingress-class.yaml
kubectl delete -f "$script_dir"/traefik-ingress-controller-cluster-role.yml
kubectl delete -f "$script_dir"/traefik-ingress-controller-cluster-role-binding.yml
kubectl delete -f "$script_dir"/traefik-ingress-controller-daemon-set.yml
kubectl delete -f "$script_dir"/traefik-ingress-controller-service-account.yml

echo "Traefik Ingress Controller removido com sucesso"
