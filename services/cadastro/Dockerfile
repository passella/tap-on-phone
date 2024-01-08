FROM alpine:3.19.0
RUN apk add --no-cache openjdk17
WORKDIR /app
COPY .buid-output/cadastro-1.0.0.jar .
CMD ["java", "-jar", "cadastro-1.0.0.jar"]
