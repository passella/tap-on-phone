FROM maven:3.8.7-eclipse-temurin-17-alpine
WORKDIR /app
COPY target/pagamento-1.0.0.jar .
COPY pom.xml .
COPY src ./src
RUN mvn package -Dmaven.test.skip=true
CMD ["java", "-jar", "target/pagamento-1.0.0.jar"]
