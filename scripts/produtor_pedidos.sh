#!/bin/bash


if ! command -v amqp-publish &> /dev/null
then
    echo "amqp-publish could not be found"
    exit 1
fi

if [ "$#" -ne 2 ] || [ "$1" == "-h" ] || [ "$1" == "--help" ] ; then
    echo "Usage: ./produtor_pedidos.sh [quantidade_pedidos] [quantidade_maxima_itens_por_pedido]"
    echo "Environment variables: RABBITMQ_USERNAME (default=user), RABBITMQ_PASSWORD (default=password)"
    exit 1
fi

quantidade_pedidos=$1
quantidade_maxima_itens_por_pedido=$2

if [ -z "$RABBITMQ_USERNAME" ]; then
    RABBITMQ_USERNAME="user"
fi

if [ -z "$RABBITMQ_PASSWORD" ]; then
    RABBITMQ_PASSWORD="password"
fi


function random_number() {
    echo $((1 + RANDOM % $1))
}

function random_product() {
    options=("lapis" "caneta" "caderno" "borracha" "apontador" "cola" "tesoura" "fita adesiva" "papel" "grampeador")
    echo "${options[$((RANDOM % ${#options[@]}))]}"
}

for i in $(seq 1 "$quantidade_pedidos")
do
    items="["

    quantidade_itens=$(random_number "$quantidade_maxima_itens_por_pedido")
    for j in $(seq 1 "$quantidade_itens")
    do
        items=$items'{"produto": "'$(random_product)'", "quantidade": '$(random_number 10)', "preco": '$(random_number 100)'.0}'
        if [ "$j" -lt "$quantidade_itens" ]; then
            items=$items","
        fi
    done

    items=$items"]"

  amqp-publish --username "$RABBITMQ_USERNAME"  --password "$RABBITMQ_PASSWORD" -r pedidos -C application/json -b '{"codigoPedido": '"$i"', "codigoCliente": 1, "items": '"$items"'}'
done
