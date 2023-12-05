FROM openjdk:11-jdk-stretch
VOLUME /tmp
ADD /target/intact-network-ws-1.0.2.jar intact-network-ws.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/intact-network-ws.jar"]