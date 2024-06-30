FROM jelastic/maven:3.9.4-openjdk-22.ea-b17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk-alpine
COPY --from=build /target/ecs-0.0.1-SNAPSHOT.jar ecs.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","ecs.jar"]