package it.polimi.sw.network.Message.serverMessage;


/**
 * This enum defines the different types of messages that the server can send to clients.
 * It categorizes messages based on their purpose in the communication protocol.
 */
public enum TypeMessageServer {

    /**
     * Response to a client request to start a game (or replay after a match).
     */
    PLAY_CODEX_REPLY,

    /**
     * Reply to a client request to set a nickname, indicating success (true) or rejection (false) if the nickname is already taken.
     */
    NICKNAME_REPLY,

    /**
     * Message type for a ping from the server to check client liveness (implementation details not specified yet).
     */
    PING,

    /**
     * Message type for error notifications sent by the server to the client.
     */
    ERROR,

    /**
     * Reply to a client request when creating a lobby, potentially containing information about the number of players allowed.
     */
    PLAYER_NUMBER_REPLY,

    /**
     * Message containing secret objectives sent by the server to a client at the beginning of the game.
     */
    SECRET_OBJ_REPLY,

    /**
     * Message containing common objectives sent by the server to all players at the beginning of the game.
     */
    COMMON_OBJ_REPLY,

    /**
     * Message sent by the server to a client informing them that it's not their turn.
     */
    NOT_YOUR_TURN_REPLY,

    /**
     * Message sent by the server to a client indicating it's their turn.
     */
    TURN_REPLY,

    /**
     * Reply to a client action of flipping a card (might be replaced by a more generic update message).
     */
    FLIP_REPLY,

     /**
      * Reply to a client action of placing a card (not currently used, replaced by update_players_reply).
      */
    PLACE_REPLY,

    /**
     * Reply to a client action of drawing a card.
     */
    DRAW_REPLY,

    /**
     * Reply containing a player's hand information (not currently used, might be replaced by update_players_reply).
     */
    HAND_REPLY,

    /**
     * Message containing updates about all players (likely including hand information).
     */
    UPDATE_PLAYERS_REPLY,

    /**
     * Message containing game result information, including the winner.
     */
    GAME_RESULT_REPLY,

    /**
     * Message containing a player's final points before the winner announcement.
     */
    FINAL_POINTS_REPLY,

    /**
    * Not sure if this is implemented here (private message request).
    */
    PRIVATE_MESSAGE_REQUEST,

    /**
     * Message containing updates about the common board with cards available to draw.
     */
    BOARD_DATA_REPLY,

    /**
     * Message sent by the server to a client informing them that a new game has been created.
     */
    CREATED_NEW_GAME,

    /**
     * Message containing the initial card and potentially other game data sent by the server to the client at the start of the game.
     */
    SEND_INITIAL_CARD,

    /**
     * Reply to a client request to create a lobby for a new game.
     */
    CREATE_LOBBY_FOR_NEW_GAME_REPLY,

    /**
     * Message informing the client that they are waiting for other players to join the lobby.
     */
    WAITING_FOR_OTHER_PLAYERS,

    /**
     * Message type for chat replies from the server to the client.
     */
    CHAT_REPLY,
    /**
        a player has finished his game, but still has to wait other players to count points
     **/
    GAME_ALMOST_DONE,

    /**
     * Message informing the client that the pion selection went wrong.
     */
    SENDING_PION_ERROR
}