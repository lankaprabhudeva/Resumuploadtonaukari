# Use Maven with JDK 17
FROM maven:3.9.4-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy project files into the container
COPY . .

# Run Maven clean and test
CMD ["mvn", "clean", "test"]
