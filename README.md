# Sample Micronaut server application

## Note:
PUT - is an idempotent request, thus it's not suitable for partial update
PATCH - is used for partial update

## Build
```bash
./gradlew assemble
```

## Test
```bash
./gradlew test
```

## To run from IDE
For IntelliJ IDEA if you plan to use the IntelliJ compiler then you should enable annotation processing 
under the "Build, Execution, Deployment → Compiler → Annotation Processors" by ticking the "Enable annotation processing" checkbox
https://docs.micronaut.io/latest/guide/index.html#ideSetup

## Quick start docker-compose with Kafka
```bash
# to start
./quick_start.sh up
# to stop
./quick_start.sh down
```

## Run using jar
```bash
java -jar build/libs/stocks-0.1-all.jar
```

## Test coverage
```bash
./gradlew jacocoTestReport
```

## Build and run in docker
```bash
# after running ./gradlew assemble
# to start
docker-compose -f docker-compose.yml up
# to stop
docker-compose -f docker-compose.yml down
# to limit docker CPUs
docker update --cpus 2 micronaut-stocks-service
```

## Hot reload execution during development
```bash
micronaut_io_watch_paths=src/main micronaut_io_watch_restart=true ./gradlew :stocks-service:run --continuous
```

