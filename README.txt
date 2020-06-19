How to use this KafkaChatter:

1) PREREQUISTES:
- The ZooKeeper server must be running on localhost:2181
- The Kafka server must be running on localhost:9092
- The MySql 8 server must be running on port 3306 (change the root password in persistence.xml accordingly)
- A database named 'chat-db' must exist in MySql (you can change the expected name in persistence.xml).
  It may be empty in the beginning (contain no tables).
  
  
2) To create a basic set of test data, run the TestDataCreation class. It will create a few users, roles and rooms
(here it is important that Kafka is already running, because room creation is always coupled with Kafka topics creation)

3) Start the application by running the ChatApplication class.
Each running instance is a chatter.

4) When starting the application, it will ask to enter a login name and a chat room name to use.
The application will create a new Kafka topic with the given chat room name, if such a topic does not yet exist.
Though, only administrators can create new chat rooms! The test data set contain also an admin account.
Afterwards, the new chatter will enter the chat room and will be able to chat with other chatters, who have
selected the same chat room. All messages between the participants of this chat room will run
over this same topic.

5) The application will run a loop asking the user for inputs.
Per default, any input line will be treated as a public message to all other chat room participants.
When beginning the input with "-to <some-player-name>", then this message will be sent as a private message
to the chatter with the specified name. Other chatters will not see it.
When beginning the input with "-ban <some-player-name>", the corresponding chatter will be banned from this chat room
(needs admin permissions!). The following text of the input will be shown as the reason of banning to the banned user.

5) To exit the chat room and the application, type "exit".

6) UNIT TESTS are currently configured to run on a separate hsql data base. Change the configuration if needed.