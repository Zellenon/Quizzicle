# Quizzicle

### Problem Statement
The goal of this project is to implement a multi-player trivia game in Java with support for
multi-media. The game should provide an engaging game playing experience for the users. You
will want to differentiate your game from other teams (remember this is a competition) by
providing multi-media question formats and potentially different game modes.

Minimum Requirements:
1. A server component that maintains a list of questions that is to be sent to the clients.
2. The server must keep track of each player’s score (you can define your own set of
scoring criteria)
3. Your server must support a minimum of 3 types of question, 1 type minimum must be
multimedia (picture, video, sound, …)
4. Clients must connect to the server and receive questions directly from the server and
report a response back to the server
5. A minimum of 3 simultaneous players must be supported
6. Clients must be able to see all users scores at least once during a game
7. A minimum of 5 questions must be played per game and they must be randomly
selected from a larger question set
8. The client must be GUI based (Swing or JavaFX)

### Developer Documentation

![uml_diagram](doc/UML2.png)

[Javadocs]doc/src/package-summary.html)

The frontend is a JavaFX layout controlled by MasterUIController. It has methods corresponding to each of the buttons and elements of the user interface. The user interface has a setup screen where players and teams are shown, and the host can choose the category and start the game. The main game screen displays your current question, your team's points, what team you're on, the current category, and has options to answer the question. Each interaction will call a method in the backend to send a corresponding data packet to the server, and data packets from the server will be reflected by the GUI.

The communication between server and client is handled by the net package, and primarily the NetworkManager, which allows the sending and receiving of packets between user and server. There are individual packets for question information, game information, team information, and any other info to be sent between the two. The NetworkManager has methods for sending to server, client, or all in the network. To use the network, packets are instantiated with the proper data and these methods are used to send them to their destination.

The backend of the application is handled by four main classes, QuizServer, QuizClient, GameManager, and DBManager. The DBManager connects to the SQL database and retrieves information as required, most importantly the GetRandomQuestion method to get a random question. QuizServer is run only by the host and manages things like client connections, answer checking, and otherwise instantiating and sending out packets. QuizClient receives packets and directs their data to the proper method on the client side. 

The GameManager class handles the logic of the game: how it runs, the state of the game, when it starts and ends, etc. It runs a timer for the whole game and updates the clients with the time remaining, maintains master lists of teams and players, and calls methods to begin and end the game. 


### User Documentation
The application will prompt the user at startup for a username, and whether they want to host or join a game

If the user wants to host a game, they will be brought to the setup screen where they can select a topic for the quiz, and watch the team roster grow as people join their server. Players will be assigned to one of three teams, with the server attempting to keep team numbers even. When enough/all people are in, press 'Begin Game' to start the round of trivia. Players _can_ join in the middle of a match.

If the user wants to join a game, they will then be prompted to enter the IP of the game to join. If they connect properly and someone in the server has already taken the username they entered at startup, they will be prompted to enter another name until they enter an unused name. They will then wait until the host starts the game.

Once the game starts, all players will be able to select one or more out of 4 answers, and receive feedback on the accuracy of their answer within one or two seconds. You score is only affected by your first guess. You are required to get the question right to continue. There are multi-answer questions where more than one of the answers is correct. To get the question correct, you must select ALL of the correct answers and NONE of the incorrect ones.

One a team runs out of questions, they will receive a small bonus.

Once the 3-minute timer runs out, all players will be sent to the leaderboard screen to see the scores of the teams.

### Source Code
[Click here to view source code](src)
