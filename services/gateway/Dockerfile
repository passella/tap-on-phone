FROM alpine:latest
RUN apk add --no-cache openjdk17
WORKDIR /app
COPY .buid-output/gateway-1.0.0.jar .
CMD ["java", "-jar", "gateway-1.0.0.jar"]
