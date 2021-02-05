FROM openjdk:8u111-jdk-alpine
VOLUME /tmp
ADD /target/intact-network-ws-0.0.1-SNAPSHOT.jar intact-network-ws.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dhttps.proxySet=true -Dhttp.proxySet=true -Dhttp.proxyHost=hx-wwwcache.ebi.ac.uk -Dhttp.proxyPort=3128 -Dhttps.proxyHost=hx-wwwcache.ebi.ac.uk -Dhttps.proxyPort=3128","-jar","/intact-network-ws.jar"]