package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class represents a message sent by the server to the client containing the initial card and potentially other game data at the start of the game.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message is used to provide the client with their initial card, hand (front and back views), and potentially other information relevant to the game state at the beginning.
 */
public class SendInitialCard extends SampleServerMessage{

    private final Player p;
    private final HashMap<Card, Integer[]> man;
    private final ArrayList<Card> handFront;
    private final ArrayList<Card> handBack;
    private final int idPlayer;
    /**
     * Constructor for a SendInitialCard message.
     *
     * @param initialCardFront The initial face-up card for the player.
     * @param initialCardBack The back of the initial card.
     * @param p The player who receives the initial card.
     * @param idPlayer The unique identifier for the player within the game.
     */
    public SendInitialCard(Card initialCardFront,Card initialCardBack, Player p,int idPlayer) {
        super(TypeMessageServer.SEND_INITIAL_CARD);
        this.p = p;
        this.man = new HashMap<>();
        this.handFront = new ArrayList<>();
        this.handBack = new ArrayList<>();
        this.handFront.add(initialCardFront);
        this.handBack.add(initialCardBack);
        this.idPlayer = idPlayer;
    }
    /**
     * This method defines the actions to be taken by the server after sending a SendInitialCard message to a client.
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
        if(p.equals(client.getMatch().getMe())){
            client.getMatch().getMe().setHand(handFront);
            client.getMatch().getMe().setHandBack(handBack);
            client.getMatch().getMe().setId(idPlayer);

            client.getView().chatWait();
            client.getView().savePlayerHandBack(handBack);
            if(client.getView() instanceof GUI)
                client.getView().showPlayerHand(client.getMatch().getMe().getHand());
            client.getView().myTurnNotification(man, handFront,handBack); //unblock inside
        }
    }
}
