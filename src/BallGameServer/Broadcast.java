package BallGameServer;

import java.util.ArrayList;
import java.util.List;

import static BallGameServer.ServerProgram.players;

public class Broadcast {


    public Broadcast(String i_am_alive) {
    }

    void broadcastJoin()
    {
        System.out.println("Player " + players.indexOf(players.get(players.size()-1)) + " has joined. Current players are:");
        for (Thread x : players
             ) {
            System.out.print(players.indexOf(x) + "  ");
        }
    }

    void broadcastLeave()
    {
        System.out.println("0");
    }
}
