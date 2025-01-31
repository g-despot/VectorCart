# VectorCart

A vector database and LLM powered e-commerce Java web application

## Quickstart

1. Clone the repository
2. Open the project in your IDE: IntelliJ IDEA (recommended)
    * If you are using IntelliJ IDEA, make sure the IDE opens project as **Maven** and recognizes the project as a Spring Boot project. Also, you must change the working directory of the project so that the views (the actual web pages to be shown) are found by Spring Boot (check out [Web Directories IntelliJ IDEA](#web-directories).
4. Configure the database connection in `application.properties` file (check the [Database](#database) section below for more info)
5. Run the project (by running the `main` method in `VectorCartApplication.java`)
6. Open http://localhost:8081/ in your browser
   * First run the [`basedata.sql`](https://github.com/jaygajera17/E-commerce-project-springBoot/blob/master2/JtProject/basedata.sql) script on the database, you can then log in with the following credentials as admin:
     * Username: `john`
     * Password: `john`
   * Log in as a normal user:
     * Username: `jane`
     * Password: `jane`

### Database

MySQL or MariaDB can be used as the database for this project. The database connection can be configured in the `src/main/resources/application.properties` file, with the appropriate values for the following properties:

```properties
    db.url=jdbc:mysql://[ip address of db]:[port of db]/vector-cart-database
    db.username=[username]
    db.password=[password]
```

### Web Directories

The views are located in `src/main/webapp/views`, but for some reason, Spring Boot doesn't recognize that directory. 
To remedy this, you must change the working directory of the project in your IDE. If you're using IntelliJ IDEA, follow these steps:

1. Click on the "Edit Configurations..." button in the top right corner of the IDE
2. Click on the `VectorCartApplication` configuration
3. Change the "Working directory" option (if not present, click on "Modify Options" and select from the list) to the `$MODULE_WORKING_DIR$` macro
4. Click "Apply" and "OK"

When you run the project, the views should be found by Spring Boot and you should see a login page in http://localhost:8081/ (if not logged in previously)!
![configurations](image.png)

## Endpoints
- http://localhost:8081/