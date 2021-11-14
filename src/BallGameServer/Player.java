package BallGameServer;

/** Creates a Player object*/

public class Player {
    public final int id;
    public boolean hasBall;

    public Player(int idInit)
    {
        this.id = idInit;
        this.hasBall = false;
    }

    public int getId()
    {
        return id;
    }


}
