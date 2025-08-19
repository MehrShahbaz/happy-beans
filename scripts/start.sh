#!/bin/bash
APP_DIR=/home/ubuntu/app
echo ">>> Starting Docker container with Compose..."
docker compose -f $APP_DIR/docker-compose.yml up -d
