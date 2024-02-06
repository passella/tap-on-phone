#!/bin/bash

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-kafka"

echo "Removendo Zookeeper ..."

kubectl delete -n "$namespace" -f "$script_dir"/zookeeper-service.yaml
kubectl delete -n "$namespace" -f "$script_dir"/zookeeper-deployment.yaml
kubectl delete -n "$namespace" -f "$script_dir"/zookeeper-env-config.yaml

echo "Zookeeper removido com sucesso"

echo "Removendo Kafka ..."

kubectl delete -n "$namespace" -f "$script_dir"/kafka-service.yaml
kubectl delete -n "$namespace" -f "$script_dir"/kafka-deployment.yaml
kubectl delete -n "$namespace" -f "$script_dir"/kafka-env-config.yaml

echo "Kafka removido com sucesso"
