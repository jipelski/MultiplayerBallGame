package BallGameClient;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable {
    final int port = 8888;
    final Scanner reader;
    private final PrintWriter writer;
    int playerId = -1;

    public Client() throws Exception {
        Socket socket = new Socket("localhost", port);

        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

    }

    public void pass(int passPlayer) {
        writer.println("PASS " + passPlayer);

    }

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
