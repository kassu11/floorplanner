# Use an official Maven image as a parent image
FROM maven:latest

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml /app/

# Copy the entire project to the container
COPY . /app/

# Package your application
RUN mvn package

# Install OpenJFX
RUN apt-get update && apt-get install -y openjfx

# Run the main class (assuming your application has a main class)
CMD ["java", "-jar", "target/floorplanner.jar"]