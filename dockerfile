FROM jelastic/maven:3.9.5-openjdk-22.ea-b19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk-alpine
COPY --from=build /target/scalevote-0.0.1-SNAPSHOT.jar scalevote.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod","scalevote.jar"]