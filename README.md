# Sample Micronaut server application

## Note:
PUT - is an idempotent request, thus it's not suitable for partial update
PATCH - is used for partial update

## Scope
What is done:
- implementation of create, get, get all, update price endpoints using Micronaut framework
- api tests
- test coverage report
- docker deployment
- simple UI to display all stocks (using Thymeleaf), available at root (e.g. http://localhost:8080/)
- swagger api documentation

What is not done:
- authentication
- unit tests of service layer (balanced by extensive coverage of rest APIs)
- unit tests of repository layer is not done because the repository is a fake implementation in memory
- logging should be improved (tracing, configurable log levels)

## Build
```bash
./gradlew assemble
```

## Test
```bash
./gradlew test
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
micronaut_io_watch_paths=src/main micronaut_io_watch_restart=true ./gradlew run --continuous
```