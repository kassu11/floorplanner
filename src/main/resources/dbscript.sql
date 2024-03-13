CREATE DATABASE IF NOT EXISTS `floorplanner`;
USE `floorplanner`;

DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY '123';
GRANT SELECT, INSERT, DROP, UPDATE, DELETE, CREATE ON floorplanner.* TO 'appuser'@'localhost';