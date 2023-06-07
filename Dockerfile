FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/task-management-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
