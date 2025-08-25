#!/bin/bash
set -e

cd /home/ubuntu/app

JAR_FILE=$(ls build/libs/*.jar | head -n 1)

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> Building Docker image"
sudo docker build -t happy-beans-image:1.0 .

echo ">>> Running Docker container"
sudo docker run -d --name happy-beans --env-file /home/ubuntu/config/.env -p 80:80 happy-beans-image:1.0
