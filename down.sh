#!/bin/sh

# Exits immediately if a command exits with a non-zero status
set -e

# Check if the first argument is "local"
if [ "$1" = "local" ]; then
    # Run docker-compose down for Kafka
    docker-compose -f kafka/docker-compose.yml down

    # Run docker-compose down for the database
    docker-compose -f db/docker-compose.yml down
else
    echo "Usage: ./down.sh local"
    exit 1
fi