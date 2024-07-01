package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.*;
import it.polimi.sw.network.Client;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * This class likely represents a message sent by the server to the client containing game board data.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message might be used to provide the client with the following information about the game board:
 *  * Facedown gold card (optional)
 *  * Facedown resource card (optional)
 *  * List of faceup gold cards
 *  * List of faceup resource cards
 *  * List of common objectives
 *  * List of all players in the game
 */
//need to be controlled. common area updates
public class BoardDataReply extends SampleServerMessage {
    private final Card facedownGold;
    private final Card facedownRes;

    private final ArrayList<Card> faceupGold;

    private final ArrayList<Card> faceupRes;

    private final ArrayList<Objective> commonObj;

    private final ArrayList<Player> allPlayers;
    private final ArrayList<Pion> pions;
    private final Player p;

    /**
     * Constructor used at the beginning,when we have to create the common table and the common objectives
     * @param fdG the back of the golden card in the common table
     * @param fdR the back of the resource card in the common table
     * @param fuG the front of the 2 golden cards in the common table
     * @param fuR the front of the 2 resource cards in the common table
     * @param commonObj common objectives of the game
     */

      public BoardDataReply(Card fdG, Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,
                            ArrayList<Objective> commonObj, ArrayList<Player> competitors,Player p,ArrayList<Pion> pions){
        super(TypeMessageServer.BOARD_DATA_REPLY);
        this.facedownGold = new GoldCard((GoldCard) fdG);
        this.facedownRes = new ResourceCard((ResourceCard) fdR);
        this.faceupGold = new ArrayList<>(fuG);
        this.faceupRes = new ArrayList<>(fuR);
        this.commonObj = new ArrayList<>(commonObj);
        this.allPlayers = new ArrayList<>(competitors);
        this.p = p;
        this.pions = pions;
    }

    /**
     *Constructor used everytime we have to update the common table but the common objectives are already setted
     * @param fdG the back of the golden card in the common table
     * @param fdR the back of the resource card in the common table
     * @param fuG the front of the 2 golden cards in the common table
     * @param fuR the front of the 2 resource cards in the common table
     */
    public BoardDataReply(Card fdG, Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,Player p){

        super(TypeMessageServer.BOARD_DATA_REPLY);
        this.facedownGold = new GoldCard((GoldCard) fdG);
        this.facedownRes = new ResourceCard((ResourceCard) fdR);
        this.faceupGold = new ArrayList<>(fuG);
        this.faceupRes = new ArrayList<>(fuR);
        this.commonObj = null;
        this.allPlayers = null;
        this.p = p;
        this.pions = null;
    }
    /**
     * This method likely defines the actions to be taken by the server after sending a BoardDataReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     * @throws RemoteException Indicates a potential exception if remote communication fails.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) throws RemoteException {
        if(commonObj != null)
            client.startMatch(allPlayers);
        client.getMatch().setFacedownGold(facedownGold);
        client.getMatch().setFacedownResource(facedownRes);
        client.getMatch().setFaceupGold(faceupGold);
        client.getMatch().setFaceupResource(faceupRes);


        if(commonObj != null){

            client.getView().showGameStart(); //welcome
            client.getView().initGame();
            client.getMatch().setCommonObjective(commonObj);
            client.getView().setBoardData(facedownGold, facedownRes, faceupGold,faceupRes,commonObj);

            //pass all the player's name
            ArrayList<String> names = new ArrayList<>();
            for (Player p: allPlayers) {
                names.add(p.getNickName());

            }
            client.getView().initializePions(allPlayers.indexOf(client.getMatch().getMe()),pions);
            client.getView().createTabbedManuscripts(names);
        }else {
            //if it's me, it's time to re-show the manuscript
            if (client.getMatch().getMe().equals(p) || client.getView() instanceof GUI)
                client.getView().showCommonTable(facedownGold, facedownRes, faceupGold, faceupRes);
        }
    }
}
