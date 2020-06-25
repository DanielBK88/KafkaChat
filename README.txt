How to use this Websocket chatter:

1) PREREQUSITE: The Database specified in persistence.xml should be running (now defined: MySQL8).

2) Run the TestDataCreation class. It will insert two roles (chatter and admin) into the database with
corresponding permissions.

3) Start the server (run the ChatServer class). The server port is defined in CommonConstants.

4) Start a client (run the ChatClient class). It will connect to the server automatically.

5) Create an account using corresponding commands. The chat application will print out possible commands to help you.
Example: 

-sign_up_name MyUser
-sign_up_pw AValidPW88!!
-sign_up_pw_confirm AValidPW88!!

After the third command the new user will be stored to the database.
This means, if you restart the server and the client, you will be able to login as this user as follows:

-login MyUser
-pw AValidPW88!!

After signing up successfully, you are logged in automatically.

6) Start more chat client instances to sign up other users.

7) Open a new chat room with any of the logged in users:

-room_open myRoomName

8) Let other logged in chatters join this room:

-room_select myRoomName

Chatters, who are already inside the room, will be notified about new joined participants.
And the newcomers will also be informed about already existing chatters.

9) Now you can chat!
Send public messages as follows:

-pub Some Message

Send private messages as follows:

-priv Some Message -to SomeUser

10) Have fun :)