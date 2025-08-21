#!/bin/bash
set -e

echo "Starting happy-beans container with Docker Compose..."

# Go to the application directory
cd /home/ubuntu/app

# Run Docker Compose
sudo docker compose -f docker-compose.yml up --build -d

echo "Container started successfully."
