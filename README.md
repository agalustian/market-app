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

Framework: `Spring core + Spring boot + Spring WebFlux + Spring R2DBC + Spring security`

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
Multimodule concept.

#### Payment module
  - generated via openapi spec (contracts -> openapi -> payment.yaml)
#### Shopfront module
  - controllers - for HTTP requests
  - models - as business model unit
  - services - as business models orchestrator
  - repositories - as layer for database encapsulation
  - resources/templates - view templates

## Usefull commands

Run unit tests: `sh ./gradlew test`

Run integration tests: `sh  ./gradlew integrationTest`

Build jar: `sh  ./gradlew build` - path `build/libs/[payment|shopfront]-0.0.1.jar`

Build Dockerfile (in each module):  `cd ./shopfront && sh ./gradlew build && docker build -t shopfront:latest . && cd ..`

Run shopfront docker container: `docker run -p 8081:8081 shopfront:latest`
Run payment docker container: `docker run -p 8081:8081 shopfront:latest`

Run infra (postgres, redis, keycloak): `sh ./scripts/start-infra`

File loading example: `curl --location 'http://localhost:8081/items/image/4' --form ''`

## Actuator endpoints
Opened actuator endpoints list:
- health
- info
- metrics

Example: `localhost:8081/actuator/metrics`
