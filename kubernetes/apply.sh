#!/bin/bash
set -e

cd postgresql
./apply.sh

cd ..
kubectl apply -f namespace.yml
kubectl apply -f storage-class.yaml
kubectl apply -f traefik-ingress-controller.yaml
