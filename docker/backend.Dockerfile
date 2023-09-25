# file for dev
FROM eclipse-temurin:20-jdk-alpine
LABEL authors="Lukas Keller"

ENV PROFILE dev
WORKDIR /home/app

ENTRYPOINT ./gradlew bootRun
