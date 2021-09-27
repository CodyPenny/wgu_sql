# WGU SQL
A GUI-based scheduling desktop application

## Table of contents
* [General info]
* [Technologies]
* [Setup]
* [Features]

## Scenario
This is a scheduling desktop application developed to explore real-world requirements for educational purposes.
It strives to satisfy the requirements presented by an imaginary consulting organization that conducts business in the States, UK, and Canada.
The consulting organization has provided a MySQL database that the application pulls data from and uses Country and First-Level-Division data from a third party. 

## Technologies
* IntelliJ IDEA 2021.1.3 (Community Edition)
* JDK version 16.0.2
* JavaFX version 16
* MySQL 8.0.26

## Setup
Install the JDK, JavaFX SDK, and Scene Builder dependencies and set up the IDE to run Java.
Connect the application to a MySQL database and configure the connection string.

## Features
* Displays the log-in form in English or French based on a user's language setting
* Adds customers to the database
* Create or schedule appointments with customers, contacts, and users
* Alerts if appointments conflict due to overlaps or different time zones
* Alerts if the user has upcoming appointments within 15 minutes of logging in
* Generates reports