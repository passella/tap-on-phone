#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))
versao=$("$script_dir"/mvnw -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec -f "$script_dir"/pom.xml)
echo "$versao" >"$script_dir"/versao.txt
echo "Iniciando build da imagem service-operacao-pagamento:$versao..."

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

echo "Criando imagem service-operacao-pagamento-builder..."
docker build --rm -f "$script_dir"/Dockerfile-builder -t service-operacao-pagamento-builder:"$versao" "$script_dir"
echo "Imagem service-operacao-pagamento-builder criada com sucesso..."
echo "Executando service-operacao-pagamento-builder..."
docker run --rm --env-file "$script_dir"/build.env -v /var/run/docker.sock:/var/run/docker.sock -v "$script_dir"/.buid-output:/output -v "$script_dir"/.m2-cache:/root/.m2 service-operacao-pagamento-builder:"$versao"
echo "service-operacao-pagamento-builder executado com sucesso..."
echo "Criando imagem service-operacao-pagamento..."
docker build --rm -f "$script_dir"/Dockerfile -t service-operacao-pagamento:"$versao" "$script_dir"
echo "Imagem service-operacao-pagamento criada com sucesso..."

echo "Tagando imagem service-operacao-pagamento..."
docker tag service-operacao-pagamento:"$versao" service-operacao-pagamento:latest
echo "Imagem service-operacao-pagamento tagada com sucesso"

echo "Salvando imagem service-operacao-pagamento..."
docker save -o "$script_dir"/.buid-output/service-operacao-pagamento-"$versao".tar service-operacao-pagamento:"$versao"
echo "Imagem service-operacao-pagamento salva com sucesso"

echo "Incrementando versão ..."
"$script_dir"/mvnw build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} -f "$script_dir"/pom.xml
echo "Versão incrementada com sucesso"
