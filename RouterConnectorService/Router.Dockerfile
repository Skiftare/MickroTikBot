FROM openjdk:21
WORKDIR /app
COPY RouterConnectorService/target/RouterConnectorService-0.1-jar-with-dependencies.jar /app/RouterConnectorService.jar
EXPOSE 8090
CMD ["java", "-jar", "/app/RouterConnectorService.jar"]
