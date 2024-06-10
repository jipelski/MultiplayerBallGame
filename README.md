# MultiplayerBallGame
## Overview
The BallGame Project is a Java-based networked multiplayer game. It simulates a simple environment where players can pass a virtual ball among themselves. The project is divided into two main components: the BallGameServer, which handles the game logic and client connections, and the BallGameClient, which allows players to interact with the game.
## Project Structure
### Server Component ('BallGameServer')
The server manages the game's state and handles client connections. It includes the following key classes:

* **ServerProgram**: The entry point of the server. It initializes the server and listens for incoming client connections.
* **ClientHandler**: Responsible for handling communication with connected clients.
* **Game**: Manages the overall game state, including players and ball possession.
* **Player**: Represents a player in the game.

### Client Component ('BallGameClient')
The client provides a user interface for players to interact with the game. It consists of these main classes:

* **ClientProgram**: The main class for the client. It processes user input and interacts with the server.
* **Client**: Handles the network connection to the server and sends commands based on user actions.
* **ClientSideListener**: Listens to messages from the server and updates the client-side display.
 
## Setup Instructions

### Prerequisites
* Java Runtime Environment (JRE) and Java Development Kit (JDK), version 8 or higher.

### Compiling and Running
**Server**
1. **Compile the server**:
    * Navigate to the server directory.
    * Run **'javac BallGameServer/*.java'** to compile all server classes.
2. **Run the server**:
    * Execute **'java BallGameServer.ServerProgram'**.
    * The server will start and listen for client connections.
**Client**
1. **Compile the Client**:
    * Navigate to the client directory.
    * Run **'javac BallGameClient/*.java'** to compile all client classes.
2. **Run the Client**:
    * Execute **'java BallGameClient.ClientProgram'**.
    * The client will connect to the server, and the user can start interacting with the game.
## Usage guide
### Client Commands
* **Pass the Ball: 'pass <PlayerID>'** - Pass the ball to another player.
* **Check Ball Holder: 'ball'** - Query who currently holds the ball.
* **Identify Yourself: 'whoAmI'** - Check your player ID.
* **List Players: 'list'** - Show all connected players.
* **Help: 'help'** - Display available commands.
* **Leave Game: 'leave'** - Disconnect from the game.
### Server Behavior
* The server handles client connections and manages the state of the game.
* All game logic, including ball passing and player tracking, is processed on the server.

## Network Configuration
* Default port: **'8888'**.
* Host: **'localhost'** (for testing on a single machine).
* Modify the client and server code for different network setups or to change the port.

## Additional Notes
* Ensure the server is running before attempting to connect with a client.
* The game logic is relatively simple and can be extended for more complex interactions or features.
* The server and client are configured to run on **'localhost'** with port **'8888'**.
