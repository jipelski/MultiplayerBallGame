package BallGameServer;

import com.sun.prism.shader.AlphaOne_Color_AlphaTest_Loader;
import com.sun.security.ntlm.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** Server main class which uses a while loop to keep the server process running indefinitely.
 *  It creates a main thread for the server and additional threads for each player that joins the game.*/

public class ServerProgram {

    private final static int port = 8888;
    static List<Thread> players = new ArrayList<>();

    /** Main method. Creates the main thread and runs the RunServer method*/
    public static void main (String [] args)
    {
        RunServer();
    }

    static Broadcast broadcast = new Broadcast("I am Alive");

    /** Method that runs indefinitely and creates additional threads for each player that joins the game. */
    private static void RunServer() {
        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server running. Waiting for players...");
            while (true) {
                Socket socket = serverSocket.accept();
                Thread playerThread = new Thread(new ClientHandler(socket));
                playerThread.start();
                players.add(playerThread);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
