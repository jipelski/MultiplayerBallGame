package BallGameServer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class that will handle the client communication.
 */

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Game game;

    public ClientHandler(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
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

            player.writer.println("You are player number " + playerId);
            for (Player player1 : game.players.values()
            ) {
                PrintWriter printWriter = player1.writer;
                printWriter.println(playerId + " joined!");
                printWriter.println("Currently playing " + game.players.size() + " players");
                System.out.println("Currently playing " + game.players.size() + " players:");
                int playerWithBall = -1;
                for (Player player2 : game.players.values()
                ) {
                    printWriter.println(player2.getId());
                    System.out.print(player2.getId() + " ");
                    if (player2.hasBall) {
                        playerWithBall = player2.id;
                    }
                }
                System.out.println();
                printWriter.println(playerWithBall + " has the ball");
                System.out.println(playerWithBall + " has the ball");
            }

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
                                    for (Player player1 : game.players.values())
                                        if (player1.getId() == passPlayer) {
                                            game.passBall(playerId, passPlayer);
                                            writer.println("You passed the ball to " + game.players.get(passPlayer).getId());
                                            passedBall = true;
                                            for (Player player2 : game.players.values()
                                            ) {
                                                PrintWriter printWriter = player2.writer;
                                                printWriter.println(player.id + " passed the ball to " + passPlayer + "!");

                                            }
                                            System.out.println(player.id + " passed the ball to " + passPlayer);
                                        }
                                    if (!passedBall)
                                        writer.println("No such player!");
                                } catch (Exception e) {
                                    writer.println("Please write pass and the id of the player. The Id must be a number.");
                                }
                            } else
                                writer.println("You don't have the ball!");
                            break;

                        case "leave":
                            writer.println("GOODBYE");
                            keepGoing = false;
                            break;
                        case "show_ball":
                            for (Player player1 : game.players.values()) {
                                writer.println(player1.getId());
                                if (player1.hasBall)
                                    writer.println(player1.getId() + " has the ball.");
                            }
                            break;

                        default:
                            //throw new Exception("Unknown command: " + substrings[0]);
                            writer.println("No such command! Only commands available are 'leave' to quit the game," +
                                            "'show_ball' to show who has the ball and 'pass' + the id of the player" +
                                            "you want to pass the ball to!");
                            break;
                    }
                }
            } catch (Exception e) {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }

        } catch (Exception ignored) {
        } finally {
            game.playerLeft(playerId);

            System.out.println("Player " + playerId + " disconnected.");
            for (Player player : game.players.values()
            ) {
                PrintWriter printWriter = player.writer;
                printWriter.println("Player " + playerId + " has left the game!");
                for (Player player2 : game.players.values()
                ) {
                    printWriter.println(player2.getId());
                    if (player2.hasBall) {
                        printWriter.println(player2.getId() + " has the ball.");
                        System.out.println(player2.getId() + " has the ball.");
                    }
                }
            }
            //broadcast to all players
        }
    }
}
