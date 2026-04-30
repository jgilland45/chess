FROM gradle:9.4.1-jdk21 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN chmod +x gradlew || true
RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
