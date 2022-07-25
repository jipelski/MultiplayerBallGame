package BallGameServer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class that handles the client communication and creates a player object.
 * It listens for player commands through the socket.
 * Handles those commands and sends a response back to the player client.
 * Sends update messages based on the game changes.
 */

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Game game;

    public ClientHandler(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    /** Sends a message update to all the players using their writers. */
    private void updateAll(String msg) {
        for (Player player : game.players.values()
        ) {
            PrintWriter printWriter = player.writer;
            printWriter.println(msg);
        }
    }

    private String getStringOfPlayers() {
        StringBuilder stringOfPlayers = new StringBuilder();
        for (Player player : game.players.values()
        ) {
            stringOfPlayers.append(player.id).append(" ");
        }
        return stringOfPlayers.toString();
    }
    /** Returns an int that corresponds with the player id of the player in possession of the ball.*/
    private int getPlayerWithBall() {
        int ballOwner = -1;
        for (Player player : game.players.values()
        ) {
            if (player.hasBall) {
                ballOwner = player.id;
                break;
            }
        }
        return ballOwner;
    }

    @Override
    public void run() {

        int playerId = -1;
        try (
                Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            Player player = game.createPlayer(writer);
            playerId = player.id;

            System.out.println("Player " + playerId + " connected!");
            player.writer.println("YOU " + playerId);

            String updateMsg = "NEW " + playerId + " " + game.players.size() + " "
                    + getStringOfPlayers() + getPlayerWithBall();


            updateAll(updateMsg);

            try {
                boolean keepGoing = true;

                while (keepGoing) {
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    switch (substrings[0].toLowerCase()) {

                        case "pass":
                            if (game.players.get(playerId).hasBall) {
                                try {
                                    int passPlayer = Integer.parseInt(substrings[1]);
                                    boolean passedBall = false;

                                    if (game.players.containsKey(passPlayer)) {
                                        game.passBall(playerId, passPlayer);
                                        passedBall = true;
                                        writer.println("PASSSUCCESS " + passPlayer);
                                        updateAll("PASS " + player.id + " " + passPlayer);
                                        PrintWriter writer1 = game.players.get(passPlayer).writer;
                                        writer1.println("PASSRECEIVED " + playerId);
                                        System.out.println(playerId + " passed the ball to " + passPlayer);

                                    }
                                    if (!passedBall) {
                                        writer.println("PASSNOPLAYER " + game.players.size() + " " + getStringOfPlayers());

                                    }
                                } catch (Exception e) {

                                    writer.println("passWrongCommand");
                                }
                            } else {

                                writer.println("PASSNOBALL " + getPlayerWithBall());
                            }
                            break;

                        case "leave":
                            keepGoing = false;
                            break;

                        case "ball":
                            writer.println("BALLOWNER " + getPlayerWithBall());
                            break;
                        case "players":
                            writer.println("PLAYERSLIST " + game.players.size() + " " + getStringOfPlayers());
                            break;

                    }
                }
            } catch (Exception e) {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }

        } catch (Exception ignored) {
        } finally {

            //Checks if the player had the ball and calls the playerLeft method to remove it from the list of players.
            boolean hadBall = game.players.get(playerId).hasBall;
            game.playerLeft(playerId);

            System.out.println("Player " + playerId + " disconnected.");


            if (hadBall)
            {
                updateAll("LEFT " + playerId + " " + true + " " + getPlayerWithBall());
                if(getPlayerWithBall()!=-1)
                    System.out.println("Player " + getPlayerWithBall() + " now has the ball.");
                else
                    System.out.println("No players in the game. When a player will join the ball will be passed to them.");
            }
            else
                updateAll("LEFT " + playerId + " " + false);
        }
    }
}
