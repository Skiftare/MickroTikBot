FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ ./src/
RUN mvn clean package

FROM openjdk:22-jdk-slim
WORKDIR /app

COPY --from=build /app/target/MickroTikBot-*.jar /app/MickroTikBot.jar
ENV TELEGRAM_BOT_TOKEN=${TELEGRAM_BOT_TOKEN}
CMD ["java", "-jar", "/app/MickroTikBot.jar"]
