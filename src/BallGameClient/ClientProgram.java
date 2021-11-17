package BallGameClient;

import java.util.Scanner;

public class ClientProgram {
    public static void main(String[] args) throws Exception {
        try (Client client = new Client()) {
            Thread t = new Thread(new ClientSideListener(client));
            t.start();
            boolean connected = true;
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter one of the following commands: \n" +
                    "'pass' to pass the ball to a new player, 'leave' to exit the game.");
            while (connected) {

                    String command = in.nextLine();
                    switch (command) {
                        case "leave":
                            client.leave();
                            connected = false;
                            t.stop();
                            continue;

                        case "pass":
                            System.out.println("Enter the id of the player you want to pass the ball to:");
                            int passPlayer = Integer.parseInt(in.nextLine());
                            client.pass(passPlayer);
                            //System.out.println(client.pass(passPlayer));
                            continue;

                        case "ball":
                            int ball = client.ball();
                            System.out.println(ball);
                            continue;

                        default:
                            System.out.println("No such command, please check your spelling.");
                    }
                //}
            }
        }
    }
}
