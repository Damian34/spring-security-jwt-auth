FROM openjdk:22-jdk-slim

WORKDIR /app

COPY ./target/security.jar security.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar", "security.jar"]