# Nibodha Integration Platform (NIP)#
NIP provides a platform to build different integration services.


##Prerequisites##
* JDK 1.8
* Maven 3.3

##Technology Stack##


* Spring Framework 4.2.3.RELEASE
* Spring Boot 1.3.0.RELEASE
* Spring Batch 3.0.5.RELEASE
* Apache Camel 2.16.1
* Spring Data 1.11.1.RELEASE

## Architecture ##

 Nibodha Integration Platform(NIP) has a layered architecture that consists of
 
* Kernel Layer
* Services layer
* Application Layer 

### Kernel Layer ###

The NIP kernel layer is based on Spring Boot that provides an embedded Jetty Servlet Container into which we can deploy the applications. The kernel layer provides the following features:

* Deployment
* Logging
* JMX Server
* Configuration
* Security

### Services Layer ###

The NIP services layer consists of embedded services which interact with the application layer to communicate with the applications built on top of the NIP. The services layer provides:

#### Transaction Manager ####

TODO: Tool choice for XA transaction manager (JBoss TS/Atomikos/Bitronix)

#### Messaging ####

The messaging service is based on Apache Active MQ which allows the developer to create JMS Message Brokers and clients and deploy them.

#### Routing/Integration engine ####

The routing/integration engine based on Apache Camel allows the developer to define routes and implement enterprise integration patterns(EIP) and deploy the routes into the camel context.
            
#### Transformation Engine ####

TODO: Add Info

#### Auditing ####

TODO: Add Info

#### ETL Engine ####

TODO: Add Info

### Application Layer ###

TODO: Add Info

## Road Map ##

TODO: Add Info

##Project Structure##

The integration platform is a multi module maven project with the following modules

* core - provides the implementation for the services and kernel layers in the architecture layers.
* launcher - provides services to launch the integration platform
* configuration -  contains the platform configuration files

##License##

The Nibodha Integration Platform is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0)

##FAQs##

1. How to set up development environment?

	a. Fork the integration-platform project to the developers gitlab account.
	
	b. Clone the integration platform projects to developer's machine.
	
	c. copy the settings.xml from configuration/etc folder to the maven local repository folder usually <user.home>/.m2
	
	For windows C:\users\<username>\.m2
	For linux/mac ~/.m2

2. How to build and run the platform?
   
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
	http://localhost:8081, enter admin/admin to login.