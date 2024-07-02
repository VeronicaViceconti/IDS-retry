package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a message sent by the server to the client containing secret objectives and potentially the player's hand.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message is used to deliver secret objectives to a player at the beginning of the game and potentially update the client-side hand view.
 */
public class SecretObjReply extends SampleServerMessage {
    private final Objective secObjOne;
    private final Objective secObjTwo;

    private final Player player;

    private final ArrayList<Card> myHand;
    private final ArrayList<Card> myHandBack;
    private final  HashMap<Card, Integer[]> manuscript;

    /**
     * Constructor for a SecretObjReply message.
     *
     * @param obj1 The first secret objective for the player.
     * @param obj2 The second secret objective for the player.
     * @param p The player who receives the secret objectives.
     * @param hand The player's hand of cards (optional, might be null if not needed).
     */
    public SecretObjReply(Objective obj1, Objective obj2, Player p, ArrayList<Card> hand,ArrayList<Card> myHandBack,HashMap<Card,Integer[]> manuscript){
        super(TypeMessageServer.SECRET_OBJ_REPLY);
        this.secObjOne = obj1;
        this.secObjTwo = obj2;

        this.player = p;
        this.myHand = new ArrayList<>(hand);
        this.myHandBack = new ArrayList<>(myHandBack);
        this.manuscript = new HashMap<>(manuscript);
    }

    /**
     * This method defines the actions to be taken by the server after sending a SecretObjReply message to a client.
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
        if(player.equals(client.getMatch().getMe())){
            client.getMatch().getMe().setHand(myHand);
            client.getMatch().getMe().setHandBack(myHandBack);
            client.getMatch().getMe().setMap(manuscript);


            client.getView().showPlayerHand(myHand);
            client.getView().savePlayerHandBack(myHandBack);
            client.getView().chatWait();
            client.getView().showPlayerTable(manuscript);

            client.getView().secObjtoChoose(secObjOne, secObjTwo);
            client.getView().chatUnblockWait();


        }else{
            if(client.getMatch().getAPlayer(player) != null) {
                client.getMatch().getAPlayer(player).setMap(manuscript);
                if(client.getView() instanceof GUI) {
                    client.getView().updatePlayerPlayCard(player, manuscript.keySet().iterator().next(), 0, 0, null, null, null, 0);
                }
            }
        }
    }
}
