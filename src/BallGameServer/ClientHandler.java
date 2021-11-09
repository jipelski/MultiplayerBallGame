package BallGameServer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/** Class that will handle the client commands. */

public class ClientHandler implements Runnable {

    private final Socket socket;

    Broadcast broadcast = new Broadcast("Ia");

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(
                Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true))
        {
            broadcast.broadcastJoin();
        }
        catch (Exception e)
        {
        }
    }
}
