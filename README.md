# Design Document
## Database
For the database implementation, I ended up using MySQL. Unfortunately, I couldn't create an RDS instance on AWS,
I kept getting denied permissions. As an alternative solution, I deployed the database on Heroku; I assume it could still
count as a distributed system.

I created a table called `words` and used each word as the `PRIMARY_KEY`; this might be a questionable choice, but given
the limited business logic of the application, I figured it'd work well. Each record has two colums, the `wid` and `total`;
the `total` column stores the number of times a words is posted to the API. 
Additionally, I updated the code to exclude punctuation and special
characters from being published to the RabbitMQ queue, so I could more easily parse the string message published to the queue. 
I am also ignoring upper and lower cases in each word so that equal words are not 
counted as different records.

To connect to the database, I created two classes: `Database` and `DatabaseDao`. The database class establishes a connection
with the db and implements basic CRUD operations. The DAO is to include a separation layer between the business logic and
the database so that the application is agnostic of the db implementation.

