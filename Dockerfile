FROM openjdk:8-jdk

WORKDIR /code
ADD . /code/src

RUN cd src && ./gradlew build

RUN mv ./src/build/dist/edustor-storage.jar .

RUN rm -rf src /root/.gradle

HEALTHCHECK CMD curl -f http://localhost:8080/version
CMD java -jar edustor-storage.jar