FROM alpine:3.19.0
RUN apk add --no-cache openjdk17
WORKDIR /app
COPY .buid-output/app.jar .
CMD ["java", "-jar", "app.jar"]
