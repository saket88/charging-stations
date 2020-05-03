# Read Me First

The application uses spring boot 2 as a web framework and Java 1.8 . 

The REST API comes with its own swagger configuraiton which can be used to test the API

Please do following steps to run it
1. mvn clean install
2. mvn compile jib:dockerBuild
3. docker run --publish=8080:8080 charging-stations:0.0.1-SNAPSHOT


You can access APIs on 
GET localhost:8080/chargingSessions

It has exclusive Tests which can be used as documentation.
There are Unit Tests for each component like controllers and services