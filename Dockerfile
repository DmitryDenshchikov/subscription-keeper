FROM adoptopenjdk/openjdk8:ubi
EXPOSE 8080
ARG JAR_FILE=build/libs/subscription-keeper-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]