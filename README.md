## General Information
* Build and test the app: `./gradlew clean build`
* Run app: `./gradlew bootRun`
* REST APIs definition (OpenAPI): http://localhost:8080/v3/api-docs
* Swagger: http://localhost:8080/swagger-ui/index.html
* H2 console: http://localhost:8080/h2-console (default username and password; url: `jdbc:h2:mem:testdb`)


## What I would've done if I had had more time
- Add a REST API endpoint to list all users that subscribed before and/or after a given date (Service already stores subscription start and end dates, so it's not a big deal to implement that). 
- Support subscriptions history. If user was unsubscribed at some point and then he subscribed again, it should be a new subscription record. This will improve data and allow to develop more related features in the future.
- Divide project into two submodules: dto-based and app-based. It improves flexibility and allow smoothly integrate if you use feign for service to service communication.
- More detailed exceptions (and more granular exception handler)
- Add a table for users (store users as well)
- Using environment variables to inject passwords, usernames and urls
- Add spring's profiles usage
- Create a common swagger config (declare a common api response for error responses)