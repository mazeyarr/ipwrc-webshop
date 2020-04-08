FROM openjdk:8-jdk

COPY config.yml /data/ipwrc-dropwizard/config.yml
COPY src/main/resources/.env /data/ipwrc-dropwizard/src/main/resources/.env
COPY /target/ipwrc-webshop-1.jar /data/ipwrc-dropwizard/ipwrc-webshop-1.jar
COPY /target/ipwrc-webshop-1.jar /data/ipwrc-dropwizard/ipwrc-webshop-1.jar

WORKDIR /data/ipwrc-dropwizard

RUN java -version

CMD ["java","-jar","ipwrc-webshop-1.jar","server","config.yml"]

EXPOSE 8080-8081
