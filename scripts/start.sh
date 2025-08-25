#!/bin/bash
JAR_FILE=$(ls /home/ubuntu/app/build/libs/*.jar | head -n 1)

if [ -z "$JAR_FILE" ]; then
  echo ">>> [ApplicationStart] No JAR file found!"
  exit 1
fi

echo ">>> ####### Test #######"
echo ">>> changing directory"
cd app

echo ">>> Building image"
sudo docker build -t happy-beans-image:1.0 .

echo ">>> Running docker image"
sudo docker run --env-file /home/ubuntu/config/.env -p 80:80 happy-beans-image:1.0

