<snippet>
  <content>
## Introduction

The purpose of this project is to deploy a DropWizad application using AWS Elastic BeanStalk as a Docker image. 


## Overview

A simple Dropwizard application "dropwizad-app" was developed. This application is integrated with swagger.  
Step by Step AWS environment configurations and Elastic BeanStalk deployment has been explained in the [Wiki](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/wiki)

## Running The Application locally

In the local development environment install and configure 

  1. Maven3
  2. Java 8
  3. MySql Server


Run the following MySql script `dropwizad-app/scripts` using MySql root user.
 
```
CREATE DATABASE `phonebook`;

CREATE USER 'phonebookuser'@'%' IDENTIFIED BY 'phonebookpassword';
GRANT ALL ON phonebook.* TO 'phonebookuser'@'%';

USE `phonebook`;

CREATE TABLE IF NOT EXISTS `contact` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `firstName` varchar(255) NOT NULL,
 `lastName` varchar(255) NOT NULL,
 `phone` varchar(30) NOT NULL,
 PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
AUTO_INCREMENT=1 ;

INSERT INTO `contact` VALUES (NULL, 'xxx', 'yyy','+123456789'), (NULL, 'aaa', 'bbb', '+987654321');
```
YML changes `dropwizad-app/config.yml` change the appropriate entries

```
database:
  driverClass: com.mysql.jdbc.Driver
  user: phonebookuser
  password: phonebookpassword
  url: jdbc:mysql://localhost/phonebook
host: localhost
port: 8080
```

Run following commands from the project root folder `dropwizad-app`

To package the application `mvn package`

To run the application `java -jar target/dropwizad-app-1.0-SNAPSHOT.jar server config.yml`

To test the application `http://localhost:8080/swagger` this will expose the Swagger UI 

![](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/blob/master/images/swagger1.jpg)

Click `List Operations` using the swagger UI test the API operations

![](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/blob/master/images/swagger2.jpg)

## Deploying the application in the Elastic BeanStalk as a Docker image 

Step by step AWS environment configuration and Elastic BeanStalk deployment explains in the [Wiki](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/wiki)

###Create a zip file to Deploy in Elastic BeanStalk###

In the `dropwizad-app/container` following fils exists `config.yml`,`Dockerfile`,`dropwizad-app-1.0-SNAPSHOT.jar` 

Create a zip file `dropwizad-app-1.1.zip` by composing `config.yml`+`Dockerfile`+`dropwizad-app-1.0-SNAPSHOT.jar`  
This zip file is used for the Elastic BeanStalk deployment.

1. **config.yml** This configuration file mainly consists of MySql DB configuration properties `user`,`password`,`url`,`host`
and swagger configuration properties `host` and `port`  
  1.1. **user:** MySql user created by the sql script.  
  1.2. **password:** MySql password created by the sql script.  
  1.3. **url** For the host name we are using the Route53 private hosted zone CNAME entry `mysql.local`.  
  1.4. **host** This is the Elastic BeanStalk host name. Depending on the Elastic BeanStalk deployment URL change this entry.
        This is used by Swagger  
  1.5. **port** This is used by swagger.  
  
  ```
 database:
  driverClass: com.mysql.jdbc.Driver
  user: phonebookuser
  password: phonebookpassword
  url: jdbc:mysql://mysql.local/phonebook?user=phonebookuser&password=phonebookpassword
host: microservicerocks-env.elasticbeanstalk.com
port: 80
  ```
  
2.  **Docker file**

```
FROM dgageot/java8

ADD dwbook-phonebook-1.0-SNAPSHOT.jar dwbook-phonebook-1.0-SNAPSHOT.jar

ADD config.yml config.yml

CMD java -jar dwbook-phonebook-1.0-SNAPSHOT.jar server config.yml

EXPOSE 8080
```

## Credits

The sample code was taken and modified from the Book `RESTful Web Services with Dropwizard Alexandros Dallas`.  
`Swagger` [federecio/dropwizard-swagger](https://github.com/federecio/dropwizard-swagger)  

</content>
  <tabTrigger></tabTrigger>
</snippet>
