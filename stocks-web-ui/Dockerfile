FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY stocks-web-ui/build/libs/stocks-web-ui-*-all.jar stocks-web-ui.jar
EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar stocks-web-ui.jar