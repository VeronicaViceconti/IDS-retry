package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Card;
import it.polimi.sw.model.Player;

import java.util.NoSuchElementException;
/**
 * This class represents a message sent by the client requesting to place a card on the game board.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID, the card object to be placed, the desired coordinates (x, y), and the ID of the player making the request.
 */
public class PlaceCardRequest extends SampleClientMessage {
    private final Card card;
    private final int x;
    private final int y;

    private final int player;

    /**
     * Constructor for a PlaceCardRequest message.
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param toPlaceCard The `Card` object representing the card to be placed.
     * @param x The desired x-coordinate for placement on the game board.
     * @param y The desired y-coordinate for placement on the game board.
     * @param id_player The ID of the player making the request.
     */
    public PlaceCardRequest(int lobby,Card toPlaceCard, int x, int y, int id_player){
        super(TypeMessageClient.PLACE_REQUEST,lobby, id_player);
        this.card = toPlaceCard;
        this.x = x;
        this.y = y;
        this.player = id_player;
    }
    /**
     * Retrieves the `Card` object representing the card to be placed.
     *
     * @return The `Card` object.
     */
    public Card getCard(){
        return card;
    }
    /**
     * Retrieves the desired x-coordinate for placement on the game board.
     *
     * @return The x-coordinate.
     */
    public int getX(){
        return x;
    }
    /**
     * Retrieves the desired y-coordinate for placement on the game board.
     *
     * @return The y-coordinate.
     */
    public int getY(){
        return y;
    }
    /**
     * Retrieves the ID of the player making the request.
     *
     * @return The player ID.
     */
    public int getPlayerID(){return player;}
    /**
     * This method defines the actions to be taken by the server upon receiving a PlaceCardRequest message.
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this method overrides a method from the parent class (likely `SampleClientMessage`).
     */
    @Override
    public void execute(GameControllerServer gcs) {
        Player p = gcs.currgame.getTotPlayers().stream()
                .filter(x -> x.getId() == this.getPlayerID())
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException( "Player with id" + this.getPlayerID()+" not found in the list of players"));

        gcs.placeCard(p,card,x,y);
    }

}
