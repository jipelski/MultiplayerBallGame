package BallGameServer;

import java.io.PrintWriter;

/** Creates a Player object*/

public class Player {
    public final int id;
    public boolean hasBall;
    public PrintWriter writer;

    public Player(int idInit, PrintWriter writer)
    {
        this.id = idInit;
        this.hasBall = false;
        this.writer = writer;
    }

    public int getId()
    {
        return id;
    }


}
