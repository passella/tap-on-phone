#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-postgresql"

echo "Removendo Postgresql ..."

kubectl delete -n "$namespace" -f "$script_dir"/ --wait
kubectl delete namespace "$namespace"

echo "Postgresql removido com sucesso"
