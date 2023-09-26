FROM alpine:latest
RUN apk add --no-cache openjdk17
WORKDIR /app
COPY .buid-output/motor-pagamento-1.0.0.jar .
CMD ["java", "-jar", "motor-pagamento-1.0.0.jar"]
