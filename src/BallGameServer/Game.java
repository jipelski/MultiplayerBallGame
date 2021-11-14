package BallGameServer;

import java.io.PrintWriter;
import java.util.*;

/** Game class keeps track of players and allows passing.*/

public class Game {
    public final Map<Integer, Player> players = new TreeMap<>();

    public int latestId = 0;
    public boolean ballOwned = false;


    /** Creates a player object and adds it to the players tree map.
     *  Checks if there are no other players in the game.
     *  If there aren't any other players passes the ball to the new player.*/
    public Player createPlayer(PrintWriter writer)
    {
        Player player = new Player(latestId, writer);
        if(!ballOwned)
        {
            players.put(latestId, player);
            passBall(-1, latestId);
            player.hasBall = true;
            ballOwned = true;
        }
        else {
            players.put(latestId, player);
            player.hasBall = false;
        }
        latestId++;
        return player;
    }

    /** Removes the player that left from the tree map. Checks if the player that left had the ball.
     * If the player that left had the ball it passes the ball to a random player.
     * Catches exception where the player that left was the last player in the game*/
    public void playerLeft(int playerId)
    {
        try{
            if(players.get(playerId).hasBall)
            {
                players.remove(playerId);
                if(players.size() != 0)
                {
                    /** Creates a list of players that are currently in the game.
                     * Uses this list to randomly allocate the new owner of the ball.*/
                    List<Player> actualPlayers = new ArrayList(players.values());
                    Random rand = new Random();
                    actualPlayers.get(rand.nextInt(actualPlayers.size())).hasBall = true;
                    ballOwned = true;
                }
                else
                    ballOwned = false;
                //broadcast the passing of the ball to the other players and the leaving of the player

            }
            else
                players.remove(playerId);

            //broadcast the leaving of the player
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.out.println("Player doesnt exist!");
        }



    }

    /** Passes the ball from a player to another or the same player. Checks to see if the player is still connected.
     * If the player that was to receive the ball disconnects the passing stops and returns to the initial possessor.
     * If the player chose to pass to themselves and they disconnect before the passing is completed the playerLeft method
     * is called.*/
    public void passBall(int previousPossessor, int newPossessor)
    {
        //check if the newPossessor is still connected to the game
        try{
            for (Player player: players.values()) {
                if(player.getId() == newPossessor)
                {
                    if(previousPossessor != -1)
                        players.get(previousPossessor).hasBall = false;
                    players.get(newPossessor).hasBall = true;
                }
            }


        }
        catch (Exception e)
        {
            playerLeft(newPossessor);
        }

    }
}
