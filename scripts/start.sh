#!/bin/bash
JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1)

## Load environment variables from .env
#if [ -f /home/ubuntu/app/.env ]; then
#  export $(grep -v '^#' /home/ubuntu/app/.env | xargs)
#fi

if [ -f /home/ubuntu/app/.env ]; then
  while read line; do
    export "$line"
  done < /home/ubuntu/app/.env
fi

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> ####### Test #######"
echo ">>> [ApplicationStart] Starting application: $JAR_FILE"
export $(grep -v '^#' ./.env | xargs)
sudo -E java -jar $JAR_FILE > /home/ubuntu/app/app.log 2>&1 &