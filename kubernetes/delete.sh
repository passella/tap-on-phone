#!/bin/bash

script_dir=$(dirname $(realpath "$0"))

echo "Removendo solução ..."

"$script_dir"/../services/cadastro/kubernetes/delete.sh
"$script_dir"/database/postgresql/delete.sh

echo "Solução removida com sucesso"
