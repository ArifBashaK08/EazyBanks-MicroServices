FROM openjdk:17-jdk-slim

MAINTAINER Arif_Basha_K
LABEL maintainer="Arif_Basha_K"

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#command to run the jar in container
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]