#!/bin/bash
CONTAINER_NAME=happy-beans-container
IMAGE_NAME=happy-beans-image:1.0
HOST_PORT=80
CONTAINER_PORT=80

echo ">>> Starting Docker container..."
docker run -d --name $CONTAINER_NAME -p $HOST_PORT:$CONTAINER_PORT $IMAGE_NAME
