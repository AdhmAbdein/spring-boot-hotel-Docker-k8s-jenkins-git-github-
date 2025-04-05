FROM maven:3.8.4-openjdk-17-slim

WORKDIR /app

COPY mvnw .
#COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests
RUN ls -l target/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

