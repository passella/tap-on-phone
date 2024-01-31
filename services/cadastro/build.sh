#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
versao=$("$script_dir"/mvnw -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec -f "$script_dir"/pom.xml)
echo "$versao" >"$script_dir"/versao.txt
echo "Iniciando build da imagem service-cadastro:$versao..."

if [ -d "$script_dir/.buid-output" ]; then
  rm -rf "$script_dir"/.buid-output
fi

mkdir "$script_dir"/.buid-output

if [ ! -d "$script_dir/.m2-cache" ]; then
  mkdir "$script_dir"/.m2-cache
fi

if [  -d "$script_dir/target" ]; then
  rm -Rf "$script_dir"/target
fi

chmod +X "$script_dir"/startup.sh

echo "Criando imagem service-cadastro-builder..."
docker build --rm -f "$script_dir"/Dockerfile-builder -t service-cadastro-builder:"$versao" "$script_dir"
echo "Imagem service-cadastro-builder criada com sucesso..."
echo "Executando service-cadastro-builder..."
docker run --rm --env-file "$script_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$script_dir"/.buid-output:/output -v "$script_dir"/.m2-cache:/root/.m2 service-cadastro-builder:"$versao"
echo "service-cadastro-builder executado com sucesso..."
echo "Criando imagem service-cadastro..."
docker build --rm -f "$script_dir"/Dockerfile -t service-cadastro:"$versao" "$script_dir"
echo "Imagem service-cadastro criada com sucesso..."

echo "Tagando imagem service-cadastro..."
docker tag service-cadastro:"$versao" service-cadastro:latest
echo "Imagem service-cadastro tagada com sucesso"

echo "Salvando imagem service-cadastro..."
docker save -o "$script_dir"/.buid-output/service-cadastro-"$versao".tar service-cadastro:"$versao"
echo "Imagem service-cadastro salva com sucesso"

echo "Incrementando versão ..."
"$script_dir"/mvnw build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} -f "$script_dir"/pom.xml
echo "Versão incrementada com sucesso"
