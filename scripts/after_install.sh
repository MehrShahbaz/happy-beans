#!/bin/bash
APP_NAME=happy-beans
IMAGE_NAME=happy-beans-image:1.0
APP_DIR=/home/ubuntu/app

echo ">>> Building Docker image..."
docker build -t $IMAGE_NAME $APP_DIR
