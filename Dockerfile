# Use the base OpenJDK 17 image
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/ReleaseNoteShareSystem-0.0.1-SNAPSHOT.jar docker-springboot.jar

# Define the entry point for the container
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "docker-springboot.jar"]
