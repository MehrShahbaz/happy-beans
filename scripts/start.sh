#!/bin/bash
#find JAR
JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1 || true)

# Load environment variables from .env
if [ -f /home/ubuntu/app/.env ]; then
  export $(grep -v '^#' /home/ubuntu/app/.env | xargs)
fi

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> [ApplicationStart] Starting application: $JAR_FILE"
#export $(grep -v '^#' ./.env | xargs)
nohup java -jar $JAR_FILE > /home/ubuntu/app/app.log 2>&1 &