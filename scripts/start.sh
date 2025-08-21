#!/bin/bash

# Path to app directory and env file
APP_DIR=/home/ubuntu/app/build/libs
ENV_FILE=/home/ubuntu/config/.env
LOG_FILE=/home/ubuntu/app/app.log

# Find the JAR file (first one in the directory)
JAR_FILE=$(ls $APP_DIR/*.jar | head -n 1)

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> ####### Test #######"
echo ">>> [ApplicationStart] Starting application: $JAR_FILE"

# Load environment variables from .env
if [ -f "$ENV_FILE" ]; then
  export $(grep -v '^#' "$ENV_FILE" | xargs)
  echo ">>> [ApplicationStart] Loaded environment variables from $ENV_FILE"
else
  echo ">>> [ApplicationStart] WARNING: .env file not found at $ENV_FILE"
fi

# Stop any existing app
PID=$(pgrep -f $JAR_FILE)
if [ -n "$PID" ]; then
  echo ">>> [ApplicationStart] Stopping existing app with PID $PID"
  kill -9 $PID
fi

# Start the app
nohup sudo java -jar $JAR_FILE > $LOG_FILE 2>&1 &

echo ">>> [ApplicationStart] App started with PID $!"
