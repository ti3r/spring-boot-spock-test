==== Spring Boot Spock Test ====

This project is a simple project to demonstrate how to integrate Spock testing framework into a spring boot application.

The project is a gradle project with mixed nature for Java (where the application and controllers are developed) and
Groovy (used to run the spock tests).


In order to build the project just run gradle clean build and check the test reports in build/reports.


You can open the project in Eclipse using gradle integration and review the source code.

App source code is inside src/main/java -> Simple spring boot application with a rest controller

Test source code is inside src/test/groovy -> Simple spock specification to run several test scenarios

