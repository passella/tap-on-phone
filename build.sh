#!/bin/bash
echo "Preparando ambiente..."

set -e

clear

if [ -d ".buid-output" ]; then
  rm -rf .buid-output
fi
mkdir .buid-output

if [ ! -d ".m2-cache" ]; then
  mkdir .m2-cache
fi

chmod +X startup.sh

echo "Ambiente pronto"

echo "Criando imagem postgresql..."
docker build -f infra/postgresql/Dockerfile -t postgresql-cadastro:latest infra/postgresql/
echo "Imagem postgresql criada com sucesso"
echo "Salvando imagem postgresql..."
docker save -o .buid-output/postgresql-cadastro.tar postgresql-cadastro
echo "Imagem postgresql salva com sucesso"

echo "Criando imagem base ..."
docker build -f Dockerfile-basebuilder -t builder:1.0.0 .
echo "Imagem base criada com sucesso"

echo "Criando imagem service-cadastro-builder..."
docker build -f Dockerfile-builder -t service-cadastro-builder:1.0.0 services/cadastro/
echo "Imagem service-cadastro-builder criada com sucesso..."
echo "Executando service-cadastro-builder..."
docker run --rm --env-file build.env -v /var/run/docker.sock:/var/run/docker.sock -v $PWD/.buid-output:/output -v $PWD/.m2-cache:/root/.m2 service-cadastro-builder:1.0.0
echo "service-cadastro-builder executado com sucesso..."
echo "Criando imagem service-cadastro..."
docker build -f services/cadastro/Dockerfile -t service-cadastro:1.0.0 .
echo "Imagem service-cadastro criada com sucesso..."
echo "Salvando imagem service-cadastro..."
docker save -o .buid-output/service-cadastro-1.0.0.tar service-cadastro:1.0.0
echo "Imagem service-cadastro salva com sucesso"

echo "Criando imagem service-gateway-builder..."
docker build -f Dockerfile-builder -t service-gateway-builder:1.0.0 services/gateway/
echo "Imagem service-gateway-builder criada com sucesso..."
echo "Executando service-gateway-builder..."
docker run --rm --env-file build.env -v /var/run/docker.sock:/var/run/docker.sock -v $PWD/.buid-output:/output -v $PWD/.m2-cache:/root/.m2 service-gateway-builder:1.0.0
echo "service-gateway-builder executado com sucesso..."
echo "Criando imagem service-gateway..."
docker build -f services/gateway/Dockerfile -t service-gateway:1.0.0 .
echo "Imagem service-gateway criada com sucesso..."
echo "Salvando imagem service-gateway..."
docker save -o .buid-output/service-gateway-1.0.0.tar service-gateway:1.0.0
echo "Imagem service-gateway salva com sucesso"

echo "Criando imagem service-operacao-pagamento-builder..."
docker build -f Dockerfile-builder -t service-operacao-pagamento-builder:1.0.0 services/operacao-pagamento/
echo "Imagem service-operacao-pagamento-builder criada com sucesso..."
echo "Executando service-operacao-pagamento-builder..."
docker run --rm --env-file build.env -v /var/run/docker.sock:/var/run/docker.sock -v $PWD/.buid-output:/output -v $PWD/.m2-cache:/root/.m2 service-operacao-pagamento-builder:1.0.0
echo "service-operacao-pagamento-builder executado com sucesso..."
echo "Criando imagem service-operacao-pagamento..."
docker build -f services/operacao-pagamento/Dockerfile -t service-operacao-pagamento:1.0.0 .
echo "Imagem service-operacao-pagamento criada com sucesso..."
echo "Salvando imagem service-operacao-pagamento..."
docker save -o .buid-output/service-operacao-pagamento-1.0.0.tar service-operacao-pagamento:1.0.0
echo "Imagem service-operacao-pagamento salva com sucesso"

echo "Criando imagem service-motor-pagamento-builder..."
docker build -f Dockerfile-builder -t service-motor-pagamento-builder:1.0.0 services/motor-pagamento/
echo "Imagem service-motor-pagamento-builder criada com sucesso..."
echo "Executando service-motor-pagamento-builder..."
docker run --rm --env-file build.env -v /var/run/docker.sock:/var/run/docker.sock -v $PWD/.buid-output:/output -v $PWD/.m2-cache:/root/.m2 service-motor-pagamento-builder:1.0.0
echo "service-motor-pagamento-builder executado com sucesso..."
echo "Criando imagem service-motor-pagamento..."
docker build -f services/motor-pagamento/Dockerfile -t service-motor-pagamento:1.0.0 .
echo "Imagem service-motor-pagamento criada com sucesso..."
echo "Salvando imagem service-motor-pagamento..."
docker save -o .buid-output/service-motor-pagamento-1.0.0.tar service-motor-pagamento:1.0.0
echo "Imagem service-motor-pagamento salva com sucesso"

echo "Criando imagem service-pagamento-builder..."
docker build -f Dockerfile-builder -t service-pagamento-builder:1.0.0 services/pagamento/
echo "Imagem service-pagamento-builder criada com sucesso..."
echo "Executando service-pagamento-builder..."
docker run --rm --env-file build.env -v /var/run/docker.sock:/var/run/docker.sock -v $PWD/.buid-output:/output -v $PWD/.m2-cache:/root/.m2 service-pagamento-builder:1.0.0
echo "service-pagamento-builder executado com sucesso..."
echo "Criando imagem service-pagamento..."
docker build -f services/pagamento/Dockerfile -t service-pagamento:1.0.0 .
echo "Imagem service-pagamento criada com sucesso..."
echo "Salvando imagem service-pagamento..."
docker save -o .buid-output/service-pagamento-1.0.0.tar service-pagamento:1.0.0
echo "Imagem service-pagamento salva com sucesso"
