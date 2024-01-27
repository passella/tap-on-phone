#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Subindo motor-pagamento ..."

MOTOR_PAGAMENTO_VERSION=$(cat "$script_dir"/../versao.txt)
docker tag service-motor-pagamento:"$MOTOR_PAGAMENTO_VERSION" localhost:32000/service-motor-pagamento:"$MOTOR_PAGAMENTO_VERSION"
docker push localhost:32000/service-motor-pagamento:"$MOTOR_PAGAMENTO_VERSION"

MOTOR_PAGAMENTO_VERSION="$MOTOR_PAGAMENTO_VERSION" envsubst < "$script_dir"/motor-pagamento-deployment.yaml | kubectl apply -f -

echo "Motor pagamento pronto para uso"
