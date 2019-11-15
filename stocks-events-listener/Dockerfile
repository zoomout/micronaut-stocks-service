FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY stocks-events-listener/build/libs/stocks-events-listener-*-all.jar stocks-events-listener.jar
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar stocks-events-listener.jar