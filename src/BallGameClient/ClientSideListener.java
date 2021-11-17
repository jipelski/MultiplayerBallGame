package BallGameClient;

public class ClientSideListener implements Runnable {
    Client client;

    ClientSideListener(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            if (client.reader.hasNextLine()) {
                String serverUpdate = client.reader.nextLine();
                String[] updateSubstrings = serverUpdate.split(" ");
                switch (updateSubstrings[0].toLowerCase()) {
                    case "you":
                        client.playerId = Integer.parseInt(updateSubstrings[1]);
                        System.out.println("You are player number " + client.playerId + "\n");
                        continue;

                    case "new":
                        System.out.println(updateSubstrings[1] + " has joined!");
                        System.out.println("Number of current playing players is: " + Integer.parseInt(updateSubstrings[2])
                                + "\nAnd said players are:");
                        for (int i = 0; i < Integer.parseInt(updateSubstrings[2]); i++) {
                            System.out.print(updateSubstrings[i + 3] + " ");
                        }
                        System.out.println("\nPlayer " + Integer.parseInt(updateSubstrings[Integer.parseInt(updateSubstrings[2]) + 3]) + " has the ball.\n");
                        continue;

                    case "left":
                        System.out.println(updateSubstrings[1] + " has left the game!");
                        String hadBall = updateSubstrings[2];
                        if (Boolean.parseBoolean(hadBall)) {
                            System.out.println("Player " + updateSubstrings[3] + " has the ball.\n");
                        }
                        continue;

                    case "pass":
                        System.out.println(updateSubstrings[1] + " passed the ball to " + updateSubstrings[2] + "\n");
                        continue;

                    case "passnoball":
                        System.out.println("You dont have the ball! Player "
                                + updateSubstrings[1] + " has the ball.\n");
                        continue;

                    case "passwrongcommand":
                        System.out.println("Wrong command!\n");
                        continue;

                    case "passnoplayer":
                        System.out.println("No such player! The players you can pass the ball to are: ");
                        for (int i = 0; i < Integer.parseInt(updateSubstrings[1]); i++)
                            System.out.print(updateSubstrings[i + 2] + " ");
                        System.out.println("\n");
                        continue;

                    case "passsuccess":
                        System.out.println("You passed the ball to player " + Integer.parseInt(updateSubstrings[1]) + "!\n");
                        continue;

                    case "passreceived":
                        System.out.println("Player " + Integer.parseInt(updateSubstrings[1]) +
                                " passed the ball to you. You have the ball now!" +
                                "\nTo pass the ball write 'pass'\n");
                        continue;

                    case "ballowner":
                        System.out.println("Player " + updateSubstrings[1] + " has the ball.\n");
                        continue;

                    case "playerslist":
                        System.out.println("List of connected players: ");
                        for (int i = 0; i < Integer.parseInt(updateSubstrings[1]); i++) {
                            System.out.println(updateSubstrings[i + 2] + " ");
                        }
                        System.out.println();

                }
            }
        }
    }
}
