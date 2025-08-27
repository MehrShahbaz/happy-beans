#!/bin/bash
JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1 || true)

echo "Please" >> /home/ubuntu/app/.env.properties

# Load environment variables from .env
export $(grep -v '^#' /home/ubuntu/app/.env.properties | xargs)

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> ####### Test #######"
echo ">>> [ApplicationStart] Starting application: $JAR_FILE"
nohup sudo -E java -jar /home/ubuntu/app/build/libs/happy-beans-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > /home/ubuntu/app/logs/app.log 2>&1 &