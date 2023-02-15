# XMLFileUploadToMySQLDb
Spring Boot application that will allow to upload XML files via REST API, validate them against an XSD schema, parse the data from them and store it in the database.

## Table of Contents
1. [Description](#description)
2. [Local Dev Set Up](#local-dev-set-up)
3. [Testing Guidelines](#testing-guidelines)
4. [Running  Application With Docker](running-application-with-docker)
4. [Testing Application](testing-application)

## Description
XMLFileUploadToMySQLDb is a Spring Boot application REST API that will allow to upload XML files via REST API, validate 
them against an XSD schema, parse the data from them and store it in the database.

## Local Dev Set Up
You should have the following setup beforehand:
* Git
* MYSQL DB
* Docker
* Postman
* (optional) Database GUI
  * MySQL Workbench
* Open the terminal and change to the directory you want to the project to be
* Clone the project using "git clone https://github.com/alhassanmr/XMLFileUploadToMySQLDb.git"
* Navigate to the project directory on your machine
* Make sure to update `application.properties` with have all the appropriate database configurations once your database is configured.
* `mvn install` all dependencies
* `mvn spring-boot:run` to run the application
* 
## Running Application With Docker
* Open your CMD, and run the below on your CMD
* Ensure you are at the root of the project directory
* run command "docker-compose up" in the root folder to start application

## Testing Application
Start your postman on your machine
import the postman collection "andela.postman_collection.json" from project directory.
* With `Postman` `GET` request on this url `http://localhost:8080/api/xml` to retrieve all saved data
* With `Postman` `POST` request on this url `http://localhost:8081/api/xml/upload` with `form-data` body
  file and attached the xml file for upload
