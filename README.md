# General Information
* Build and test the app: `./gradlew clean build`
* Run app: `./gradlew bootRun`
* REST APIs definition (OpenAPI): http://localhost:8080/v3/api-docs
* Swagger: http://localhost:8080/swagger-ui/index.html
* H2 console: http://localhost:8080/h2-console (default username and password; url: `jdbc:h2:mem:testdb`)


# What I would've done if I had had a bit more time
- Decouple libraries (dto-based library, app-based library). It will allow smoothly integrate with other microservices
- using feign
- cache
- more layers
- multiple subscriptions (to see the history) / subscriptions history
- Set IDs on my own (instead of lettting spring and DB do that)
- More detailed exceptions
- Add a table for users
- Multiple different subscriptions
- Using env vars to inject passwords and usernames and urls
- Better exception handling
- Profiles
- Custom media types for return values
- Create a common swagger config