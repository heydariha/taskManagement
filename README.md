# Read Me First
This code challenge is being done to go with Java, Spring boot and Maven.

# Getting Started

### Reference Documentation
Please refer to the following sections for more information:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.7/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.7/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.7/reference/htmlsingle/#web)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.0.7/reference/htmlsingle/#web.servlet.spring-mvc.template-engines)

### Guides
This application can be run standalone or in a Docker environment. The necessary configurations are already provided.

Run Standalone:

- To run the application without Docker, you need to set up a local PostgreSQL database and define a database.
- Set the following keys in the `application.properties` file:
  - `spring.datasource.url`
  - `spring.datasource.username`
  - `spring.datasource.password`

After running the application, you can access the result at `http://localhost:8080`.
A basic route will redirect you to `/tasks`, where you can add, update, or remove tasks.

Run with Docker:
To run the application with Docker, follow these steps:
- Create a database in your PostgreSQL container.
- Run the following command in the root directory of the application:
  `docker-compose up -d`
- Make sure the following keys are correctly set in the `application.properties` file:
  - `spring.datasource.url`
  - `spring.datasource.username`
  - `spring.datasource.password`
### Note
If you need to run this test with postgres in docker, you need to set `spring.datasource.url` to : 
`jdbc:postgresql://db:5432/task_db`



Please note that whenever you make changes to the application, you need to rebuild the Docker image before running the Docker commands again to ensure that the latest changes are applied. 
