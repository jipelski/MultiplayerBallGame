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

    void updateAll(String msg) {
        for (Player player : game.players.values()
        ) {
            PrintWriter printWriter = player.writer;
            printWriter.println(msg);
        }
    }

    String getStringofPlayers() {
        StringBuilder stringOfPlayers = new StringBuilder();
        for (Player player : game.players.values()
        ) {
            stringOfPlayers.append(player.id).append(" ");
        }
        return stringOfPlayers.toString();
    }

    int getPlayerWithBall() {
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
            player.writer.println("you " + playerId);

            String updateMsg = "new " + playerId + " " + game.players.size() + " "
                    + getStringofPlayers() + getPlayerWithBall();


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
                                        writer.println("passSuccess " + passPlayer);
                                        updateAll("pass " + player.id + " " + passPlayer);
                                        PrintWriter writer1 = game.players.get(passPlayer).writer;
                                        writer1.println("passReceived " + playerId);

                                    }
                                    if (!passedBall) {
                                        writer.println("passNoPlayer " + game.players.size() + " " + getStringofPlayers());

                                    }
                                } catch (Exception e) {

                                    writer.println("passWrongCommand");
                                }
                            } else {

                                writer.println("passNoball " + getPlayerWithBall());
                            }
                            break;

                        case "leave":
                            keepGoing = false;
                            break;

                        case "ball":
                            writer.println("ballOwner " + getPlayerWithBall());
                            break;
                        case "players":
                            writer.println("playersList " + game.players.size() + " " + getStringofPlayers());
                            break;

                    }
                }
            } catch (Exception e) {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }

        } catch (Exception ignored) {
        } finally {
            boolean hadBall = false;
            if (game.players.get(playerId).hasBall)
                hadBall = true;
            game.playerLeft(playerId);

            System.out.println("Player " + playerId + " disconnected.");

            if (hadBall)
                updateAll("left " + playerId + " " + true + " " + getPlayerWithBall());
            else
                updateAll("left " + playerId + " " + false);

        }
    }
}
