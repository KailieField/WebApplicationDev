#-----------------
# BUILD STAGE
#-----------------

FROM gradle:8-jdk23-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

#-----------------
# PACK STATE
#-----------------
FROM openjdk:23-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/inventory-service.jar

ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]