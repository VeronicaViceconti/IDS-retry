package it.polimi.sw.network.Message.ViewMessage;
/**
 * This class represents a message sent by the client-side view to the server indicating the player's request to draw a card.
 * It inherits from the `SampleViewMessage` class.
 *
 * This message is used to communicate the player's choice of which card to draw from the deck (facedown or faceup, depending on game mechanics).
 */
public class SendingCardToDraw extends SampleViewMessage{

    private final int deck;
    private final int pose;
    /**
     * Constructor for a SendingCardToDraw message requesting a faceup card.
     *
     * @param whichDeckFaceUp The index or identifier of the desired faceup deck to draw from.
     * @param whichPosition The position where the faceup card should be placed (might be used for specific game mechanics).
     */
    public SendingCardToDraw(int whichDeckFaceUp,int whichPosition) {
        super(TypeMessageView.CARD_TO_DRAW_FACEUP);
        deck = whichDeckFaceUp;
        pose = whichPosition;
    }
    /**
     * Constructor for a SendingCardToDraw message requesting a facedown card.
     *
     * @param whichDeckFaceDown The index or identifier of the desired facedown deck to draw from.
     */
    public SendingCardToDraw(int whichDeckFaceDown) {
        super(TypeMessageView.CARD_TO_DRAW_FACEDOWN);
        deck = whichDeckFaceDown;
        pose = -1;
    }
    /**
     * Getter method to access the deck identifier from which the card should be drawn.
     *
     * @return The integer representing the chosen deck.
     */
    public int getDeck() {
        return deck;
    }
    /**
     * Getter method to access the position where the faceup card should be placed (applicable only for faceup cards).
     *
     * @return The integer representing the position (might be -1 if not applicable).
     */
    public int getPose() {
        return pose;
    }
}
