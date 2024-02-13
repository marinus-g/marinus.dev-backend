#!/bin/bash

# env variables
EXPOSED_MARINUS_DEV_API_PORT=8080
WEATHER_API_KEY=key
COOKIE_DOMAIN=localhost
COOKIE_SECURE=false

export COOKIE_DOMAIN
export EXPOSED_MARINUS_DEV_API_PORT
export WEATHER_API_KEY
export COOKIE_SECURE

# Set Java 21 as JAVA_HOME
JAVA_HOME="$HOME/.jdks/openjdk-21"
PATH="$JAVA_HOME/bin:$PATH"


# Stop Gradle daemons in the background
./gradlew --stop &

# Wait for Gradle daemons to stop
wait $!

echo "Stopped Gradle daemons"

# Clean project
./gradlew clean

# Build JAR
./gradlew bootJar

# Build or rebuild the "web" image and force recreate the service
docker compose down --rmi all
docker compose up -d --force-recreate

echo "Script executed"
