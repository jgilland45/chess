FROM gradle:9.4.1-jdk21 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle gradlew gradlew.bat settings.gradle build.gradle ./
COPY --chown=gradle:gradle gradle/wrapper/ gradle/wrapper/
RUN chmod +x gradlew || true
RUN --mount=type=cache,target=/home/gradle/.gradle ./gradlew --no-daemon dependencies
COPY --chown=gradle:gradle src/ src/
RUN --mount=type=cache,target=/home/gradle/.gradle ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
