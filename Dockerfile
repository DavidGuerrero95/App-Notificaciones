FROM openjdk:12
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Notificaciones.jar
ENTRYPOINT ["java","-jar","/Notificaciones.jar"]