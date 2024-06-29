package it.polimi.sw.network.Message.serverMessage;

/**
 * This enum defines the different types of errors that can occur during gameplay.
 * It's likely used by the server to identify and communicate errors to clients.
 */
public enum ErrorType {

 /**
  * The player attempted to place a card when it wasn't their turn or the action was not allowed at that moment.
  */
 NOT_THE_RIGHT_MOMENT_TO_PLACE_CARD,

 /**
  * The player tried to place a card that is not in their hand.
  */
 CARD_NOT_IN_HAND,

 /**
  * The player attempted to place a card that has already been used.
  */
 USED_CARD,

 /**
  * The chosen position for placing a card would cover two corners of the same existing card (invalid placement).
  */
 COVERED_TWO_CORNERS,

 /**
  * The player placed a card in an invalid location (not connected to any existing cards).
  */
 NO_CORNERS_COVERED,

 /**
  * The placed card covered a hidden corner of another card (illegal placement).
  */
 HIDDEN_CORNER_COVERED,

 /**
  * The player doesn't have enough resources to place a gold card.
  */
 NOT_ENOUGH_RESOURCES,

 /**
  * Incorrect parameters were provided in a DrawCard request (details depend on server implementation).
  */
 INCORRECT_PARAMETRES_DRAWCARD,

 /**
  * Incorrect parameters were provided in a PlaceCard request (details depend on server implementation).
  */
 INCORRECT_PARAMETRES_PLACECARD,

 /**
  * The player attempted to draw a card before playing a card (might not be implemented yet).
  */
 PLAYER_HAS_3_CARDS,

 /**
  * The deck from which the player tries to draw a card is empty.
  */
 EMPTY_DECK,

 /**
  * The player attempted to draw a card when it wasn't their turn or the action was not allowed at that moment (might not be implemented yet).
  */
 NOT_THE_RIGHT_MOMENT_TO_DRAW_CARD,

 NO_DESTINATION
}