package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Card;
import it.polimi.sw.model.Player;
/**
 * This class represents a message sent by the client requesting to draw a card.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID, the deck name from which to draw, and the ID of the player making the request.
 * It can optionally specify a position within the deck.
 */
public class DrawCardRequest extends SampleClientMessage {
    private int position;
    private String deck;
    /**
     * Constructor for a DrawCardRequest message specifying a position within the deck (applicable to decks with order).
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param deck The name of the deck from which to draw the card.
     * @param pose The desired position within the deck.
     * @param id_player The ID of the player making the request.
     */

    public DrawCardRequest(int lobby,String deck, int pose, int id_player){

        super(TypeMessageClient.DRAW_TABLE_REQUEST,lobby, id_player);
        this.position = pose;
        this.deck = deck;
    }

    /**
     * Constructor for a DrawCardRequest message without specifying a position (applicable to decks without order).
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param deck The name of the deck from which to draw the card.
     * @param id_player The ID of the player making the request.
     */
    public DrawCardRequest(int lobby,String deck, int id_player){
        //mettere il giusto messaggio
        super(TypeMessageClient.DRAW_DECK_REQUEST,lobby, id_player);
        this.position = -1;
        this.deck = deck;
    }
    /**
     * Retrieves the desired position within the deck (if specified).
     *
     * @return The position.
     */
        public int getPosition(){
            return position;
        }
    /**
     * Retrieves the name of the deck from which to draw the card.
     *
     * @return The deck name.
     */
        public String getDeck(){
            return deck;
        }

    /**
     * use the controller to manage the draw card request
      * @param gcs controller
     */
    @Override
    public void execute(GameControllerServer gcs) {
        Player p = gcs.currgame.getTotPlayers().stream().filter(x -> x.getId() == this.getId_player()).findFirst().get();
        if(position == -1)
            gcs.drawCardFromDeck(p,deck);
        else
            gcs.drawCardFromTable(p,position-1,deck);
    }
}
