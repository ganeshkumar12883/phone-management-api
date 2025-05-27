FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/phone-management-api-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]