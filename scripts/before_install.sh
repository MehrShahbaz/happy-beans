#!/bin/bash
set -e

echo ">>> [BeforeInstall] Stopping Docker container if running..."
#CONTAINER_ID=$(docker ps -q -f name=happy-beans)
#if [ -n "$CONTAINER_ID" ]; then
#    docker stop happy-beans
#    docker rm happy-beans
#fi

echo ">>> [BeforeInstall] Cleaning old build artifacts..."
rm -rf /home/ubuntu/app/build/libs/*