package BallGameClient;

import java.util.Scanner;

public class ClientProgram {
    public static void main(String[] args) throws Exception {
        try (Client client = new Client()) {
            Thread t = new Thread(new ClientSideListener(client));
            t.start();
            boolean connected = true;
            Scanner in = new Scanner(System.in);
            System.out.println("Available commands are:" +
                    "\n'pass' to pass the ball to a new player" +
                    "\n'ball' to show who has the ball" +
                    "\n'list' to show a list of connected players" +
                    "\n'help' to show the available commands" +
                    "\n'leave' to exit the game\n");
            while (connected) {

                String command = in.nextLine();
                String[] commandSubstring = command.split(" ");
                switch (commandSubstring[0].toLowerCase()) {
                    case "leave":
                        System.out.println("Good bye!");
                        client.leave();
                        connected = false;
                        t.stop();
                        continue;

                    case "pass":
                        System.out.println("Enter the id of the player you want to pass the ball to:");
                        try {
                            int passPlayer = Integer.parseInt(in.nextLine());
                            client.pass(passPlayer);
                        } catch (Exception e) {
                            System.out.println("No such player!\n");
                        }
                        continue;

                    case "ball":
                        client.ball();
                        continue;

                    case "list":
                        client.connectedPlayers();
                        continue;

                    case "help":
                        System.out.println("Available commands are:" +
                                "\n'pass' to pass the ball to a new player" +
                                "\n'ball' to show who has the ball" +
                                "\n'list' to show a list of connected players" +
                                "\n'help' to show the available commands" +
                                "\n'leave' to exit the game\n");
                        continue;
                    default:
                        System.out.println("No such command, enter 'help' for the list of commands.\n");
                }
            }
        }
    }
}
