#!/bin/bash
JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1)

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> ####### Test #######"
echo ">>> [ApplicationStart] Starting application: $JAR_FILE"
sudo java -jar $JAR_FILE > /home/ubuntu/app/app.log 2>&1 &

