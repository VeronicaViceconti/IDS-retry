package it.polimi.sw.network.Message.ViewMessage;

import it.polimi.sw.model.Card;
/**
 * This class represents a message sent by the client-side view to the server informing the server about a card played by the player.
 * It inherits from the `SampleViewMessage` class.
 *
 * This message is used during gameplay to communicate the player's chosen card and its intended placement on the board (if applicable).
 */
public class SendingCardPlayed extends SampleViewMessage{

    private final Card card;
    private final Integer[] coords;

    /**
     * Constructor for a SendingCardPlayed message.
     *
     * @param card The `Card` object representing the card played by the player.
     * @param coords An array of two Integers representing the x and y coordinates of the played card's intended position on the board (optional, might be null for special cases like initial card placement).
     */
    public SendingCardPlayed(Card card, Integer[] coords) {
        super(TypeMessageView.CARD_PLAYED);
        this.card = card;
        this.coords = coords;
    }
    /**
     * Getter method to access the played card.
     *
     * @return The `Card` object representing the card played by the player.
     */
    public Card getCard() {
        return card;
    }
    /**
     * Getter method to access the played card's intended position on the board.
     *
     * @return An array of two Integers representing the x and y coordinates (might be null for special cases).
     */
    public Integer[] getCoords() {
        return coords;
    }
}
