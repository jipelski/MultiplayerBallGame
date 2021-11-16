package BallGameClient;

import java.util.Scanner;

public class ClientProgram {
    public static void main(String [] args) throws Exception {
        try(Client client = new Client())
        {
            System.out.println(client.playerId);
            boolean connected = true;
            Scanner in = new Scanner(System.in);
                while (connected)
                {
                    String command = in.nextLine();
                    switch (command)
                    {
                        case "leave":
                            client.leave();
                            connected = false;

                        case "pass":
                            System.out.println("Enter the id of the player you want to pass the ball to:");
                            int passPlayer = Integer.parseInt(in.nextLine());
                            System.out.println(client.pass(passPlayer));

                    }
                }
        }
    }
}
