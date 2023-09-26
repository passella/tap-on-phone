FROM alpine:latest
RUN apk add --no-cache openjdk17
WORKDIR /app
COPY .buid-output/operacao-pagamento-1.0.0.jar .
CMD ["java", "-jar", "operacao-pagamento-1.0.0.jar"]
