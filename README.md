This is a sample OAuth token microservice built using [Helidon SE 4.2.4](https://helidon.io/docs/v4) with Java 21.

The API exposes a `POST /token` endpoint that issues JWT tokens signed using HS256. It demonstrates how to build lightweight, fast HTTP services using Helidon SE.

## Prerequisites

- Java 21 installed and available on `PATH`. You can check with: `java -version`
- Maven 3.6+ installed
- Redis running locally on port 6379

## Prepare Redis with an OAuth client and token expiry config
> HSET oauth_clients my-client-id my-secret
> SET token_expiry 3600

# Build
mvn clean package

# Run
$ java  -jar target/helidon-nima-token-poc-1.0-SNAPSHOT.jar 

You will see output as in below: 

Jul 24, 2025 11:58:33 AM io.helidon.common.features.HelidonFeatures features
INFO: Helidon SE 4.2.4 features: [WebServer]
Jul 24, 2025 11:58:33 AM io.helidon.webserver.ServerListener startIt
INFO: [0x59570c52] http://0.0.0.0:8080 bound for socket '@default'

# Make a token request 
$ curl -X POST http://localhost:8080/token \
  -H "Content-Type: application/json" \
  -d '{"client_id":"my-client-id","client_secret":"my-secret"}'

Response will be something along these lines: 

{"access_token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIi...Q","token_type":"bearer","expires_in":3600
