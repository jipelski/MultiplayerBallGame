package BallGameClient;

import java.util.Scanner;

public class ClientProgram {
    public static void main(String[] args) throws Exception {
        try (Client client = new Client()) {
            System.out.println(client.playerId);
            boolean connected = true;
            Scanner in = new Scanner(System.in);
            while (connected) {
                if (client.reader.hasNextLine()) {
                    String serverUpdate = client.reader.nextLine();
                    switch (serverUpdate) {
                        case "you":
                            client.playerId = Integer.parseInt(client.reader.nextLine());
                            System.out.println("You are player number " + client.playerId);
                            continue;

                        case "new":
                            System.out.println(client.reader.nextLine() + "has joined!");
                            serverUpdate = client.reader.nextLine();
                            System.out.println("Currently playing players are: ");
                            for (int i = 0; i < Integer.parseInt(serverUpdate); i++) {
                                System.out.print(client.reader.nextLine() + " ");
                            }
                            System.out.println("\nPlayer " + client.reader.nextLine() + " has the ball.");
                            continue;
                        case "left":
                            System.out.println(client.reader.nextLine() + "has left the game!");
                            String hadBall = client.reader.nextLine();
                            if(Boolean.parseBoolean(hadBall))
                                {
                                    System.out.println("Player " + client.reader.nextLine() + " has now the ball.");
                                }
                            continue;
                        case "pass":
                            System.out.println(client.reader.nextLine() + " passed the ball to " + client.reader.nextLine());
                            continue;
                    }
                } else {
                    String command = in.nextLine();
                    switch (command) {
                        case "leave":
                            client.leave();
                            connected = false;
                            continue;

                        case "pass":
                            System.out.println("Enter the id of the player you want to pass the ball to:");
                            int passPlayer = Integer.parseInt(in.nextLine());
                            System.out.println(client.pass(passPlayer));
                            continue;

                        case "ball":
                            int ball = client.ball();
                            System.out.println(ball);
                            continue;
                    }
                }
            }
        }
    }
}
