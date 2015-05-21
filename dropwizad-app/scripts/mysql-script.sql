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