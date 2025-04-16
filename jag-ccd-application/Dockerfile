FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk add --upgrade --no-cache libexpat

COPY ./target/ccd-application.jar ccd-application.jar

ENTRYPOINT ["java","-Xmx2g", "-jar","/ccd-application.jar"]
