#!/bin/bash
set -e

JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1 || true)
if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

mkdir -p /home/ubuntu/app/logs
chown -R $(whoami):$(whoami) /home/ubuntu/app/logs

if [ -f /home/ubuntu/app/.env.properties ]; then
  export $(grep -v '^#' /home/ubuntu/app/.env.properties | xargs)
fi

if ! command -v authbind >/dev/null 2>&1; then
  echo ">>> authbind not found! Install with: sudo apt install authbind"
  exit 1
fi

echo "Changing directory..."
cd /home/ubuntu/app
echo "Directory changed to $(pwd)"

echo ">>> $(date '+%Y-%m-%d %H:%M:%S') [ApplicationStart] Starting application: $JAR_FILE"

nohup authbind --deep java -jar /home/ubuntu/app/build/libs/happy-beans-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > /home/ubuntu/app/logs/app.log 2>&1 &

echo ">>> Application started in background. Logs: /home/ubuntu/app/logs/app.log"
