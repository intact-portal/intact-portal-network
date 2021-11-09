FROM openjdk:8u111-jdk-alpine
VOLUME /tmp
ADD /target/intact-network-ws-1.0.1-SNAPSHOT.jar intact-network-ws.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/intact-network-ws.jar"]