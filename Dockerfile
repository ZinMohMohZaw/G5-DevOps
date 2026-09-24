# Base Image: Amazon Corretto 25
FROM amazoncorretto:25

# Set container working directory
WORKDIR /tmp

# Copy compiled Uber JAR into container image
COPY target/SET09803-DevOps-Project-1.0-SNAPSHOT.jar app.jar

# Application launch command
ENTRYPOINT ["java", "-jar", "app.jar"]
