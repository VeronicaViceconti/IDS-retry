package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;

import java.awt.geom.Point2D;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This class represents a message sent by the server to all clients indicating whose turn it is to play.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message is used to synchronize the game state among all players and trigger UI updates on the client-side.
 */
public class TurnReply extends SampleServerMessage {
    private final Player currp;

    private final ArrayList<Point2D> positions;

    /**
     * Constructor for a TurnReply message.
     *
     * @param player The player whose turn it is to play.
     * @param pos A list of valid positions (e.g., for card placement) relevant to the current turn.
     */

    public TurnReply(Player player, ArrayList<Point2D> pos){
        super(TypeMessageServer.TURN_REPLY);
        this.currp = player;
        this.positions = new ArrayList<>(pos);
    }
    /**
     * This method defines the actions to be taken by the server after sending a TurnReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @throws RemoteException Indicates a potential exception if remote communication fails.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) throws RemoteException {


        client.getMatch().setCurrPlayer(currp);
        if(currp.equals(client.getMatch().getMe())){
            client.getMatch().getMe().resetAvPos(positions);

            client.getView().resetAvPos(positions);
            client.getView().chatWait();

            client.getView().myTurnNotification(client.getMatch().getMe().getMap(), client.getMatch().getMe().getHand(),client.getMatch().getMe().getHandBack());


        }
    }
}
