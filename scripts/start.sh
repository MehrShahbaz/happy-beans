#!/bin/bash
JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1 || true)

# Load environment variables from .env
if [ -f /home/ubuntu/app/.env ]; then
  export $(grep -v '^#' /home/ubuntu/app/.env | xargs)
fi

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> ####### Test #######"
echo ">>> [ApplicationStart] Starting application: $JAR_FILE"
#nohup java -jar "$JAR_FILE" > /home/ubuntu/app/logs/app.log 2>&1 &
nohup sudo -E java -jar "$JAR_FILE" --spring.profiles.active=prod > /home/ubuntu/app/logs/app.log 2>&1 &