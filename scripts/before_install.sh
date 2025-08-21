#!/bin/bash
set -e

# Stop running container if exists
if sudo docker ps -q --filter "name=happy-beans" | grep -q .; then
  echo "Stopping existing container..."
  sudo docker stop happy-beans
fi

# Remove old container if exists
if sudo docker ps -aq --filter "name=happy-beans" | grep -q .; then
  echo "Removing old container..."
  sudo docker rm happy-beans
fi

sudo systemctl enable docker
