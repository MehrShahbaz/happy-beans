#!/bin/bash
APP_DIR=/home/ubuntu/app
echo ">>> Building Docker image..."
docker compose -f $APP_DIR/docker-compose.yml build
