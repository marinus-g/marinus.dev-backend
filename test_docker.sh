#!/bin/sh

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
