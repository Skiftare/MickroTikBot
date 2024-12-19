FROM openjdk:21
WORKDIR /app
COPY BotService/target/BotService-0.1-jar-with-dependencies.jar /app/BotService.jar
CMD ["java", "-jar", "/app/BotService.jar"]
