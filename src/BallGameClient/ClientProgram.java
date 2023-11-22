package BallGameClient;

import java.util.Scanner;

/** Main class of the Client Program. Creates a Client object.
 *  Creates and starts a ClientSideListener thread.
 *  Listens for player input using a scanner. Handles the input and calls the appropriate Client methods.
 *  Gives a list of possible commands to the player.*/

public class ClientProgram {
    public static void main(String[] args) throws Exception {
        try (Client client = new Client()) {
            Thread t = new Thread(new ClientSideListener(client));
            t.start();
            boolean connected = true;
            Scanner in = new Scanner(System.in);
            System.out.println("""
                    Available commands are:
                    'pass' to pass the ball to a new player
                    'ball' to show who has the ball
                    'whoAmI' to show what player number you have
                    'list' to show a list of connected players
                    'help' to show the available commands
                    'leave' to exit the game
                    """);
            while (connected) {

                String command = in.nextLine();
                String[] commandSubstring = command.split(" ");
                switch (commandSubstring[0].toLowerCase()) {
                    case "leave" -> {
                        System.out.println("Good bye!");
                        client.leave();
                        connected = false;
                        t.interrupt();

                    }
                    case "pass" -> {
                        System.out.println("Enter the id of the player you want to pass the ball to:");
                        try {
                            int passPlayer = Integer.parseInt(in.nextLine());
                            client.pass(passPlayer);
                        } catch (Exception e) {
                            System.out.println("No such player!\n");
                        }

                    }
                    case "whoami" -> System.out.println("You are player " + client.playerId + "\n");
                    case "ball" -> client.ball();
                    case "list" -> client.connectedPlayers();
                    case "help" -> System.out.println("""
                            Available commands are:
                            'pass' to pass the ball to a new player
                            'ball' to show who has the ball
                            'whoAmI' to show what player number you have
                            'list' to show a list of connected players
                            'help' to show the available commands
                            'leave' to exit the game
                            """);
                    default -> System.out.println("No such command, enter 'help' for the list of commands.\n");
                }
            }
        }
    }
}
