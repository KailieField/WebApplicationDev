#--------------
# BUILD STAGE
#--------------

FROM gradle:8-jdk-21-and-22-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test


#--------------
# PACKAGE STAGE
#--------------

FROM openjdk:21-jdk-slim

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/notification-service.jar

ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password

EXPOSE 8085

ENTRYPOINT ["java","-jar", "/app/notification-service.jar"]