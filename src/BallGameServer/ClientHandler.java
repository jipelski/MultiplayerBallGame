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
            player.writer.println("you");
            player.writer.println(playerId);



            for (Player player1 : game.players.values()
            ) {


                PrintWriter printWriter = player1.writer;
                printWriter.println("new");
                printWriter.println(playerId);
                printWriter.println(game.players.size());
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
                printWriter.println(playerWithBall);
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
                                            writer.println("passsuccess");
                                            writer.println(passPlayer);
                                            passedBall = true;
                                            for (Player player2 : game.players.values()
                                            ) {
                                                PrintWriter printWriter = player2.writer;
                                                if(player2.id == passPlayer)
                                                {
                                                    printWriter.println("passreceived");
                                                    printWriter.println(player.id);
                                                }
                                                else
                                                {
                                                    printWriter.println("pass");
                                                    printWriter.println(player.id);
                                                    printWriter.println(passPlayer);
                                                }


                                            }
                                            System.out.println(player.id + " passed the ball to " + passPlayer);
                                        }
                                    if (!passedBall)
                                    {
                                        writer.println("passnoplayer");}
                                } catch (Exception e) {

                                    writer.println("passwrongcommand");
                                }
                            } else
                            {

                                writer.println("passnoball");
                            }
                            break;

                        case "leave":
                            //writer.println("goodbye");
                            keepGoing = false;
                            break;
                        case "show_ball":
                            for (Player player1 : game.players.values()) {
                                if (player1.hasBall)
                                    writer.println(player1.getId());
                            }
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
            if(game.players.get(playerId).hasBall)
                hadBall = true;
            game.playerLeft(playerId);

            System.out.println("Player " + playerId + " disconnected.");
            for (Player player : game.players.values()
            ) {
                PrintWriter printWriter = player.writer;
                printWriter.println("left");
                printWriter.println(playerId);
                printWriter.println(hadBall);
                if(hadBall)
                {
                    for (Player player2 : game.players.values()) {
                        if (player2.hasBall) {
                            printWriter.println(player2.getId());
                            System.out.println(player2.getId() + " has the ball.");
                        }
                    }
                }

            }
            //broadcast to all players
        }
    }
}
