#!/bin/bash
echo ">>> Stopping old containers..."
docker compose -f /home/ubuntu/app/docker-compose.yml down || true

# Also kill any leftover java processes
pkill -f 'happy-beans' || true
