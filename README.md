An integration platform which supports both data integration and application integration, built using Spring Boot.

##Development Environment##
###Prerequisites###
* JDK 1.8
* Maven 3.3

###Technology Stack###
* Spring Framework 4.2.3.RELEASE
* Spring Boot 1.3.0.RELEASE
* Spring Batch 3.0.5.RELEASE
* Apache Camel 2.16.1
* Spring Data 1.11.1.RELEASE

###Project Structure###

###FAQs###
1. How to build and run the platform?
   
   Execute the following commands in terminal/command prompt
   
   Build the integration-platform
   
   `mvn clean install`
   
   Change to launcher directory
   
   `cd launcher`
   
   Start the integration platform
   
   `mvn spring-boot:run`
2. How to start the monitoring console?

	If the integration platform is not built, build the integration platform using maven.
	
	Change to monitoring console directory.

	`cd launcher`
	
	Start the monitoring cosole
	
	`mvn spring-boot:run`
	
	Once the monitoring console is up and running, open bowser and got to 
	http://localhost:8081, use admin/admin to login.
	   
