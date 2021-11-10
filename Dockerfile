FROM openjdk:11
VOLUME /tmp
COPY /target/orderMS-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8300
ENV JAVA_OPTS=""
RUN sh -c "touch orderMS-0.0.1-SNAPSHOT.jar"
ENTRYPOINT ["java", "-jar", "orderMS-0.0.1-SNAPSHOT.jar"]