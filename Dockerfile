FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY build/libs/\*.jar app.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:50005,server=y,suspend=n","-jar","/app.jar"]
EXPOSE 8082
EXPOSE 50005