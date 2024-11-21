#!/bin/bash

services=()

for service in "${services[@]}"; do
    cd /home/projects/promixity-finder/ &&
        docker compose -f docker-compose.yml up "$service" --remove-orphans -d
done