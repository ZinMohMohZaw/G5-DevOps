FROM amazoncorretto:25

# Set container working directory
WORKDIR /tmp

# Copy compiled Uber JAR into container image
COPY target/world-population-app.jar app.jar

# Application launch command
ENTRYPOINT ["java", "-jar", "app.jar"]