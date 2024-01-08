#!/bin/bash
set -e
echo "Iniciando build da imagem service-cadastro..."

script_dir=$(dirname $(realpath "$0"))
base_dir="$script_dir"/../../build

echo "script_dir: $script_dir"
echo "base_dir: $base_dir"

"$base_dir"/build-base.sh

echo "Criando imagem service-cadastro-builder..."
docker build -f "$base_dir"/Dockerfile-builder -t service-cadastro-builder:1.0.0 "$script_dir"
echo "Imagem service-cadastro-builder criada com sucesso..."
echo "Executando service-cadastro-builder..."
docker run --rm --env-file "$base_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$base_dir"/.buid-output:/output -v "$base_dir"/.m2-cache:/root/.m2 service-cadastro-builder:1.0.0
echo "service-cadastro-builder executado com sucesso..."
echo "Criando imagem service-cadastro..."
docker build -f "$script_dir"/Dockerfile -t service-cadastro:1.0.0 "$base_dir"
echo "Imagem service-cadastro criada com sucesso..."
echo "Salvando imagem service-cadastro..."
docker save -o "$base_dir"/.buid-output/service-cadastro-1.0.0.tar service-cadastro:1.0.0
echo "Imagem service-cadastro salva com sucesso"
