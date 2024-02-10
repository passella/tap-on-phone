#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

echo "Removendo solução ..."

"$script_dir"/services/gateway/kubernetes/delete.sh
"$script_dir"/services/pagamento/kubernetes/delete.sh
"$script_dir"/services/operacao-pagamento/kubernetes/delete.sh
"$script_dir"/services/motor-pagamento/kubernetes/delete.sh
"$script_dir"/services/cadastro/kubernetes/delete.sh
"$script_dir"/infra/kubernetes/redis/delete.sh
"$script_dir"/infra/kubernetes/mongo/delete.sh
"$script_dir"/infra/kubernetes/postgresql/delete.sh
"$script_dir"/infra/kubernetes/kafka/delete.sh

echo "Solução removida com sucesso"
