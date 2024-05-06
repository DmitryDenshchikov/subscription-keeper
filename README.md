## General Information
* Build and test the app: `./gradlew clean build`
* Run app: `./gradlew bootRun`
* REST APIs definition (OpenAPI): http://localhost:8080/v3/api-docs
* Swagger: http://localhost:8080/swagger-ui/index.html
* H2 console: http://localhost:8080/h2-console (default username and password; url: `jdbc:h2:mem:testdb`)
* Build a docker image: `docker build --tag subscription-keeper-dmitry-denshchikov .`
* Run the docker image: `docker run -p 8080:8080 --name subscription-keeper-dmitry-denshchikov subscription-keeper-dmitry-denshchikov`


## What I would've done if I had had more time
- Secure the endpoints by implementing at least JWT authentication and authorisation.
- Add a REST API endpoint to list all users that subscribed before and/or after a given date (Service already stores subscription start and end dates, so it's not a big deal to implement that). 
- Support subscriptions history. If user had been unsubscribed at some point and then he was subscribed again, it should be a new subscription record. This will improve data and allow to develop more related features in the future.
- Divide project into two submodules: dto-based and app-based. It improves flexibility and allows to smoothly integrate if you use feign for service to service communication.
- Add custom exceptions where it would be more suitable (and add more granular exception handler)
- Add a table for users (store users as well)
- Use environment variables to inject passwords, usernames and urls
- User spring's profiles where it would be suitable
- Create a common swagger config (declare a common api response for error responses)
- Implement database migrations through either liquibase or flyway