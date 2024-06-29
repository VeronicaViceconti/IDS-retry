package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;

import java.util.ArrayList;
/**
 * This class represents a message sent by the server to all clients announcing the winner(s) of the game.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message is used to mark the end of the game and inform all players about the winner(s).
 */
public class WinnerReply extends SampleServerMessage {
    private final ArrayList<Player> winners;

    public WinnerReply(ArrayList<Player> players){
        super(TypeMessageServer.GAME_RESULT_REPLY);
        this.winners = players;
    }
    /**
     * This method defines the actions to be taken by the server after sending a WinnerReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) {
        client.getView().announceWinner(winners);
    }
}
