#!/bin/bash

# Find the first JAR in build/libs
JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1 || true)

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> Create log dir if not there"
mkdir -p /home/ubuntu/app/logs

# Make sure the current user owns the log directory
sudo chown -R $(whoami):$(whoami) /home/ubuntu/app/logs

# Load environment variables from .env.properties
if [ -f /home/ubuntu/app/.env.properties ]; then
  export $(grep -v '^#' /home/ubuntu/app/.env.properties | xargs)
fi

echo ">>> ####### Test #######"
echo ">>> [ApplicationStart] Starting application: $JAR_FILE"

# Run the app with nohup so it survives logout
sudo -E nohup java -jar "$JAR_FILE" \
  --spring.profiles.active=prod \
  >> /home/ubuntu/app/logs/app.log 2>&1 &

echo ">>> Application started in background. Logs are at /home/ubuntu/app/logs/app.log"
