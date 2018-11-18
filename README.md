# Calcard

A simple credit analysis app. Provides a service that receives credit proposals and returns a rejection reason or aproved credit limits.

The supplied test data is included in the unit tests.

Swagger documentation can be found in the administration menu after login. Default user is admin/admin. 

Update of credit proposals is deliberately blocked, in case of mistakes the user should create a new proposal.

It uses Spring Boot for the back end and React with bootstrap for the front end. 

This application was generated using JHipster 5.7.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v5.7.0](https://www.jhipster.tech/documentation-archive/v5.7.0).

There is a LOT of generated boiler plate. The actual developed code can be found in:

```
src/main/webapp/app/modules/home/**
src/test/java/com/bisol/calcard/web/rest/CreditProposalResourceIntTest.java
src/main/java/com/bisol/calcard/service/CreditProposalService.java
src/main/java/com/bisol/calcard/repository/CreditProposalRepository.java
```

## Development

To start your application in the dev profile, with an H2 database, simply run:

```
./mvnw    
```

## Docker

To run it with Docker, a docker-compose file is provided which will also launch a mariadb instance. Just run:

```
./mvnw package -Pprod jib:dockerBuild
docker-compose -f src/main/docker/app.yml up
```

## Testing

To launch your application's tests, run:

    ./gradlew test

For more information, refer to the [Running tests page][].