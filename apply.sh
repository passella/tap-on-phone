#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Subindo solução ..."

"$script_dir"/infra/kubernetes/kafka/apply.sh
"$script_dir"/infra/kubernetes/ingress-controller/apply.sh
"$script_dir"/infra/kubernetes/postgresql/apply.sh
"$script_dir"/infra/kubernetes/mongo/apply.sh
"$script_dir"/infra/kubernetes/redis/apply.sh
"$script_dir"/services/cadastro/kubernetes/apply.sh
"$script_dir"/services/motor-pagamento/kubernetes/apply.sh
"$script_dir"/services/operacao-pagamento/kubernetes/apply.sh

echo "Solução pronta para uso"
