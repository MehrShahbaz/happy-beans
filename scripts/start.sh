#!/bin/bash

DEPLOYMENT_DIR="/home/ubuntu/app"

JAR_FILE=$(find "$DEPLOYMENT_DIR/build/libs" -name "*.jar")

if [ -z "$JAR_FILE" ]; then
    echo "Error: JAR file not found in $DEPLOYMENT_DIR/build/libs"
    exit 1
fi

echo "Starting application with environment variables..."

sudo java -jar "$JAR_FILE" \
    --spring.profiles.active=prod \
    -Djwt.secret="$JWT_SECRET" \
    -Djwt.time="$JWT_TIME" \
    -Dstripe.secret.key="$STRIPE_SECRET_KEY" \
    > /home/ubuntu/app/app.log 2>&1 &