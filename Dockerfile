FROM gradle:8.10-jdk21 as build
WORKDIR /app
COPY . .
RUN gradle build

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
