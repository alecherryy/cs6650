
# Design Document
## Structure
After refactoring the client from Assignment 1; I updated the project structure to include the `Server`.


### Server
The `Server` class has been updated to include the `init()` method; this is where I call all objects needed to create a 
connection to RabbitMQ and instantiate a new channel pool. In the `doPost()`, we grab a Channel from
the pool each time a new request is received, publish the payload to the queue and return the channel to the pool.
To successfully implement the Channel Pool, I created three additional classes.

The `PoolChannelFactory` class is used to create a new `Channel` object to inject into the object pool; in the `create()` 
method I define the queue and exchange names to create a Sender. As channels are
created they are placed into the pool and are selectively taken out and put back in the pool
once the message has been delivered to the queue.

The `PoolChannelWrapper` is more of a utility class to prevent Exceptions when instantiation a new pool.

Aside from that, the only other new Class I created was the `Task` class which can be used to perform different operations
on the HTTP requests bodies.

All results from the various tests can be found in the `/screenshots` folder.