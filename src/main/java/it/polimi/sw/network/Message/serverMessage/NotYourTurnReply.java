package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
/**
 * This class represents a message sent by the server to the client informing them that it's not their turn.
 * It inherits from the `SampleServerMessage` class (likely containing common server message properties).
 *
 * This message is used to prevent a player from taking actions when it's not their turn during gameplay.
 */
public class NotYourTurnReply extends SampleServerMessage {
    private final Player misbehavingPlayer;

    /**
     * Constructor for a NotYourTurnReply message.
     *
     * @param player The player who attempted an action out of turn.
     */
    public NotYourTurnReply(Player player){
        super(TypeMessageServer.NOT_YOUR_TURN_REPLY);
        this.misbehavingPlayer = player;
    }
    /**
     * This method defines the actions to be taken by the server after sending a NotYourTurnReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */

    @Override
    public void execute(Client client) {
        //if message arrives to the misbehaved player, then show the pop up
            if(misbehavingPlayer.equals(client.getMatch().getMe())){
                client.getView().notYourTurn();
            }
    }
}
