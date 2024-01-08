#!/bin/bash
set -e
echo "Iniciando build das imagens..."

script_dir=$(dirname $(realpath "$0"))

"$script_dir"/build-base.sh

echo "Criando imagem postgresql..."
docker build -f "$script_dir"/../infra/postgresql/Dockerfile -t postgresql-cadastro:latest "$script_dir"/../infra/postgresql/
echo "Imagem postgresql criada com sucesso"
echo "Salvando imagem postgresql..."
docker save -o "$script_dir"/.buid-output/postgresql-cadastro.tar postgresql-cadastro
echo "Imagem postgresql salva com sucesso"

"$script_dir"/../services/cadastro/build.sh

echo "Criando imagem service-gateway-builder..."
docker build -f "$script_dir"/Dockerfile-builder -t service-gateway-builder:1.0.0 "$script_dir"/../services/gateway/
echo "Imagem service-gateway-builder criada com sucesso..."
echo "Executando service-gateway-builder..."
docker run --rm --env-file "$script_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$script_dir"/.buid-output:/output -v "$script_dir"/.m2-cache:/root/.m2 service-gateway-builder:1.0.0
echo "service-gateway-builder executado com sucesso..."
echo "Criando imagem service-gateway..."
docker build -f "$script_dir"/../services/gateway/Dockerfile -t service-gateway:1.0.0 "$script_dir"
echo "Imagem service-gateway criada com sucesso..."
echo "Salvando imagem service-gateway..."
docker save -o "$script_dir"/.buid-output/service-gateway-1.0.0.tar service-gateway:1.0.0
echo "Imagem service-gateway salva com sucesso"

echo "Criando imagem service-operacao-pagamento-builder..."
docker build -f "$script_dir"/Dockerfile-builder -t service-operacao-pagamento-builder:1.0.0 "$script_dir"/../services/operacao-pagamento/
echo "Imagem service-operacao-pagamento-builder criada com sucesso..."
echo "Executando service-operacao-pagamento-builder..."
docker run --rm --env-file "$script_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$script_dir"/.buid-output:/output -v "$script_dir"/.m2-cache:/root/.m2 service-operacao-pagamento-builder:1.0.0
echo "service-operacao-pagamento-builder executado com sucesso..."
echo "Criando imagem service-operacao-pagamento..."
docker build -f "$script_dir"/../services/operacao-pagamento/Dockerfile -t service-operacao-pagamento:1.0.0 "$script_dir"
echo "Imagem service-operacao-pagamento criada com sucesso..."
echo "Salvando imagem service-operacao-pagamento..."
docker save -o "$script_dir"/.buid-output/service-operacao-pagamento-1.0.0.tar service-operacao-pagamento:1.0.0
echo "Imagem service-operacao-pagamento salva com sucesso"

echo "Criando imagem service-motor-pagamento-builder..."
docker build -f "$script_dir"/Dockerfile-builder -t service-motor-pagamento-builder:1.0.0 "$script_dir"/../services/motor-pagamento/
echo "Imagem service-motor-pagamento-builder criada com sucesso..."
echo "Executando service-motor-pagamento-builder..."
docker run --rm --env-file "$script_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$script_dir"/.buid-output:/output -v "$script_dir"/.m2-cache:/root/.m2 service-motor-pagamento-builder:1.0.0
echo "service-motor-pagamento-builder executado com sucesso..."
echo "Criando imagem service-motor-pagamento..."
docker build -f "$script_dir"/../services/motor-pagamento/Dockerfile -t service-motor-pagamento:1.0.0 "$script_dir"
echo "Imagem service-motor-pagamento criada com sucesso..."
echo "Salvando imagem service-motor-pagamento..."
docker save -o "$script_dir"/.buid-output/service-motor-pagamento-1.0.0.tar service-motor-pagamento:1.0.0
echo "Imagem service-motor-pagamento salva com sucesso"

echo "Criando imagem service-pagamento-builder..."
docker build -f "$script_dir"/Dockerfile-builder -t service-pagamento-builder:1.0.0 "$script_dir"/../services/pagamento/
echo "Imagem service-pagamento-builder criada com sucesso..."
echo "Executando service-pagamento-builder..."
docker run --rm --env-file "$script_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$script_dir"/.buid-output:/output -v "$script_dir"/.m2-cache:/root/.m2 service-pagamento-builder:1.0.0
echo "service-pagamento-builder executado com sucesso..."
echo "Criando imagem service-pagamento..."
docker build -f "$script_dir"/../services/pagamento/Dockerfile -t service-pagamento:1.0.0 "$script_dir"
echo "Imagem service-pagamento criada com sucesso..."
echo "Salvando imagem service-pagamento..."
docker save -o "$script_dir"/.buid-output/service-pagamento-1.0.0.tar service-pagamento:1.0.0
echo "Imagem service-pagamento salva com sucesso"

echo "Imagens criadas com sucesso"
