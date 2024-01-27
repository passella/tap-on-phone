#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

echo "Subindo solução ..."

"$script_dir"/ingress-controller/apply.sh
"$script_dir"/database/postgresql/apply.sh
"$script_dir"/../services/cadastro/kubernetes/apply.sh

echo "Solução pronta para uso"
