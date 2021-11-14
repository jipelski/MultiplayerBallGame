package BallGameServer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/** Class that will handle the client communication. */

public class ClientHandler implements Runnable {

    private final Socket socket;
    private Game game;



    public ClientHandler(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    @Override
    public void run() {

        int playerId = -1;
        try(
                Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true))
        {

            game.createPlayer(writer);
            playerId = game.latestId-1;

            for (PrintWriter printWriter: ServerProgram.writers.values()
                 ) {
                printWriter.println("Player "+ playerId + " has joined!");
            }
            ServerProgram.writers.put(playerId, writer);

            try{
                boolean keepGoing = true;
                System.out.println("Player " + playerId + " connected!");
                writer.println("CONNECTED");

                while(keepGoing)
                {
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    switch (substrings[0].toLowerCase())
                    {
                        case "pass":
                            if(game.players.get(playerId).hasBall)
                            {
                                try {
                                    int passPlayer = Integer.parseInt(substrings[1]);
                                    boolean passedBall = false;
                                    for (Player player : game.players.values())
                                        if (player.getId() == passPlayer) {
                                            game.passBall(playerId, passPlayer);
                                            writer.println("You passed the ball to " + game.players.get(passPlayer).getId());
                                            passedBall = true;
                                        }
                                    if (!passedBall)
                                        writer.println("No such player!");
                                }
                                catch (Exception e)
                                {
                                    writer.println("Please write pass and the id of the player. The Id must be a number.");
                                }
                            }
                            else
                                writer.println("You don't have the ball!");
                            break;

                        case "leave":
                            game.playerLeft(playerId);
                            writer.println("GOODBYE");
                            keepGoing = false;
                            break;
                        case "show_ball":
                            for (Player player : game.players.values())
                            {writer.println(player.getId());
                                if (player.hasBall)
                            writer.println(player.getId() + " has the ball.");}
                            break;

                        default:
                            //throw new Exception("Unknown command: " + substrings[0]);
                            writer.println("No such command! Only commands available are 'leave' to quit the game," +
                                    "'show_ball' to show who has the ball and 'pass' + the id of the player" +
                                    "you want to pass the ball to!");
                            break;
                    }
                }
            }
            catch (Exception e)
            {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }

        }
        catch (Exception ignored)
        {
        }
        finally {
            game.playerLeft(playerId);
            System.out.println("Player " + playerId + " disconnected.");
            ServerProgram.writers.remove(playerId);
            for (PrintWriter printWriter: ServerProgram.writers.values()
                 ) {
                printWriter.println("Player " + playerId + " has left the game!");
            }
            //broadcast to all players
        }
    }
}
