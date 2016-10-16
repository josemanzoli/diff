# Diff Scalable Web Project

Project that implements three endpoints.
- One to receive a JSON base64 encoded binary data for the left side.
- One to receive a JSON base64 encoded binary data for the right side.
- One that gives a result of the diff between the received JSON.
If you call more than one time the endpoints with the same ID to put the JSON base64 an update will be performed to this ID at the left or right side, depends on what endpoint you are calling.

## Requirements

* [Java8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven] (https://maven.apache.org/)

#### Mac Os

* [Docker](https://docs.docker.com/docker-for-mac/)

#### Linux

* [Docker](https://docs.docker.com/engine/installation/linux/ubuntulinux/)

### Development

 * Some IDE of your choice *[Eclipse] (http://www.eclipse.org/home/index.php) *[Netbeans] (https://netbeans.org/) *[IntelliJ IDEA] (https://www.jetbrains.com/idea/)

## Installation

### Docker

##### Setup

```
cp .env.example .env
mvn clean install
docker-compose up --build
```
## Swagger interface

After the start of the diff docker container, go to localhost:8080 and your browser will redirect to the Swagger UI for you to try it out all the endpoints.

## Using Curl
* curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' -d 'eyJuYW1lIjogImJsYWJsYSJ9' 'http://localhost:8080/diff/v1/diff/1/right'
* curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' -d 'eyJuYW1lIjogImJsYWJsYSJ9' 'http://localhost:8080/diff/v1/diff/1/left'
* curl -X GET --header 'Accept: text/plain' 'http://localhost:8080/diff/v1/diff/1'


