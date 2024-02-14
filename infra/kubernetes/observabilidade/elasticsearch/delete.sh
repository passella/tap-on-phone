#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

echo "Removendo Elasticsearch ..."

namespace="tap-on-phone-elasticsearch"

kubectl delete -n "$namespace" -f "$script_dir"/kibana-tap-on-phone.yaml
kubectl delete -n "$namespace" -f "$script_dir"/elasticsearch-tap-on-phone.yaml

kubectl delete -f "$script_dir"/elasticsearch-operator.yaml
kubectl delete -f "$script_dir"/elasticsearch-crds.yaml

echo "Elasticsearch removido com sucesso"
