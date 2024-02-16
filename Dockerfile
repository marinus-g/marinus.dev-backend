FROM amazoncorretto:21-alpine-jdk
LABEL authors="marinus"
MAINTAINER marinus.dev
COPY wait-for-db.sh /wait-for-db.sh
COPY build/libs/website-backend-0.0.1-SNAPSHOT.jar marinus-dev-backend-1.0.0.jar
RUN apk update && apk add mysql-client
EXPOSE 8080
RUN chmod +x /wait-for-db.sh
ENTRYPOINT ["sh", "/wait-for-db.sh", "mariadb", "java", "-jar", "/marinus-dev-backend-1.0.0.jar"]