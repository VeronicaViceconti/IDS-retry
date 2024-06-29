package it.polimi.sw.network.Message.serverMessage;

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
        //messaggio inviato a tutti, tutti settano il currp
        //se client.getMe è il currP allora faccio partire il suo turno

        client.getMatch().setCurrPlayer(currp);
        if(currp.equals(client.getMatch().getMe())){
            client.getMatch().getMe().resetAvPos(positions);
            client.getView().resetAvPos(positions);

            client.getView().chatWait(); //chat stores

            client.getView().myTurnNotification(client.getMatch().getMe().getMap(), client.getMatch().getMe().getHand(),client.getMatch().getMe().getHandBack());
            //client.getView().showAvailablePositions(); should be called inside myturnNotification
            // chat unblocks within my turn notification
        }else{
            //Yana: I would remove .notYourTurn. player is already in reader
            System.out.println("NOT YOUR TURN");
            //client.getView().notYourTurn();
            //client.getView().readNotYourTurnInput(); viene creato un thread per reader alla fine di drawcard reader
        }
    }
}
