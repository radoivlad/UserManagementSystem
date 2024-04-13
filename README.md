# UserManagementSystem (INSTALLATION GUIDE BELOW)

Hello and welcome to UserManagementSystem! :)

A user management system created with Spring Boot, having a simple, dynamic and user-friendly Thymeleaf/Swagger interface, that can be engaged by web HTTP requests (Postman or Swagger).

This application is my **final project** (due for exam) as part of the Full-Stack Developer Course (IT Factory).

The user management system offers a simple methodology of interacting with 2 databases: Person and Job.

The project is built using Spring Boot; the controller classes contain the REST APIs, having the respective endpoints mapped to handler methods, for each desired operation: GET (extracting certain entries), POST (adding new entries), PUT (updating entries) and DELETE (deleting entries).

A first approach, after running the application, would be to interact with the databases by means of the Thymeleaf simple interface (follow on-screen indications), accessible by browser through the implemented Swagger (OpenAPI) interface, or Postman, at: **http://localhost:8080/umsfinalproject/**.

For adding new entries, deleting entries or updating current entries, Postman can be used with the respective request type (POST, DELETE, and PUT, respectively).

Another approach would be to access the Swagger (OpenAPI) interface (buttons available on the Thymeleaf interface, for each database, 'person' and 'job'), and use the available options for manipulating the 2 databases.

Program is comprised of multiple try-catch blocks, meant to handle and inform on a majority of possible errors that may occur with inputting invalid information or executing error-prone database operations (adding user with already existent id, deleting non-existent entry etc.).

JUnit tests (Integration + Mock tests) are in place for each of the 2 databases, testing each of the Repository, Service and Controller methods, including connection tests and inputting faulty information tests.

Project makes use of a UserManager interface, and PersonManager implementing class to provide additional information about the 'person' database.

The BaseModel class allows for simpler implementation of further models for the databases (by inheritannce).

**Maven dependencies used:** spring-boot-starter-web, spring-boot-devtools, spring-boot-starter-actuator, spring-boot-starter-test, junit-jupiter-api, mockito-core, mysql-connector-j, spring-boot-starter-thymeleaf, springdoc-openapi-starter-webmvc-ui.

# HOW TO INSTALL AND USE THE PROJECT

# a. Open with Intellij, using repository link:

1. Press the green button labelled "Code" (top right corner of the project files) to copy the repository link;
2. Alternatively, you can copy it directly from here: https://github.com/radoivlad/UserManagementSystem.git
3. Open IntelliJ (make sure you have **Intellij Community Edition** installed);
4. Top-left corner: **File -> New -> Project from Version Control**;
5. Select Git as **Version control**;
6. Paste repository link in **URL** field;
7. If **Maven** is not installed -> IntelliJ displays a message to install it, follow instructions provided (Maven will automatically configure the **dependencies** for you);
8. Open the **Main** class and run the ATM-Machine (Shift + F10);

# b. RECOMMENDED: Clone repository to local drive, open with IntelliJ (locally):

1. Navigate to your local drive repository folder (where you would like to have the project cloned);
2. **Right-click -> Open Git Bash here** (make sure you have **Git** installed);
3. Type: **git clone https://github.com/radoivlad/UserManagementSystem.git** (this will create a clone of the project in the current directory);
4. Open IntelliJ;
5. Top-left corner: **File -> Open**;
6. Select the folder we cloned earlier;
7. If **Maven** is not installed -> IntelliJ displays a message to install it, follow instructions provided (Maven will automatically configure the **dependencies** for you);
8. Open the **Main** class and run the ATM-Machine (Shift + F10);

# Finally, have fun && happy database navigating! :D
//really open to any suggestions or improvement ideas, hit me up
