#!/bin/bash
set -e

echo "Starting happy-beans container..."

sudo docker run \
  --env-file /home/ubuntu/config/.env \
  -p 80:80 \
  --name happy-beans \
  -d happy-beans-image:1.0

echo "Container started successfully."
