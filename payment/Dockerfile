FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY ./build/libs/payment-0.0.1.jar /app/payment-0.0.1.jar

EXPOSE 8090
CMD ["java", "-jar", "payment-0.0.1.jar"]
