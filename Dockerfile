FROM openjdk:17.0.2-jdk-oracle
COPY target/client-storage-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3002
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

