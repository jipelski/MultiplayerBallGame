package BallGameClient;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable{
    final int port = 8888;
    int playerId = -1;

    final Scanner reader;
    private final PrintWriter writer;

    public Client() throws Exception
    {
        // Connecting to the server and creating objects for communications
        Socket socket = new Socket("localhost", port);
        reader = new Scanner(socket.getInputStream());

        // Automatically flushes the stream with every command
        writer = new PrintWriter(socket.getOutputStream(), true);

        // Parsing the response
        //playerId = Integer.parseInt(reader.nextLine());
        reader.nextLine();
    }

    public String pass(int passPlayer)
    {
        writer.println("PASS " + passPlayer);
        String line = reader.nextLine();
        return line;
    }

    public int ball()
    {
        writer.println("show_ball");
        int playerIdwithBall = reader.nextInt();
        return playerIdwithBall;
    }

    public void leave()
    {
        writer.println("LEAVE");
        reader.nextLine();
    }

    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
    }
}
