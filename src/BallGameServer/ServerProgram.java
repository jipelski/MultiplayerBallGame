package BallGameServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/** Server main class which uses a while loop to keep the server process running indefinitely.
 *  It creates a main thread for the server and additional threads for each player that joins the game.*/

public class ServerProgram {

    private final static int port = 8888;

    private static final Game game = new Game();

    public static Map<Integer, PrintWriter> writers = new HashMap<>();

    /** Main method. Creates the main thread and runs the RunServer method*/
    public static void main (String [] args)
    {
        RunServer();
    }

    /** Method that runs indefinitely and creates additional threads for each player that joins the game. */
    private static void RunServer() {
        ServerSocket serverSocket;

        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server running. Waiting for players...");
            while (true) {

                //Synchronize the threads to dont lose or get the wrong player Id
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, game)).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }




}
