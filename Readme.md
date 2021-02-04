# Assignment

<b>Assignment is a test automation project being submitted for interview process</b>

## Folders:
1. src/test/java/com/assignment/app/ApiTest.java contains the api tests
2. src/test/java/com/assignment/app/UiTest.java contains the api tests   
3. src/test/java/com/assignment/app/TestListener.java is a TestNGListener file with a basic implementation for reports.
4. src/main/java/com/assignment/app/pageobjectfactory folder contains page object files
5. src/main/java/com/assignment/app/utilities folder contains the following files
    1. AutomationReport.java - simple extent report config
    2. NavigationUtils.java - methods containing repetitive navigation
    3. OtherUtils.java - reusable methods containing repetitive user actions
    4. WebManager.java - method to initialize WebDriver
6. testng.xml - testng xml file to control tests
7. reports/failure-screenshot - A screenshot .png file is created for each failing test for every test run
8. reports/HTMLReport/failureReport.html - Extent Reports capturing only failures
9. reports/HTMLReport/report.html - Extent Reports capturing all test cases

## Requirements:
1. Maven - [Maven Download](https://maven.apache.org/download.cgi)
   All dependencies required for running the project are packaged in the pom
2. Java 1.8 - language set to create the project

MAVEN_HOME, JAVA_HOME and path variables have to be set

## Maven commands to run
    mvn test

This command runs the testNg file in the pom.xml