package it.polimi.sw.network.Message.ViewMessage;


/**
 * This enum defines the different types of messages that the client-side view can send to the server.
 * It categorizes messages based on their purpose in the communication protocol.
 */
public enum TypeMessageView {

    /**
     * Message sent by the client-side view to indicate which objective the player has chosen.
     */
    SEND_OBJECTIVE_CHOSEN,

    /**
     * Message sent by the client-side view to request resending the nickname (potentially due to rejection during initial setup).
     */
    RESEND_NICKNAME,

    /**
     * Message sent by the client-side view to inform the server about a played card.
     */
    CARD_PLAYED,

    /**
     * Message sent by the client-side view to request a facedown card to be drawn from the deck.
     */
    CARD_TO_DRAW_FACEDOWN,

    /**
     * Message sent by the client-side view to request a faceup card to be drawn from the deck (might be dependent on game mechanics).
     */
    CARD_TO_DRAW_FACEUP,

    /**
     * Message sent by the client-side view containing a chat message from the player.
     */
    CHAT_REQUEST,

    /**
     * Message sent by the client-side view indicating the number of players chosen by the player (likely during lobby creation).
     */
    NUM_PLAYERS_CHOSEN
}

