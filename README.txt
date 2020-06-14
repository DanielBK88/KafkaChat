How to use this KafkaChatter:

1) PREREQUISTES:
- The ZooKeeper server must be running on localhost:2181
- The Kafka server must be running on localhost:9092

2) Start the application by running the ChatApplication class.
Each running instance is a chatter.

3) When starting the application, it will ask to enter a login name and a chat room name to use.
The application will create a new Kafka topic with the given chat room name, if such a topic does not yet exist.
Afterwards, the new chatter will enter the chat room and will be able to chat with other chatters, who have
selected the same chat room. All messages between the participants of this chat room will run
over this same topic.

4) The application will run a loop asking the user for inputs.
Per default, any input line will be treated as a public message to all other chat room participants.
When beginning the input with "-to <some-player-name>", then this message will be sent as a private message
to the chatter with the specified name. Other chatters will not see it.

5) To exit the chat room and the application, type "exit".