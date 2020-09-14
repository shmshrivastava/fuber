FROM openjdk:8-alpine

COPY target/uberjar/fuber.jar /fuber/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/fuber/app.jar"]
