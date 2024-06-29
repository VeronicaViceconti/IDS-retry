package it.polimi.sw.network.Message.ViewMessage;

import it.polimi.sw.model.Pion;

import static it.polimi.sw.network.Message.ViewMessage.TypeMessageView.NUM_PLAYERS_CHOSEN;

/**
 * This class represents a message sent by the client-side view to the server indicating the number of players and the player's chosen pawn (pion) for a new game.
 * It inherits from the `SampleViewMessage` class.
 *
 * This message is used during lobby creation or when a player joins an existing game to communicate their preferences to the server.
 */

public class SendingNumPlayersAndPion extends SampleViewMessage{
    private final int num;
    private final Pion pion;

    /**
     * Num of players = 0 if the player is joining a game, != 0 if is creating new game
     * @param numP number of players in the game
     * @param pion pion of the current player
     */
    public SendingNumPlayersAndPion(int numP,Pion pion){
        super(NUM_PLAYERS_CHOSEN);
        this.num = numP;
        this.pion = pion;
    }
    /**
     * Getter method to access the chosen pawn (pion).
     *
     * @return The `Pion` object representing the player's chosen pawn.
     */
    public Pion getPion() {
        return pion;
    }
    /**
     * Getter method to access the number of players.
     *
     * @return The number of players chosen by the player (0 indicates joining an existing game).
     */
    public int getNum(){
        return num;
    }

}
