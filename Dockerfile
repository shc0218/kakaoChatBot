FROM azul/zulu-openjdk:22
VOLUME /tmp
ENTRYPOINT ["java","-jar","target/app.jar"]