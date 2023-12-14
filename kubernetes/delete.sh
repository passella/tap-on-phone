#!/bin/bash

kubectl delete -f traefik-ingress-controller.yaml

# shellcheck disable=SC2164
cd postgresql
./delete.sh
# shellcheck disable=SC2103
cd ..

kubectl delete -f storage-class.yaml
kubectl delete -f namespace.yml
