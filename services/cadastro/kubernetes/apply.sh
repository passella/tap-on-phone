#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-services"
namespace_blue="tap-on-phone-services-cadastro-blue"
intervalo=5

echo "Subindo cadastro ..."

CADASTRO_VERSION=$(cat "$script_dir"/../versao.txt)
docker tag service-cadastro:"$CADASTRO_VERSION" localhost:32000/service-cadastro:"$CADASTRO_VERSION"
docker push localhost:32000/service-cadastro:"$CADASTRO_VERSION"

if kubectl get namespaces | grep -c "$namespace_blue" >0; then
  kubectl delete namespace "$namespace_blue" --wait
  echo "Aguardando namespace $namespace_blue ser removido..."
  while kubectl get namespaces | grep -c "$namespace_blue" >0; do
    sleep "$intervalo"
  done
fi

kubectl get namespace "$namespace_blue" || kubectl create namespace "$namespace_blue"
kubectl apply -n "$namespace_blue" -f "$script_dir"/cadastro-env-config.yaml --wait
kubectl apply -n "$namespace_blue" -f "$script_dir"/../../../kubernetes/database/postgresql/postgresql-secret.yaml --wait
kubectl apply -n "$namespace_blue" -f "$script_dir"/postgresql-cadastro-secret.yaml --wait
CADASTRO_VERSION="$CADASTRO_VERSION" envsubst <"$script_dir"/cadastro-deployment.yaml | kubectl apply -n "$namespace_blue" -f - --wait
kubectl apply -n "$namespace_blue" -f "$script_dir"/cadastro-autoscaling.yaml --wait
kubectl apply -n "$namespace_blue" -f "$script_dir"/cadastro-service.yaml --wait
kubectl apply -n "$namespace_blue" -f "$script_dir"/cadastro-ingress.yaml --wait

while ! kubectl get pods -n "$namespace_blue" -l app=cadastro -o jsonpath='{.items[*].status.containerStatuses[*].ready}' | grep -q true; do
  echo "Aguardando cadastro blue..."
  sleep "$intervalo"
done

set +e
"$script_dir"/delete.sh
set -e

while kubectl get pods -n "$namespace" -l app=cadastro -o jsonpath='{.items[*].status.containerStatuses[*].ready}' | grep -q true; do
  echo "Aguardando cadastro..."
  sleep "$intervalo"
done

kubectl get namespace "$namespace" || kubectl create namespace "$namespace"
kubectl apply -n "$namespace" -f "$script_dir"/cadastro-env-config.yaml --wait
kubectl apply -n "$namespace" -f "$script_dir"/../../../kubernetes/database/postgresql/postgresql-secret.yaml --wait
kubectl apply -n "$namespace" -f "$script_dir"/postgresql-cadastro-secret.yaml --wait
CADASTRO_VERSION="$CADASTRO_VERSION" envsubst <"$script_dir"/cadastro-deployment.yaml | kubectl apply -n "$namespace" -f - --wait
kubectl apply -n "$namespace" -f "$script_dir"/cadastro-autoscaling.yaml --wait
kubectl apply -n "$namespace" -f "$script_dir"/cadastro-service.yaml --wait
kubectl apply -n "$namespace" -f "$script_dir"/cadastro-ingress.yaml --wait

while ! kubectl get pods -n "$namespace" -l app=cadastro -o jsonpath='{.items[*].status.containerStatuses[*].ready}' | grep -q true; do
  echo "Aguardando cadastro..."
  sleep "$intervalo"
done

set +e
kubectl delete -n "$namespace_blue" -f "$script_dir"/cadastro-ingress.yaml --wait
kubectl delete namespace "$namespace_blue" --wait
set -e

echo "Cadastro pronto para uso"
