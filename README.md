# awesome-broker

Awesome broker is a simple message communication broker that will allow users to send and receive messages.
Users can get do a quick registration process to sign up into the platform, then they are ready to go to start communicating with all the platform users.

## Users
Users are required to sign up providing a unique alphanumeric identifier with a few special characters allowed like "-", "_".

User model 
````
User
    + username
````


## Messages 
Messages can be any alphanumeric text string be sent into the message delivery request, the message delivery request requires receiving 3 main parameters, 
from, to and message that will allow to track the messages sent and received.

````
Message
    + from
    + to
    + message
````

## Next Features

### Authentication
This is the first proof of concept for this API we want to allow everyone to interact and use the messaging app, so we expect anybody can use it without requiring special authentication requirements. 

In the future users will be required to proof they are authorized actors to interact with it using `jwt` tokens with oauth model.

### Users management
This application holds soft delete for audit purposes, so we expect in the future we can deactivate inactive users and archive the data.

### Session groups
This application will deliver the messages even if users doesn't exist of course it will send messages to DL but that's fine TEXT message doesn't validate receiver
before sending the message, but this can be improved from consumers creating a session where a chat session checks if users exists prior starting the message exchange.

### Performance 
Allow resources to be paginated default support is enabled in JPA in repositories

### Asynchronous Handling
RabbitMQ is a message broker to process this messages, what should be implemented next: 
- Exponential Backoff
- Better Error Handling
- Consumers may want to use a chat session (websockets) to pull messages processed withing the session

### CI/CD 
The project included a test dependencies.yml file to start up the dependencies and run the test inside a pipeline 
Jenkins, Travis, Bamboo can be a good fit for it, pipeline must deploy the docker image to the docker registry repository.

### Deployment check dependencies 

This project needs to be improved to run in different steps like check dependencies if not run the dependencies first
and then run the application. To simplify the process yaml file is provided with all the required dependencies to start all together. 

## Dependencies

+ PostgresSQL
  + port: 5432
+ RabbitMQ
  + port: 5672

## Development

This application is build with java and spring-boot framework, you should follow feature branch model to include new features with the base of
`feature/<feature_name>`. This application uses `maven` for project management and dependencies management.

This project uses several plugins to create build and manage the development and production code ready. 

Running your app locally. 

#### IntelliJ IDE

This application has datalayer dependencies like rabbit and postgres, if you want to run them you can use docker to easy your development
````
docker-compose -f docker/test/dependencies.yml up -d
````
This will initialize the datalayer dependencies, under localhost with default ports.
Once you are ready to run the application make sure add the profile via JVM argument `-Dspring.profiles.active=dev` as argument if you run from `mvn spring-boot:run` or either `AwesomeMessageApplication` from run configurations in the IDE.

The application uses h2 in memory for testing same as RabbitMocks for MessagingIT bean needs to be overridden

#### Postman
To test the API you will find a postman collection in root folder you can import it and manage your self to run the collection in order test all the required scenarios.

#### Self Document
The API uses Swagger and OpenAPI3 to build the documentation each endpoint must be updated with the proper annotations if new  

#### Production Ready

The application is configured production ready use maven plugins to build the code and create the docker image required to run the application. 
Once the application is build
Emulate pipeline step and deployment step
````
mvn install 
````
Then run the application (server side systemd should initialize the service with:)
````
docker-compose -f docker-compose.yml up -d 
````

This will initialize all the dependencies all together and run the application. 

Service is managing its own data-layers. 


#### Properties 
This project takes advantage of Spring profiles to manage different configurations based on the scenario
+ default (prod ready) 
+ dev (set hosts to localhost)
+ test (uses h2 in memory and JPA autogenerate entities and tables)