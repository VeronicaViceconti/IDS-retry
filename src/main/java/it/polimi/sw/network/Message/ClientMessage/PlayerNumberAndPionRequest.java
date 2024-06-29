package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Pion;

/**
 * This class represents a message sent by the client to the server requesting to join a game or create a new one.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message is used during the initial game setup phase. It communicates the player's chosen nickname, desired number of players (0 for joining existing game), and preferred pawn (pion).
 */
public class PlayerNumberAndPionRequest extends SampleClientMessage {
        int numPlayer;
        Pion pion;
        String nickname;
    /**
     * Constructor for a PlayerNumberAndPionRequest message.
     *
     * @param lobby The lobby ID.
     * @param nickname The player's chosen nickname.
     * @param num The desired number of players.
     * @param pion The `Pion` object representing the player's preferred pawn.
     */
    public PlayerNumberAndPionRequest(int lobby,String nickname, int num, Pion pion){
        super(TypeMessageClient.PLAYER_NUMBER_REQUEST,lobby,0);
        this.numPlayer = num;
        this.pion = pion;
        this.nickname = nickname;
    }
    /**
     * Getter method to access the player's chosen nickname.
     *
     * @return The String containing the player's nickname.
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * Getter method to access the desired number of players.
     *
     * @return The integer representing the desired number of players (0 for joining).
     */
    public int getNum(){
            return numPlayer;
        }
    /**
     * Getter method to access the player's preferred pawn (pion).
     *
     * @return The `Pion` object representing the chosen pawn.
     */
    public Pion getPion() {
        return pion;
    }
    /**
     * This method is likely overridden for server-side processing of the message.
     * However, the provided implementation is empty.
     *
     * @param gcs The `GameControllerServer` object representing the server-side game controller.
     */
    @Override
    public void execute(GameControllerServer gcs) {

    }
}
