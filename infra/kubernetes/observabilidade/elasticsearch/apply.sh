#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Criando Elasticsearch ..."

kubectl create namespace elastic-system || true

kubectl apply -f "$script_dir"/elasticsearch-crds.yaml
kubectl apply -f "$script_dir"/elasticsearch-operator.yaml

if ! kubectl -n elastic-system rollout status statefulset elastic-operator; then
  echo "Rollout falhou devido ao timeout. Realizando rollback..."
  kubectl -n elastic-system rollout undo statefulset elastic-operator
  exit 1
fi

namespace="tap-on-phone-elasticsearch"

kubectl create namespace "$namespace" || true

kubectl apply -n "$namespace" -f "$script_dir"/elasticsearch-tap-on-phone.yaml
kubectl apply -n "$namespace" -f "$script_dir"/kibana-tap-on-phone.yaml

health=""
while [ "$health" != "green" ]; do
  health=$(kubectl -n tap-on-phone-elasticsearch get kibana kibana -o jsonpath='{.status.health}')
  echo "Aguardando kibana: $health"
  sleep 1
done

kubectl patch svc kibana-kb-http -n tap-on-phone-elasticsearch --type='json' -p='[{"op": "replace", "path": "/spec/type", "value": "NodePort"}]'
kibana_port=$(kubectl get svc kibana-kb-http -n tap-on-phone-elasticsearch -o jsonpath='{.spec.ports[0].nodePort}')
echo "Portal Kibana: https://localhost:$kibana_port"

elasticsearch_password=$(kubectl -n tap-on-phone-elasticsearch get secret elasticsearch-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')
echo "Acesso ao kibana, usuário: elastic, senha: $elasticsearch_password"

echo "Elasticsearch e Kibana criados com sucesso"
