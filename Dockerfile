ARG BUILD_HOME=/app

# -*-*- Build stage -*-*-
FROM gradle:8.10-jdk21-alpine as build

ARG BUILD_HOME

ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

RUN gradle --no-daemon build
# -*-*--*-*-

# -*-*- Run stage -*-*-
FROM eclipse-temurin:21-alpine

RUN apk add --no-cache bash && \
    apk add --no-cache rabbitmq-c-utils # binaries to initial application load

ARG BUILD_HOME

ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY scripts $APP_HOME/scripts
COPY --from=build $APP_HOME/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT java -jar app.jar
# -*-*--*-*-