# Networking

The networking package encompasses all classes that create and communicate with the server and all classes that write and read the database.

## File Descriptions

### AbstractDatabaseManager
- Filename: AbstractDatabaseManager.java
- Location: `src/networking/AbstractDatabaseManager.java` <br>
- Description: All common functions and values used throughout MatchDatabaseManager, ProfileDatabaseManager and QueueDatabaseManager as well as general database manipulation and reading.

### ProfileDatabaseManager
- Filename: ProfileDatabaseManager.java
- Location: `src/networking/ProfileDatabaseManager.java` <br>
- Description: Methods for the reading and writing of all profile information from/to the database.

### MatchDatabaseManager
- Filename: MatchDatabaseManager.java
- Location: `src/networking/MatchDatabaseManager.java` <br>
- Description: Methods for the reading and writing of all match/player game stats information from/to the database.

### QueueDatabaseManager
- Filename: QueueDatabaseManager.java
- Location: `src/networking/QueueDatabaseManager.java` <br>
- Description: Methods for the reading and writing of all current queue information from/to the database.

### AbstractNetworkManager
- **Filename**: `AbstractNetworkManager.java`
- **Location**: `src/networking/AbstractNetworkManager.java` <br>
- **Description**: Abstract class defining foundational methods for managing connections and disconnections in the server lifecycle.

### GameServerManager
- **Filename**: `GameServerManager.java`
- **Location**: `src/networking/GameServerManager.java`  <br>
- **Description**: The main server controller. Accepts new client connections, spawns new `ClientHandler` threads for communication, and maintains the state of active players and games.

### ClientHandler
- **Filename**: `ClientHandler.java`
- **Location**: `src/networking/ClientHandler.java`  <br>
- **Description**: Handles communication between the server and each client. Processes incoming messages (login, registration, matchmaking, chat, etc.), delegates appropriate actions to database managers or matchmaking logic, and sends responses.

### Client
- **Filename**: `Client.java`
- **Location**: `src/networking/Client.java`  <br>
- **Description**: Acts as the client-side communication class responsible for sending and receiving serialized messages to/from the server. Used primarily in the GUI layer to connect with the back-end.

## How Communication Works

- All messages are sent over sockets using Java’s `ObjectInputStream` and `ObjectOutputStream`.
- The client sends serialized message objects (e.g., `LogIn`, `Register`, `FindGame`) to the server.
- The `ClientHandler` on the server receives these messages and routes them using a `switch` statement based on the object type.
- Database managers are used to persist profile and match data.
- Game matchmaking and move handling is done through controllers and helper classes from the `match_making` package.

## References

| Package Name                 | Purpose                                   |
|------------------------------|-------------------------------------------|
| `java.io`                    | File handling (reading/writing files)     |
| `java.util`                  | Arrays and ArrayLists                     |
| `java.util.concurrent.locks` | File read and write locks                 |
| `match_making.Game`          | Game Enum for match game type and sorting |

## Contributors
- Owen Hills      (owen.hills1@ucalgary.ca)
- Rojina Dayhimi (rojina.dayhimi@ucalgary.ca)
- Saw Daniel Aung Khant Moe (sawdanielaungkhant.m@ucalgary.ca)
- Dhruv Pujara (dhruv.pujara1@ucalgary.ca)
- Henis Patel (henis.patel@ucalgary.ca)
- Elina Pachhai (elina.pachhai@ucalgary.ca)