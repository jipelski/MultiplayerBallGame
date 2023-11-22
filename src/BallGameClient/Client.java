package BallGameClient;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/** Creates a socket and connects to the server using it.
 *  Creates a reader and a writer object that will be used to communicate with the server.
 *  Contains methods for each available command the player can use.
 *  Those commands send messages based on the communication protocol to the server.
 * */


public class Client implements AutoCloseable {
    final int port = 8888;
    final Scanner reader;
    private final PrintWriter writer;
    public int playerId = -1;

    public Client() throws Exception {
        Socket socket = new Socket("localhost", port);

        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

    }

    public void pass(int passPlayer) {
        writer.println("PASS " + passPlayer);

    }

    public void setPlayerId(int playerIdInt) {playerId = playerIdInt;}

    public void ball() {
        writer.println("BALL");
    }

    public void connectedPlayers() {
        writer.println("PLAYERS");
    }

    public void leave() {
        writer.println("LEAVE");
    }

    @Override
    public void close() {
        reader.close();
        writer.close();
    }
}
