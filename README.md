# Sample Micronaut server application

# Note:
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