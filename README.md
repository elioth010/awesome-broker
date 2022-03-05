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

## Authentication
This is the first proof of concept for this API we want to allow everyone to interact and use the messaging app, so we expect anybody can use it without requiring special authentication requirements. 

In the future users will be required to proof they are authorized actors to interact with it using `jwt` tokens with oauth model. 
