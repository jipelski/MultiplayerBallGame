package BallGameClient;

public class ClientSideListener implements Runnable {
    Client client;

    ClientSideListener (Client client)
    {
        this.client = client;
    }

    @Override
    public void run() {
        boolean keepgoing = true;
        while (keepgoing) {
            if(client.reader.hasNextLine()){
            String serverUpdate = client.reader.nextLine();
                switch (serverUpdate) {
                    case "you":
                        client.playerId = Integer.parseInt(client.reader.nextLine());
                        System.out.println("You are player number " + client.playerId);
                        continue;

                    case "new":
                        System.out.println(client.reader.nextLine() + " has joined!");
                        serverUpdate = client.reader.nextLine();
                        System.out.println("Currently playing players are: ");
                        for (int i = 0; i < Integer.parseInt(serverUpdate); i++) {
                            System.out.print(client.reader.nextLine() + " ");
                        }
                        System.out.println("\nPlayer " + client.reader.nextLine() + " has the ball.");
                        continue;
                    case "left":
                        System.out.println(client.reader.nextLine() + " has left the game!");
                        String hadBall = client.reader.nextLine();
                        if(Boolean.parseBoolean(hadBall))
                        {
                            System.out.println("Player " + client.reader.nextLine() + " has now the ball.");
                        }
                        continue;
                    case "pass":
                        System.out.println(client.reader.nextLine() + " passed the ball to " + client.reader.nextLine());
                        continue;

                    case "passnoball":
                        System.out.println("You dont have the ball!");
                        continue;

                    case "passwrongcommand":
                        System.out.println("Wrong command!");
                        continue;

                    case "passnoplayer":
                        System.out.println("No such player!");
                        continue;

                    case "passsuccess":
                        int newOwner = Integer.parseInt(client.reader.nextLine());
                        System.out.println("You passed the ball to player " + newOwner + "!");
                        continue;

                    case "passreceived":
                        int oldOwner = Integer.parseInt(client.reader.nextLine());
                        System.out.println("Player " + oldOwner + " passed the ball to you. You have the ball now!" +
                                "\nTo pass the ball write 'pass'");
                        continue;

                    case "goodbye":
                        keepgoing = false;
                        continue;
                }
            }
    }
}}
