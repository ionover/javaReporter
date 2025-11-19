FROM azul/zulu-openjdk:21-jre
LABEL maintainer="Ionov <v.ionov@mycrg.ru>"

WORKDIR /app

COPY target/jasperReporte-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "app.jar"]
