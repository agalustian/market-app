## Awesome market
Search items, add to cart and buy it.

- [Tech stack](#Tech-stack)
- [Known issues](#Known-issues)
- [Rules for contributors](#Rules-for-contributors)
- [Architecture](#Architecture)
    - [Future concerns](#Future-concerns)
- [Project structure](#Project-structure)
- [Useful commands](#Usefull-commands)

## Tech stack
Language: `Java 21`

Framework: `Spring core + Spring boot + Spring WebFlux + Spring R2DBC`

Test tools: `Spring boot test \ JUNIT 5 \ Jupiter \ Mockito \ TestContainers`

Build tools: `Gradle`

HTML templates: `Thymeleaf`

Observability: `Actuator`

## Rules for contributors:
We're using:
- conventional-commits;
- folder structure convention;
- avoiding GOD files.

We're respecting:
- SOLID;
- DRY;
- KISS;
- YAGNI;
- and other good principles.

## Architecture
Project use `MVC` architecture

### Future concerns:
- Moving to `SPA` instead of templates.

## Project structure
- controllers - for HTTP requests
- models - as business model unit
- services - as business models orchestrator
- repositories - as layer for database encapsulation
- resources/templates - view templates

## Usefull commands

Run unit tests: `./gradlew test`

Run integration tests: `./gradlew integrationTest`

Build jar: `./gradlew build` - path `build/libs/market-0.0.1.jar`

Build Dockerfile:  `sh ./gradlew build && docker build -t market:test .`

Run docker container: `docker run -p 8080:8080 market:test`

Run postgres: `docker compose up -d`

File loading example: `curl --location 'http://localhost:8080/items/image/4' --form ''`

## Actuator endpoints
Opened actuator endpoints list:
- health
- info
- metrics

Example: `localhost:8080/actuator/metrics`
