# GameServerManager README

## Overview
The GameServerManager class is the main component that handles client connections and game sessions for our multiplayer board game platform. It listens for client connections, manages match requests, and processes challenges. This class connects the networking side of the application with the game logic and match-making system.

## Main Responsibilities
Client Connection Handling:
Opens a server socket to accept clients and creates a new handler for each client connection.

Match Requests and Match-Making:
Manages match requests by:

Finding a match for a player.

Hosting a game lobby.

Ending a hosted lobby.

Allowing players to join existing lobbies.

Challenge Management:
Lets players send, cancel, and accept challenges. When a challenge is accepted, it creates a game session (match) between the two players.

Database Tasks (Testing):
Uses basic database managers (match, profile, and queue) for handling data. Some parts are meant for testing and will be changed when full authentication and persistence are implemented.

## Methods Overview
### Connection Management
Constructor (GameServerManager(ServerSocket serverSocket))
Sets up the server using a given socket and creates database managers and a match maker.

startServer()
Listens continuously for new client connections. For each new client, it creates a ClientHandler and starts a new thread to handle that connection.

stopServer()
Closes the server socket to stop client connections.

main(String[] args)
This is the starting point of the server. It creates a server socket, initializes the server, clears the test database, and starts the server.

### Client Handling
registerLoggedInUser(int playerID, ClientHandler handler)
Adds a client to the list of logged-in users. This is used for sending messages to the right clients until proper authentication is in place.

removeClientHandler(int clientID)
Removes a client from the list when they disconnect.

### Match Operations
requestMatch(int playerID, Game game)
Looks for a match for a player. If a match is found, it collects all the client handlers involved and starts the match.

hostMatch(Game game, int playerID)
Tells the match maker to host a game lobby for the specified player.

unhostMatch(Game game, int playerID)
Ends a hosted game lobby using the match maker.

getHosts(Game game)
Returns a list of players who are currently hosting a game lobby.

joinMatch(Game game, int playerID, int hostID)
Lets a player join an existing match hosted by another player. It gets the game controller from the match maker and then starts the match. If joining fails, it throws an exception.

### Challenge Operations
registerChallenge(Game game, int challengerID, int challengedID)
Sends a challenge from one player to another using the match maker. If there is an error during registration, the challenger is notified.

deregisterChallenge(MatchChallenge m)
Removes a challenge from the system.

acceptChallenge(MatchChallenge challenge)
Accepts a challenge to start a match between two players. This method checks that both players are online and then creates a match session. If it fails, it throws an exception.

## Summary
The GameServerManager class handles the core server operations: connecting clients, processing match requests, and managing challenges. It works together with the match maker and various database managers to provide a seamless multiplayer experience. This README explains each function in simple terms to help understand how the server operates.
