# tree-quest
## Decisions


To make it easy to test the tree merge algorithm I had decided to create some endpoints using rest API.

* Add initial tree
* Add new tree
* Merge trees
* Clean trees

I also decide to use JAVA 11 in this project. So it is necessary install it to run from your local machine. 

## Assumptions
* We will not deal with so big json files, then I did a real-time merge endpoint
* I am assuming that we don't need future retrieve in that data, for this reason, I am not using any kind of storage (database)
* The ROOT node is always the same in initial and new trees. FOr this reason, I am not merging it.


## To build and run: 

* To build the project, and run the tests, execute in the project root folder the following argument   

```./mvn clean install ```

* To execute the following argument to run the project:

``` ./mvnw spring-boot:run ```

To test the application using the **Swagger** open the following url in your web browser: 
```
http://localhost:8080/swagger-ui.html
```

Using the Swagger user interface it will be possible to test the full application, *Upload the initial tree*, *upload the new tree* and *merge the trees*

## Deployment 

I created a kubernates deployment and deploy the application in Google Gloud Platform. (Google Kubernetes Engine). If you want to check, this is the URL: 

```
http://34.91.92.238/swagger-ui.html
```

## Stack of technologies: 

* JAVA 11
* Spring Boot 2.3.11
* Swagger 2.9.2 
* lombok 
* Maven 3.6.3 
* Junit 5.6.2


Copyright (C) 2001 - 2020 by Marktplaats BV an Ebay company. All rights reserved.
