#!/usr/bin/env bash

set -e

function resolve_host_ip() {
    default_gw=$(ip route | grep default | grep -v " link" | head -1 | awk '{print $3}')
    host_ip=$(ip route get ${default_gw} | grep src | awk '{print $5}')
    if [[ -z ${host_ip} ]]; then
        default_gw=$(ip route | grep default | grep en0 | head -1 | awk '{print $3}')
        host_ip=$(ip route get ${default_gw} | grep src | awk '{print $NF}')
    fi
    if [[ -z ${host_ip} ]]; then
        echo "ERROR: Cannot resolve host ip address"
        exit 1
        fi
    echo ${host_ip}
}

export KAFKA_HOST=$(resolve_host_ip)

if [[ "${1}" == "up" ]]; then
    echo "KAFKA_HOST=${KAFKA_HOST}"
    ./gradlew assemble
    docker-compose -f docker-compose.yml up --build -d
elif [[ "${1}" == "down" ]]; then
    docker-compose -f docker-compose.yml down
else
    echo "${0} up|down"
    exit 1
fi
