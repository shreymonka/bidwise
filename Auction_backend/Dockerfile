FROM eclipse-temurin:17.0.11_9-jre-ubi9-minimal

WORKDIR /app

COPY target/auction-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

