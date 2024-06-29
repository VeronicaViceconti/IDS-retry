package it.polimi.sw.network.ClientModel;

import it.polimi.sw.model.*;


import java.util.ArrayList;

/**
 * This class likely represents a game session (Match) between players.
 */
 public class Match {

    /** Internal state variables suggest this class might manage:
     *  * Players:
     *      * `me`: The player representing the current user (local player).
     *      * `playersList`: A list containing information about all players in the game.
     *      * `currPlayerSmall`: Potentially a reference to the current player.
     */
     private Player me;
     private ArrayList<Player> playersList;
     private Player currPlayerSmall;
     /** Cards:
      *      * `faceupGold`: A list of face-up gold cards in the game.
      *      * `faceupResource`: A list of face-up resource cards in the game.
      *      * `facedownGold`: A single facedown gold card.
      *      * `facedownResource`: A single facedown resource card.
      */
     private ArrayList<Card> faceupGold;
     private ArrayList<Card> faceupResource;
     private Card facedownGold;
     private Card facedownResource;

     /** Objectives:
      *      * `commonObjective`: A list containing the common objectives for all players.
      */
    private ArrayList<Objective> commonObjective;

    /**
     * Constructor for the Match class that initializes a game session with players.
     *
     * @param playersList An `ArrayList` containing information about all players in the game.
     * @param myNickName The nickname of the current user (local player).
     */
    public Match(ArrayList<Player> playersList, String myNickName) {
        this.facedownResource = new ResourceCard();
        this.facedownGold = new GoldCard();
        this.faceupResource = new ArrayList<>();
        this.faceupGold = new ArrayList<>();

        this.commonObjective = new ArrayList<>();
        this.playersList = new ArrayList<Player>(playersList);
        for(Player p: playersList){
            if(p.getNickName().equals(myNickName)){
                this.me = p;
            }
        }

        this.currPlayerSmall = null;
    }
    /**
     * Retrieves a copy of the list containing all players in the game.
     *
     * @return An unmodifiable `List` containing the `Player` objects.
     */
    public ArrayList<Player> getTotPlayers() {
        return playersList;
    }
    /**
     * Retrieves the player representing the current user (local player).
     *
     * @return The `Player` object representing the local player, or null if not found during initialization.
     */
    public Player getMe(){return me;}
    /**
     * Retrieves the player whose turn it is currently.
     *
     * @return The `Player` object representing the current player, or null if not yet set.
     */
    public Player getCurrPlayer(){return currPlayerSmall;}
    /**
     * Sets the current player whose turn it is.
     *
     * @param curr The `Player` object representing the player whose turn is starting.
     */
    public void setCurrPlayer(Player curr) {this.currPlayerSmall = curr;}
    /**
     * Retrieves the facedown gold card from the game.
     *
     * @return The `Card` object representing the facedown gold card, or null if not yet set.
     */
    public Card getFacedownGold() {
            return facedownGold;
    }
    /**
     * Sets the facedown gold card in the game.
     *
     * @param facedownGold The `Card` object representing the facedown gold card.
     */
    public void setFacedownGold(Card facedownGold) {
            this.facedownGold = facedownGold;
    }
    /**
     * Retrieves a copy of the list containing all faceup gold cards in the game.
     *
     * @return List containing the `Card` objects representing the faceup gold cards.
     */
    public ArrayList<Card> getFaceupGold() {
            return faceupGold;
    }
    /**
     * Sets the list of faceup gold cards in the game.
     *
     * @param faceupGold An `ArrayList` containing the `Card` objects representing the faceup gold cards.
     */
    public void setFaceupGold(ArrayList<Card> faceupGold) {
            this.faceupGold = faceupGold;
    }
    /**
     * Retrieves a copy of the list containing all faceup resource cards in the game.
     *
     * @return List containing the `Card` objects representing the faceup resource cards.
     */
    public ArrayList<Card> getFaceupResource() {
            return faceupResource;
    }
    /**
     * Sets the list of faceup resource cards in the game.
     *
     * @param faceupResource An `ArrayList` containing the `Card` objects representing the faceup resource cards.
     */

    public void setFaceupResource(ArrayList<Card> faceupResource) {
            this.faceupResource = faceupResource;
    }

    /**
     * Retrieves the facedown resource card from the game.
     *
     * @return The `Card` object representing the facedown resource card, or null if not yet set.
     */
    public Card getFacedownResource() {
        return facedownResource;
    }
    /**
     * Sets the facedown resource card in the game.
     *
     * @param facedownResource The `Card` object representing the facedown resource card.
     */
    public void setFacedownResource(Card facedownResource) {
        this.facedownResource = facedownResource;
    }
    /**
     * Retrieves a copy of the list containing the common objectives for all players.
     *
     * @return An unmodifiable `List` containing the `Objective` instances.
     */
    public ArrayList<Objective> getCommonObjective() {
            return commonObjective;
    }
    /**
     * Sets the list of common objectives for all players.
     *
     * @param o An `ArrayList` containing the `Objective` instances representing the common objectives.
     */
    public void setCommonObjective(ArrayList<Objective> o) {
            commonObjective = o;
    }

    public Player getAPlayer(Player player) {
        for (Player p:playersList) {
            if(p.equals(player))
                return p;
        }
        return null;
    }

 }
