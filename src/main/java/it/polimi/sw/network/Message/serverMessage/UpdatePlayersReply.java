package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * This class represents a message sent by the server to all clients containing updates about a specific player after their turn.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message is used to synchronize the game state among all clients by reflecting changes made by a player during their turn.
 */
public class UpdatePlayersReply extends SampleServerMessage {
    //private String updateDetail; // a comment if needed
    private final Player player;
    private final int points;
    private final Card playedCard;
    private final Integer x;
    private final Integer y;

    private final HashMap<String, Integer> numOfResourceAndObject;

    private final ArrayList<Card> phand;
    private final ArrayList<Card> handBack;

    private final boolean lastTurn;
    /**
     * Constructor for an UpdatePlayersReply message.
     *
     * @param player1 The player whose information is being updated.
     * @param point The updated player's points.
     * @param placedCard The card played by the updated player.
     * @param x The x-coordinate of the played card's position (on the board).
     * @param y The y-coordinate of the played card's position (on the board).
     * @param numOfResourceAndObject A map containing the player's updated resource and object counts.
     * @param hand The updated player's hand (front view).
     * @param handBack The updated player's hand (back view).
     */
    public UpdatePlayersReply(Player player1, int point, Card placedCard, Integer x, Integer y,
                              HashMap<String, Integer> numOfResourceAndObject, ArrayList<Card> hand,ArrayList<Card> handBack,
                              boolean endGame){
        super(TypeMessageServer.UPDATE_PLAYERS_REPLY);
        this.player = player1;
        this.points = point;
        this.playedCard = placedCard;
        this.x =x;
        this.y = y;
        this.numOfResourceAndObject = new HashMap<>(numOfResourceAndObject);
        this.phand = new ArrayList<>(hand);
        this.handBack = new ArrayList<>(handBack);
        this.lastTurn = endGame;
    }

    /**
     * This method defines the actions to be taken by the server after sending an UpdatePlayersReply message to a client.
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
        //se sono il giocatore corrente, modifico le mie varie cose
        if(client.getMatch().getMe().equals(player)){

            client.getMatch().getMe().setPoints(points);
            client.getMatch().getMe().setNumOfResourceAndObject(numOfResourceAndObject);
            client.getMatch().getMe().setHand(phand);
            client.getMatch().getMe().setHandBack(handBack);
            client.getMatch().getMe().addCardToMap(playedCard, x, y);
            client.getView().addToTimeLine(playedCard);

            client.getView().chatWait(); //chat stores
            client.getView().updatePlayerPlayCard(player,playedCard, x, y, numOfResourceAndObject,phand,handBack ,points);
            client.getView().showCommonTable(client.getMatch().getFacedownGold(),
                                                client.getMatch().getFacedownResource(),
                                                client.getMatch().getFaceupGold(),
                                                client.getMatch().getFaceupResource());
            client.getView().showPlayerHand(client.getMatch().getMe().getHand());

  /*          System.out.println("Timeline during updating card->");
            for(Card c: player.getTimeline())
                System.out.println("Cartaa -> "+c.toString());
*/
            if(lastTurn) {
                client.getView().drawCard(); // here chat unlocks
            }
        }else{ //se non è il mio turno, aggiungo il nuovo manoscritto anche nel mio model, se mai volessi vederlo
            for(Player p: client.getMatch().getTotPlayers()){
                if(p.equals(player)){
                    p.addCardToMap(playedCard,x,y);
                    p.setPoints(points);
                    p.addToTimeline(playedCard);
                    if(client.getView() instanceof GUI)
                        client.getView().updatePlayerPlayCard(p,playedCard,x,y,null,null,null,points);
                }
            }
        }

    }
}
