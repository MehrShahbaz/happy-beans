#!/bin/bash
APP_NAME=happy-beans
CONTAINER_NAME=happy-beans-container
IMAGE_NAME=happy-beans-image:1.0

echo ">>> Stopping old container (if exists)..."
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

echo ">>> Removing old Docker image (optional)..."
docker rmi $IMAGE_NAME || true
