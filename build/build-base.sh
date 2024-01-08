#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

if [ -d "$script_dir/.buid-output" ]; then
  rm -rf "$script_dir"/.buid-output
fi
mkdir "$script_dir"/.buid-output

if [ ! -d "$script_dir/.m2-cache" ]; then
  mkdir "$script_dir"/.m2-cache
fi

chmod +X "$script_dir"/startup.sh

echo "Criando imagem base ..."
docker build -f "$script_dir"/Dockerfile-basebuilder -t builder:1.0.0 "$script_dir"
echo "Imagem base criada com sucesso"
