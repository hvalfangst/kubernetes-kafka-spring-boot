#!/bin/sh

# Exits immediately if a command exits with a non-zero status
set -e

if [ "$1" = "local" ]; then
    # Run docker-compose up for Kafka
    docker-compose -f kafka/docker-compose.yml up -d

    # Run docker-compose up for the database
    docker-compose -f db/docker-compose.yml up -d
elif [ "$1" = "kubernetes" ]; then

    # Build docker image
      echo "[Building image [distributed-sorting-using-kafka] from Dockerfile]"
      if ! docker build -t hardokkerdocker/hvalfangst:distributed-sorting-using-kafka .; then
        echo
        echo "[Error building image [distributed-sorting-using-kafka] - Exiting script]"
        exit 1
      else
        echo -e "\n\n"
      fi


else
    echo "Usage: ./up.sh local|kubernetes"
    exit 1
fi
