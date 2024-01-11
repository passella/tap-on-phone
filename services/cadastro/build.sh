#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
base_dir="$script_dir"/../../build
versao=$("$script_dir"/mvnw -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec -f "$script_dir"/pom.xml)
echo "$versao" > "$script_dir"/versao.txt
echo "Iniciando build da imagem service-cadastro:$versao..."

echo "script_dir: $script_dir"
echo "base_dir: $base_dir"

"$base_dir"/build-base.sh

echo "Criando imagem service-cadastro-builder..."
docker build -f "$base_dir"/Dockerfile-builder -t service-cadastro-builder:"$versao" "$script_dir"
echo "Imagem service-cadastro-builder criada com sucesso..."
echo "Executando service-cadastro-builder..."
docker run --rm --env-file "$base_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$base_dir"/.buid-output:/output -v "$base_dir"/.m2-cache:/root/.m2 service-cadastro-builder:"$versao"
echo "service-cadastro-builder executado com sucesso..."
echo "Criando imagem service-cadastro..."
docker build -f "$script_dir"/Dockerfile -t service-cadastro:"$versao" "$base_dir"
echo "Imagem service-cadastro criada com sucesso..."

echo "Tagando imagem service-cadastro..."
docker tag service-cadastro:"$versao" service-cadastro:latest
echo "Imagem service-cadastro tagada com sucesso"

echo "Salvando imagem service-cadastro..."
docker save -o "$base_dir"/.buid-output/service-cadastro-"$versao".tar service-cadastro:"$versao"
echo "Imagem service-cadastro salva com sucesso"

echo "Incrementando versão ..."
"$script_dir"/mvnw build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} -f "$script_dir"/pom.xml
echo "Versão incrementada com sucesso"
