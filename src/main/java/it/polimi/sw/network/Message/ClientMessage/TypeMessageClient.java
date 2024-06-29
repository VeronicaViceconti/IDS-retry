package it.polimi.sw.network.Message.ClientMessage;
/**
 * This enum defines the different message types that can be sent by the client to the server.
 * These messages likely trigger specific actions or responses from the server.
 */
public enum TypeMessageClient {
    /**
     * Connection request message sent at the beginning to identify the client.
     */
    CONNECTION_REQUEST,
    /**
     * Player number request message, likely used when creating a lobby.
     */
    PLAYER_NUMBER_REQUEST,

    /**
     * Request to get the client's current points (score).
     */
    MYPOINTS_REQUEST,

    /**
     * Request to place something on the game board (action details not specified here).
     */
    PLACE_REQUEST,

    /**
     * Request to draw cards from the table (action details not specified here).
     */
    DRAW_TABLE_REQUEST,

    /**
     * Request to draw cards from the deck (action details not specified here).
     */
    DRAW_DECK_REQUEST,

    /**
     * Request to send a public message to the chat.
     */
    PUBLIC_MESSAGE_REQUEST,

    /**
     * Request to send a private message to a specific player.
     */
    PRIVATE_MESSAGE_REQUEST,

    /**
     * Request to get the number of resources a player has.
     */
    NUM_OF_RESOURCE_REQUEST,

    /**
     * Request to get the client's private objectives.
     */
    PRIVATE_OBJECTIVE_REQUEST,

    /**
     * Request to set or change the client's nickname.
     */
    NICKNAME_REQUEST,

    /**
     * Message to check if the server is still connected (ping).
     */
    PING,

    /**
     * Message indicating that the client is disconnecting from the game.
     */
    DISCONNECTION,

    /**
     * Request to get the board data (likely the current state of the game board).
     */
    BOARD_DATA_REQUEST,

    /**
     * Request to exit the game.
     */
    EXIT_REQUEST
}

