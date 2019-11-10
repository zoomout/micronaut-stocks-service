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