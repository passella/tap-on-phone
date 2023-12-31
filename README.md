# Tap-on-phone

Tap-on-phone é um projeto que permite realizar pagamentos por meio de um dispositivo móvel, usando a tecnologia NFC.

O projeto é dividido nas seguintes aplicações:

* **Gateway**: responsável por rotear as requisições entre as outras aplicações.
* **Cadastro**: responsável por gerenciar os dados dos estabelecimentos e dos dispositivos.
* **Operações Pagamento**: responsável por criar e atualizar os pagamentos.
* **Pagamentos**: responsável por consultar os pagamentos de um estabelecimento.
* **Motor Pagamentos**: responsável por processar os pagamentos e enviar mensagens para o Big Data e o Dead Letter.

## Pré-requisitos

Para subir a solução completa é necessário ter o Docker e o Docker Compose instalados na máquina.

## Instalação Docker

Seguir a documentação oficial: [https://www.docker.com/](https://www.docker.com/)

## Como executar

Para gerar as imagens execute o comando: `./build.sh`

Para subir a solução execute o comando: `./run.sh`

Para gerar as imagens e subir a solução, execute o comando: `./build_run.sh`

## **Como testar**

Dentro da pasta testes, existe o arquivo tap-on-phone.postman_collection.json, esse arquivo é a coleção do Postman para testar a aplicação.

### Lista de requisições:

**Criar estabelecimento:**

`curl --location 'http://localhost:8080/cadastro/api/v1/estabelecimentos' 
--header 'Content-Type: application/json' 
--data '{
"nome": "Estabelecimento X"
}'`

**Criar Device:**

`curl --location 'http://localhost:8080/cadastro/api/v1/devices' 
--header 'Content-Type: application/json' 
--data '{
"nome": "Device 4",
"estabelecimentos": [
"230a66ab-b3e4-41cd-af09-36c56fb91038"
]
}'`

**Criar Pagamento:**

`curl --location 'http://localhost:8080/operacao-pagamento/api/v1/pagamentos' 
--header 'Content-Type: application/json' 
--data '{
"id_device": "3199f384-d8b2-411d-b40d-14991a0dffb7",
"valor": 50.89
}'`

**Obter Device:**

`curl --location 'http://localhost:8080/cadastro/api/v1/devices/3199f384-d8b2-411d-b40d-14991a0dffb7'`

**Atualizar pagamento:**

`curl --location --request PATCH 'http://localhost:8080/operacao-pagamento/api/v1/pagamentos' 
--header 'Content-Type: application/json' 
--data '{
"id_pagamento": "aac098db-8296-493c-b654-e9177bc0a222",
"status": "CANCELADO"
}'`

**Obter pagamentos:**

`curl --location 'http://localhost:8080/pagamento/api/v1/pagamentos/230a66ab-b3e4-41cd-af09-36c56fb91038'`

### Documentação

O usuário pode acessar o Swagger diretamente de cada aplicação:

* Aplicação Cadastro: http://localhost:8081/webjars/swagger-ui/index.html
* Aplicação Operações Pagamento: http://localhost:8082/webjars/swagger-ui/index.html
* Aplicação Pagamento: http://localhost:8083/webjars/swagger-ui/index.html

Dentro de cada Swagger existem os schemas.

### Observações

A solução envia mensagens para Big Data e Dead Letter, porém ainda não existem as aplicações que escutam esse tópico.
