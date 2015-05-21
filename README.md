<snippet>
  <content>
## Introduction

The purpose of this project is to deploy a DropWizad application using AWS Elastic BeanStalk as a Docker image.  
Please follow the [Wiki](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/wiki) which gives an overall AWS deployment architecture and step by step configuration steps needed.  

## Overview

A simple Dropwizard application `dropwizad-app` integrated with swagger is intendent to deployed in AWS Elastic BeanStalk as a docker image.  
This application is going to deploy in a `public subnet` which resides in a `private VPC`, this utilize a `MySql RDS instance` deployed in a `private subnet` which reside in a `private VPC`.  
Step by Step AWS environment configurations (VPC, Subnet, Internet Gateway, Rout Table, Security Groups ,Subnet Groups,RDS,EC2) explain in the [Wiki AWS Configuration Section](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/wiki/AWS-Environment-configuration-steps)   
Step by step AWS Elastic beanstalk explain in the [Wiki ELB Deployment Section](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/wiki/Deploy-the-Docker-image-using-AWS-Elastic-BeanStalk.).

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

###Steps to follow###
 1. Configure the AWS deployment environment (Follow the instructions in the [Wiki](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/wiki/AWS-Environment-configuration-steps))
 2. Create a zip file to Deploy in Elastic BeanStalk (This explains in the following section)
 3. Deploy the zip file in Elastic BeanStalk as a docker image (Follow the instructions in the [Wiki](https://github.com/muditha-silva/DropWizard-AWS-Elastic-BeanStalk/wiki/Deploy-the-Docker-image-using-AWS-Elastic-BeanStalk.))

###Create a zip file to Deploy in Elastic BeanStalk###

In the `dropwizad-app/container` following fils exists `config.yml`,`Dockerfile`,`dropwizad-app-1.0-SNAPSHOT.jar` 

Create a zip file `dropwizad-app-1.1.zip` by composing `config.yml`+`Dockerfile`+`dropwizad-app-1.0-SNAPSHOT.jar`  
This zip file is used for the Elastic BeanStalk deployment.  
(This file `dropwizad-app-1.1.zip` is already exists in the `dropwizad-app/container`.)

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
  
2. **Docker file**  
```
FROM dgageot/java8

ADD dropwizad-app-1.0-SNAPSHOT.jar dropwizad-app-1.0-SNAPSHOT.jar

ADD config.yml config.yml

CMD java -jar dropwizad-app-1.0-SNAPSHOT.jar server config.yml

EXPOSE 8080
```  
3. **dropwizad-app-1.0-SNAPSHOT.jar** 

This DropWizard jar file generates once you run the `mvn package` command.  
By default this file will be located at `dropwizad-app/target`  
This file also avilable in `dropwizad-app/container`

## Credits

The sample code was taken and modified from the Book `RESTful Web Services with Dropwizard Alexandros Dallas`.  
`DropWizard Swagger integration` [federecio/dropwizard-swagger](https://github.com/federecio/dropwizard-swagger)  

</content>
  <tabTrigger></tabTrigger>
</snippet>
