# Cursor Pagination in Spring Boot with Spring Data JPA and PostgreSQL

This is a Gradle project demonstrating cursor-based pagination in a Spring Boot application with Spring Data JPA and PostgreSQL. The project includes a test class named `PaginationApplicationTests` that tests the cursor pagination feature of the application.

## Examples

### Uses default page size
```curl
curl --location 'http://localhosst:8080/customers'
```
#### Response
```json
{
  "content": ["100 Elements"],
  "nextPageCursor": "eyJwYWdlTnVtYmVyIjoxLCJwYWdlU2l6ZSI6MTAwfQ=="
}
```
### Uses custom page size
```curl
curl --location 'http://localhosst:8080/customers?pageSize=10'
```
#### Response
```json
{
  "content": ["10 Elements"],
  "nextPageCursor": "eyJwYWdlTnVtYmVyIjoxLCJwYWdlU2l6ZSI6MTB9"
}
```
### Uses bookmark to previous or next page
```curl
curl --location 'http://localhosst:8080/customers?bookmark=xhfdjskuewffnfbw'
```
#### Response
```json
{
  "content": ["10 Elements"],
  "previousPageCursor": "eyJwYWdlTnVtYmVyIjowLCJwYWdlU2l6ZSI6MTB9",
  "nextPageCursor": "eyJwYWdlTnVtYmVyIjoyLCJwYWdlU2l6ZSI6MTB9"
}
```
## Dependencies

The project uses the following dependencies:

- [Lombok](https://projectlombok.org/): Provides code simplification and boilerplate reduction for Java classes.
    - Dependency: `org.projectlombok:lombok`

- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/): Enables connecting to a PostgreSQL database.
    - Dependency: `org.postgresql:postgresql`

- [Spring Boot Starter Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-jpa-and-spring-data): Provides Spring Data JPA support for the application.
    - Dependency: `org.springframework.boot:spring-boot-starter-data-jpa`

- [Spring Boot Starter Web](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-developing-web-applications): Provides Spring Web support for building web applications.
    - Dependency: `org.springframework.boot:spring-boot-starter-web`

- [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/): A library of utilities for working with strings and other objects.
    - Dependency: `org.apache.commons:commons-lang3:3.13.0`

- [Spring Boot Starter Test](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing): Provides support for testing Spring Boot applications.
    - Dependency: `org.springframework.boot:spring-boot-starter-test`

- [Spring Boot Testcontainers](https://github.com/testcontainers/testcontainers-spring-boot): Provides support for running test containers within a Spring Boot test environment.
    - Dependency: `org.springframework.boot:spring-boot-testcontainers`

- [JUnit Jupiter](https://junit.org/junit5/): The testing framework for writing and executing tests.
    - Dependency: `org.testcontainers:junit-jupiter`

- [Testcontainers PostgreSQL Module](https://www.testcontainers.org/modules/databases/postgres/): Provides support for running PostgreSQL containers in tests.
    - Dependency: `org.testcontainers:postgresql`

## Test Class: PaginationApplicationTests

The `PaginationApplicationTests` class contains test methods to validate the cursor pagination feature of the application. It uses a PostgreSQL container for integration testing.

- `@BeforeAll`: Starts a PostgreSQL Docker container before all tests to provide a database for testing.

- `@AfterAll`: Stops the PostgreSQL Docker container after all tests have been executed.

- `@BeforeEach`: Executed before each test to ensure that the database is clean and populated with test data.

- `@DynamicPropertySource`: Configures the Spring datasource properties to use the PostgreSQL container's connection details for testing.

- `getAllCustomers`: A test method that checks the functionality of retrieving customers using cursor-based pagination. It tests different scenarios, including retrieving the first page of customers, custom page sizes, and using "nextPageCursor" and "previousPageCursor."

- `getBaseUrl`: A helper method that returns the base URL for making HTTP requests to the application.

- `addAllCustomersToDatabase`: A helper method that populates the database with a specified number of customer records for testing.

## Running the Tests

To run the tests in this Gradle project:

1. Ensure you have Docker installed on your system.

2. Open a terminal and navigate to the project directory.

3. Run the following Gradle command to clean the project:
   ```shell
   ./gradlew clean
   ```

4. Run the following Gradle command to run the tests:
   ```shell
   ./gradlew test
   ```

On Windows, replace `./gradlew` with `gradlew.bat`.

Make sure you are in the project directory when executing these commands.

Feel free to modify and extend the `PaginationApplicationTests` class to cover additional test scenarios as needed for your application's cursor pagination functionality.