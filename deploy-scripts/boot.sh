#!/bin/bash

services=()

for service in "${services[@]}"; do
    cd /home/projects/proximity-finder/ &&
        docker compose -f docker-compose.yml up "$service" --remove-orphans -d
done

if docker ps --filter "name=rabbitmq-container" --format '{{.Names}}' | grep -q "rabbitmq-container"; then
    echo "Rabbitmq container is already up and running"
else
    cd /home/projects/proximity-finder/ &&
        echo "Rabbitmq container is not up and running"
        if docker compose -f docker-compose.yml up rabbitmq --remove-orphans -d; then
                echo "RabbitMQ container started successfully"
        else
                echo "Failed to start RabbitMQ container"
                exit 1
        fi
fi

echo "Deployment complete"
echo "All service containers are up and running 👌"