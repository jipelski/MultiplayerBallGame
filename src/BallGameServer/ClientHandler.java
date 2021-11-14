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
            Player player = game.createPlayer(writer);
            playerId = player.id;
            for (Player player1: game.players.values()
                 ) {
                PrintWriter printWriter = player1.writer;
                printWriter.println("Player "+ playerId + " has joined the game!");
            }

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
                                    for (Player player1 : game.players.values())
                                        if (player1.getId() == passPlayer) {
                                            game.passBall(playerId, passPlayer);
                                            writer.println("You passed the ball to " + game.players.get(passPlayer).getId());
                                            passedBall = true;
                                            for (Player player2: game.players.values()
                                                 ) {
                                                PrintWriter printWriter = player2.writer;
                                                printWriter.println(player.id + " passed the ball to " + passPlayer + "!");
                                            }
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
                            writer.println("GOODBYE");
                            keepGoing = false;
                            break;
                        case "show_ball":
                            for (Player player1 : game.players.values())
                            {writer.println(player1.getId());
                                if (player1.hasBall)
                            writer.println(player1.getId() + " has the ball.");}
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
            boolean newOwner = false;
            int newOwnerId = -1;
            if(game.players.get(playerId).hasBall) {
                newOwner = true;
                game.playerLeft(playerId);
                for (Player player: game.players.values()
                     ) {
                    if(player.hasBall)
                        newOwnerId = player.id;
                }
            }
            else
                game.playerLeft(playerId);

            System.out.println("Player " + playerId + " disconnected.");
            for (Player player : game.players.values()
                 ) {
                PrintWriter printWriter = player.writer;
                printWriter.println("Player " + playerId + " has left the game!");
                if(newOwner)
                    printWriter.println("Player " + newOwnerId + " has the ball now!");

            }
            //broadcast to all players
        }
    }
}
