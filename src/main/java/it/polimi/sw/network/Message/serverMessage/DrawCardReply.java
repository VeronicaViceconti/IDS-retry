package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import java.util.ArrayList;

/**
 * This class represents a message sent by the server to the client containing the result of a draw card request.
 * It inherits from the `SampleServerMessage` class (likely containing common server message properties).
 *
 * This message can have two purposes depending on the context:
 *  1. Responding to a draw card request: Informs the client about the card drawn (including its back)
 *      and potentially updates the common table data (facedown/faceup cards).
 *  2. Sending the initial card to a player: Provides the drawn card (including its back) without
 *      additional table data, as the common table hasn't been set yet.
 */
public class DrawCardReply extends SampleServerMessage {
    private final Card wantedCard;
    private final Card wantedCardBack;
    private final Player destination;

    private final Card faceDownGold;
    private final Card faceDownResource;
    private final ArrayList<Card> faceUpGold;
    private final ArrayList<Card> faceupResource;

    /**
     * Constructor for a DrawCardReply message responding to a draw card request.
     *
     * @param card The card drawn by the player.
     * @param cardBack The back of the drawn card.
     * @param player The player who drew the card.
     * @param faceDownGold The facedown gold card on the common table (optional, can be null).
     * @param faceDownResource The facedown resource card on the common table (optional, can be null).
     * @param faceUpGold An ArrayList containing the faceup gold cards on the common table (optional, can be null).
     * @param faceupResource An ArrayList containing the faceup resource cards on the common table (optional, can be null).
     */
    public DrawCardReply(Card card,Card cardBack, Player player, Card faceDownGold, Card faceDownResource,
                         ArrayList<Card> faceUpGold, ArrayList<Card> faceupResource){
        super(TypeMessageServer.DRAW_REPLY);
        this.wantedCard = card;
        this.destination = player;
        this.faceDownGold = faceDownGold;
        this.faceDownResource = faceDownResource;
        this.faceUpGold = new ArrayList<>(faceUpGold);
        this.faceupResource = new ArrayList<>(faceupResource);
        this.wantedCardBack = cardBack;
    }


    /**
     * Constructor for a DrawCardReply message sending the initial card to a player.
     *
     * @param card The initial card drawn by the player.
     * @param cardBack The back of the initial card.
     * @param player The player who receives the initial card.
     */
    public DrawCardReply(Card card,Card cardBack, Player player){
        super(TypeMessageServer.DRAW_REPLY);
        this.wantedCard = card;
        this. destination = player;
        this.faceDownGold = null;
        this.faceDownResource = null;
        this.faceUpGold = null;
        this.faceupResource = null;
        this.wantedCardBack = cardBack;
    }
    /**
     * This method defines the actions to be taken by the server after sending a DrawCardReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client ){

         System.out.println("DRAW CARD REPLY HA FACEUP RESOURCE: ");
        for (Card c: client.getMatch().getFaceupResource()) {
            System.out.println("card -> "+c.toString());
        }

        client.getMatch().setFacedownResource(faceDownResource);
        client.getMatch().setFaceupGold(faceUpGold);
        client.getMatch().setFacedownGold(faceDownGold);
        client.getMatch().setFaceupResource(faceupResource);

        System.out.println("FACEDOWN RESOURCE DOPO "+faceDownResource.toString());
        System.out.println("FACEDOWN GOLD DOPO "+faceDownGold.toString());
        if (destination.equals(client.getMatch().getMe())) {

            client.getView().chatWait();

            client.getMatch().getMe().getHand().add(wantedCard);
            client.getMatch().getMe().getHandBack().add(wantedCardBack);
            client.getView().savePlayerHandBack(client.getMatch().getMe().getHandBack());
            client.getView().showPlayerHand(client.getMatch().getMe().getHand());

            client.getView().chatUnblockWait();
        } else {
            client.getView().notYourTurn();
        }


        if(faceDownGold != null){
            client.getView().showCommonTable(faceDownGold,faceDownResource,faceUpGold,faceupResource);
        }
    }
}
