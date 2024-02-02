#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
namespace="tap-on-phone-services"

tag_version=$(cat "$script_dir"/../versao.txt)
docker tag service-motor-pagamento:"$tag_version" localhost:32000/service-motor-pagamento:"$tag_version"
docker push localhost:32000/service-motor-pagamento:"$tag_version"

deployment_name=$(tag_version="$tag_version" envsubst <"$script_dir"/motor-pagamento-deployment.yaml |
  kubectl apply -f - --dry-run=client -o json |
  jq -r '.metadata.name')

deployment_name_old=${deployment_name}-old

kubectl create namespace "$namespace" || true

if kubectl -n "$namespace" get -f "$script_dir"/motor-pagamento-deployment.yaml; then
  if kubectl get deployments -n "$namespace" "$deployment_name_old" >/dev/null; then
    kubectl -n "$namespace" delete deployment "$deployment_name_old"
  fi

  tag_version="$tag_version" envsubst <"$script_dir"/motor-pagamento-deployment.yaml |
    kubectl apply -n "$namespace" -f - --dry-run=client -o json |
    jq ".spec.replicas = 1" |
    jq ".metadata.name = \"$deployment_name_old\"" |
    jq ".metadata.labels.app = \"$deployment_name_old\"" |
    jq ".spec.selector.matchLabels.app = \"$deployment_name_old\"" |
    jq ".spec.template.metadata.labels.app = \"$deployment_name_old\"" |
    kubectl apply -n "$namespace" -f -

  if ! kubectl -n "$namespace" rollout status deployment "$deployment_name_old"; then
    echo "Rollout falhou devido ao timeout. Realizando rollback..."
    if ! kubectl -n "$namespace" rollout undo deployment "$deployment_name_old"; then
      kubectl delete -n "$namespace" deployment "$deployment_name_old"
    fi
    exit 1
  fi

  sleep 10

  kubectl apply -n "$namespace" -f "$script_dir"/motor-pagamento-service.yaml --dry-run=client -o json |
    jq ".spec.selector.app = \"$deployment_name_old\"" |
    kubectl apply -n "$namespace" -f -
fi

kubectl apply -n "$namespace" -f "$script_dir"/motor-pagamento-env-config.yaml

tag_version="$tag_version" envsubst <"$script_dir"/motor-pagamento-deployment.yaml |
  kubectl apply -n "$namespace" -f -

if ! kubectl -n "$namespace" rollout status -f "$script_dir"/motor-pagamento-deployment.yaml; then
  echo "Rollout falhou devido ao timeout. Realizando rollback..."
  kubectl -n "$namespace" rollout undo deployment "$deployment_name"
  exit 1
fi

sleep 10

kubectl apply -n "$namespace" -f "$script_dir"/motor-pagamento-autoscaling.yaml
kubectl apply -n "$namespace" -f "$script_dir"/motor-pagamento-service.yaml
kubectl apply -n "$namespace" -f "$script_dir"/motor-pagamento-ingress.yaml

if kubectl -n "$namespace" get deployment "$deployment_name_old"; then
  kubectl delete -n "$namespace" deployment "$deployment_name_old"
fi

echo "motor-pagamento pronto para uso"
