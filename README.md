# Diff Scalable Web Project

Project that implements three endpoints.
- One to receive a JSON base64 encoded binary data for the left side.
- One to receive a JSON base64 encoded binary data for the right side.
- One that gives a result of the diff between the received JSON.

If you call more than one time the endpoints with the same ID to put the JSON base64, an update will be performed to this ID at the left or right side, depends on what endpoint you are calling.

## Sugestion for improvement
- Create Integration tests with RestAssured and cucumber-jvm in a separated project to write business scenarios;
- Better Log entries;
- Improve the method DiffBusiness.parseAndCompareGenericJson(String rightJson, String leftJson) to go deep inside the values of the JSON tree;
- More Unit tests;

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

You can use for testing if you want these parameters:

* ```id = 1 for both JSON's```
* ```jsonBase64 for the left call = eyJmaXJzZmllbGQiOiAidGVzdEVxdWFsIiwgIm5hbWUiOiJhbm90aGVyIHN1cGVyIGNvb2wgc3RyaW5nIHdpdGggbW9yZSBjb21wbGV4aXR5IiwgImxlZnRGaWVsZCI6ICJ0ZXN0In0=```
* ```jsonBase64 for the right call = eyJmaXJzZmllbGQiOiAidGVzdEVxdWFsIiwgIm5hbWUiOiJqc29uQmFzZTY0IiwgInJpZ2h0RmllbGQiOiAidGVzdCJ9```

## Using Curl
* ```curl -X PUT --header 'Content-Type: application/json' --header 'Accept: */*' -d 'eyJmaXJzZmllbGQiOiAidGVzdEVxdWFsIiwgIm5hbWUiOiJhbm90aGVyIHN1cGVyIGNvb2wgc3RyaW5nIHdpdGggbW9yZSBjb21wbGV4aXR5IiwgImxlZnRGaWVsZCI6ICJ0ZXN0In0=' 'http://localhost:8080/diff/v1/diff/1/left'```
* ``` curl -X PUT --header 'Content-Type: application/json' --header 'Accept: */*' -d 'eyJmaXJzZmllbGQiOiAidGVzdEVxdWFsIiwgIm5hbWUiOiJqc29uQmFzZTY0IiwgInJpZ2h0RmllbGQiOiAidGVzdCJ9' 'http://localhost:8080/diff/v1/diff/1/right'```
* ``` curl -X GET --header 'Accept: application/json' 'http://localhost:8080/diff/v1/diff/1' ```

The Response Body for this get Curl should be

```
{
  "differences": [
    {
      "field": "name",
      "rightSize": 10,
      "leftSize": 46
    },
    {
      "field": "leftField",
      "rightSize": null,
      "leftSize": 4
    },
    {
      "field": "rightField",
      "rightSize": 4,
      "leftSize": null
    }
  ],
  "result": "JSON's provided have different sizes"
}
```

That JSON above shows where the differences are at the JSON tree. The field "name" is present in both JSON's with different sizes, the field "leftField" is present only at the leftJson and the field "rightField" is present only at the rightJson.

* curl -X GET --header 'Accept: application/json' 'http://localhost:8080/diff/version'

## Executing commands inside the container.
If you want to check out the data saved by the diff application at MongoDB database, follow these steps

```
docker ps
```
The result should be like this:
```
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                      NAMES
5c284462230b        diff:latest         "/usr/lib/jvm/java-8-"   3 minutes ago       Up 3 minutes        0.0.0.0:8080->8080/tcp     diff
239af33679e3        mongo:2.6           "/entrypoint.sh mongo"   3 hours ago         Up 3 minutes        0.0.0.0:27017->27017/tcp   diff-mongo
```
Get the CONTAINER ID for the mongo container and execute this:
```
docker exec -it 239af33679e3 /bin/bash
```
To go to the collection and see how it looks at MongoDB, do this:
```
mongo;
show databases;
use diff;
show collections;
db.received_jsons.find().pretty();
```
