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
    -DJWT_SECRET="$JWT_SECRET" \
    -DJWT_TIME="$JWT_TIME" \
    -DSTRIPE_SECRET_KEY="$STRIPE_SECRET_KEY" \
    > /home/ubuntu/app/app.log 2>&1 &