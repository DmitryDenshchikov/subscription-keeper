## General Information
* Build and test the app: `./gradlew clean build`
* Run app: `./gradlew bootRun`
* REST APIs definition (OpenAPI): http://localhost:8080/v3/api-docs
* Swagger: http://localhost:8080/swagger-ui/index.html
* H2 console: http://localhost:8080/h2-console (default username and password; url: `jdbc:h2:mem:testdb`)
* Build a docker image: `docker build --tag subscription-keeper-dmitry-denshchikov .`
* Run the docker image: `docker run -p 8080:8080 --name subscription-keeper-dmitry-denshchikov subscription-keeper-dmitry-denshchikov`
