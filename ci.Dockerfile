FROM openjdk:8-jdk

ADD build/dist/edustor-storage.jar .

HEALTHCHECK CMD curl -f http://localhost:8080/version
CMD java -jar edustor-storage.jar