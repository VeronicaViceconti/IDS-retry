package it.polimi.sw.model;
/**
 * Defines the different game states that the game can be in.
 */
public enum GameState {
    /**
     * The game is just starting and initial setup is happening.
     */
    STARTGAME,
    /**
     * The game has ended and scorekeeping are in progress.
     */
    ENDGAME,
    /**
     * It's the current player's turn to place a card.
     */
    PLACECARD,
    /**
     * It's the current player's turn to draw a card.
     */
    DRAWCARD,

    /**
     * It's not the current player's turn.
     */
    NOT_MY_TURN, // during this state the controller has to evaluate if the game is finished,
    // change the curr player or calculate the winner


}
