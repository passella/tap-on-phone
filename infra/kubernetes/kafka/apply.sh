#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-kafka"

echo "Criando Zookeeper ..."

zookeeper_deployment_name=$(kubectl apply -f "$script_dir"/zookeeper-deployment.yaml --dry-run=client -o json |
  jq -r '.metadata.name')

kubectl get namespace "$namespace" || kubectl create namespace "$namespace"

kubectl apply -n "$namespace" -f "$script_dir"/zookeeper-env-config.yaml
kubectl apply -n "$namespace" -f "$script_dir"/zookeeper-deployment.yaml

if ! kubectl -n "$namespace" rollout status -f "$script_dir"/zookeeper-deployment.yaml; then
  echo "Rollout falhou devido ao timeout. Realizando rollback..."
  kubectl -n "$namespace" rollout undo deployment "$zookeeper_deployment_name"
  exit 1
fi

sleep 10

kubectl apply -n "$namespace" -f "$script_dir"/zookeeper-service.yaml

echo "Zookepeer criado com sucesso"

echo "Criando Kafka ..."

kafka_deployment_name=$(kubectl apply -f "$script_dir"/kafka-deployment.yaml --dry-run=client -o json |
  jq -r '.metadata.name')

kubectl apply -n "$namespace" -f "$script_dir"/kafka-env-config.yaml
kubectl apply -n "$namespace" -f "$script_dir"/kafka-deployment.yaml

if ! kubectl -n "$namespace" rollout status -f "$script_dir"/kafka-deployment.yaml; then
  echo "Rollout falhou devido ao timeout. Realizando rollback..."
  kubectl -n "$namespace" rollout undo deployment "$kafka_deployment_name"
  exit 1
fi

sleep 10

kubectl apply -n "$namespace" -f "$script_dir"/kafka-service.yaml

echo "Kafka criado com sucesso"
echo "Porta Kafka: $(kubectl get service kafka -n "$namespace" -o jsonpath='{.spec.ports[0].nodePort}')"
echo "Porta Kafka: $(kubectl get service kafka -n "$namespace" -o jsonpath='{.spec.ports[1].nodePort}')"
