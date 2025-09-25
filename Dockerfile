FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY build/libs/market-0.0.1.jar /app/market-0.0.1.jar

EXPOSE 8090
CMD ["java", "-jar", "market-0.0.1.jar"]
