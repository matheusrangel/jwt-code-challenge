FROM openjdk:21-slim

# Set the working directory inside the container
WORKDIR /app
COPY target/*.jar /app/jwt-code-challenge.jar

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/jwt-code-challenge.jar"]