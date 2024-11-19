# Use a JDK image as the base image
FROM amazoncorretto:21.0.4

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/runners-util-java-backend-0.0.2-0.0.1-SNAPSHOT.jar /app/runners-util-java-backend-0.0.2-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "runners-util-java-backend-0.0.2-0.0.1-SNAPSHOT.jar"]