#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
base_dir="$script_dir"/../../build
versao=$("$script_dir"/mvnw -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec -f "$script_dir"/pom.xml)
echo "$versao" > "$script_dir"/versao.txt
echo "Iniciando build da imagem service-motor-pagamento:$versao..."

echo "script_dir: $script_dir"
echo "base_dir: $base_dir"

"$base_dir"/build-base.sh

echo "Criando imagem service-motor-pagamento-builder..."
docker build -f "$base_dir"/Dockerfile-builder -t service-motor-pagamento-builder:"$versao" "$script_dir"
echo "Imagem service-motor-pagamento-builder criada com sucesso..."
echo "Executando service-motor-pagamento-builder..."
docker run --rm --env-file "$base_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$base_dir"/.buid-output:/output -v "$base_dir"/.m2-cache:/root/.m2 service-motor-pagamento-builder:"$versao"
echo "service-motor-pagamento-builder executado com sucesso..."
echo "Criando imagem service-motor-pagamento..."
docker build -f "$script_dir"/Dockerfile -t service-motor-pagamento:"$versao" "$base_dir"
echo "Imagem service-motor-pagamento criada com sucesso..."

echo "Tagando imagem service-motor-pagamento..."
docker tag service-motor-pagamento:"$versao" service-motor-pagamento:latest
echo "Imagem service-motor-pagamento tagada com sucesso"

echo "Salvando imagem service-motor-pagamento..."
docker save -o "$base_dir"/.buid-output/service-motor-pagamento-"$versao".tar service-motor-pagamento:"$versao"
echo "Imagem service-motor-pagamento salva com sucesso"

echo "Incrementando versão ..."
"$script_dir"/mvnw build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} -f "$script_dir"/pom.xml
echo "Versão incrementada com sucesso"
