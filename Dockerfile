# Use the official OpenJDK image from the Docker Hub
FROM openjdk:21-jdk-slim

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]