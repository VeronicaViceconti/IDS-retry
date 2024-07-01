package it.polimi.sw.model;

import it.polimi.sw.Observer.Observable;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.*;
/**
 * Represents a player in the game.
 */
public class Player extends Observable implements Serializable {
    /** The id of the player (1,2,3,4).*/
    private int id;
    /** The player's chosen nickname. */
    private String nickName;
    /** The points of the player*/
    private int points;
    /** The player's hand of cards. */
    private ArrayList<Card> hand;
    /** The player's hand of the back of the cards. */
    private ArrayList<Card> handBack;
    /** The player's secret objective. */
    private Objective objective;
    /** The player's game piece (pion). */
    private Pion pion;
    /** A HashMap linking cards to their coordinates. */
    private HashMap<Card, Integer[]> map;
    /** A HashMap tracking the number of resources and objectives the player has. */
    private HashMap<String, Integer> numOfResourceAndObject;
    /** Available positions for plays card. */
    private ArrayList<Point2D> availablePositions;

    private ArrayList<Card> timeline;



    public void setTimeline(ArrayList<Card> timeline) {
        this.timeline = timeline;
    }

    public ArrayList<Card> getTimeline() {
        return timeline;
    }

    public void addToTimeline(Card c){
        this.timeline.add(c);
    }

    /**
     * Retrieves the player's unique identifier.
     *
     * @return The player's ID.
     */

    public int getId() {
        return id;
    }
    /**
     * Constructs a new `Player` object with the specified nickname and game piece (pion).
     *
     * @param nickName The player's chosen nickname.
     * @param pion The player's game piece (pion).
     */
    public Player(String nickName, Pion pion) {
        this.nickName = nickName;
        this.availablePositions = new ArrayList<Point2D>();
        availablePositions.add(new Point2D.Double()); //constructor already initialises the point at (0, 0)
        this.points = 0;
        this.handBack = new ArrayList<Card>();
        this.hand = new ArrayList<Card>(); //empty at the beginning
        this.numOfResourceAndObject = new HashMap<>();
        this.numOfResourceAndObject.put("PLANT",0);
        this.numOfResourceAndObject.put("ANIMAL",0);
        this.numOfResourceAndObject.put("INSECT",0);
        this.numOfResourceAndObject.put("FUNGI",0);
        this.numOfResourceAndObject.put("INKWELL",0);
        this.numOfResourceAndObject.put("QUILL",0);
        this.numOfResourceAndObject.put("MANUSCRIPT",0);
        this.timeline = new ArrayList<>();

        this.objective = null;
        this.pion = pion;
        map = new HashMap<>();
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param id The player's ID.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Retrieves the player's chosen nickname.
     *
     * @return The player's nickname.
     */
    public String getNickName() {
        return nickName;
    }
    /**
     * Sets the player's chosen nickname.
     *
     * @param nickName The new nickname for the player.
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    /**
     * Retrieves the player's current score.
     *
     * @return The player's current points.
     */
    public int getPoints() {
        return points;
    }
    /**
     * Sets the player's current score.
     *
     * @param points The new score for the player.
     */
    public void setPoints(int points) {
        this.points = points;
    }
    /**
     * Adds points to the player's current score.
     *
     * @param addP The number of points to add (can be negative).
     */
    public void addPoints(Integer addP){
        this.points = this.points + addP;
    }
    /**
     * Retrieves a copy of the player's resource and objective counts.
     *
     * @return A copy of the `HashMap` containing the player's current resource and objective counts.
     *
     */
    public HashMap<String, Integer> getNumOfResourceAndObject() {
        return numOfResourceAndObject;
    }
    /**
     * Sets the player's resource and objective counts (likely for internal use).
     *
     * @param numOfResourceAndObject A `HashMap` containing the new resource and objective counts for the player.
     *
     */
    public void setNumOfResourceAndObject(HashMap<String, Integer> numOfResourceAndObject) {
        this.numOfResourceAndObject = numOfResourceAndObject;
    }

    /**
     * Increments the player's count for a specific resource or objective type.
     *
     * @param name The name of the resource or objective type (e.g., "PLANT", "INSECT", "MANUSCRIPT").
     * @param num The number to add to the player's count.
     */
    public void addResourceOrObject(String name, Integer num){
        this.numOfResourceAndObject.put(name, this.getNumOfResourceAndObject().get(name) + num );
    }


    /**
     * @param c, the card to be added to the hand
     */
    public void addCard(Card c){
        hand.add(c);
    }
    /**
     * @param c, the card to be added to the handBack
     */
    public void addCardBack(Card c){
        handBack.add(c);
    }
    /**
     * Retrieves a copy of the player's cards in hand.
     *
     * @return An unmodifiable `ArrayList` containing the player's cards in hand,
     *         or an empty list if there are no cards in hand.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }
    /**
     * Sets the player's hand with the provided cards.
     *
     * @param newHand An `ArrayList` containing the new cards for the player's hand.
     */

    public void setHand(ArrayList<Card> newHand) {
        this.hand = newHand;
    }

    public void setMap(HashMap<Card, Integer[]> map) {
        this.map = map;
    }

    /**
     * Retrieves the player's secret objective.
     *
     * @return The player's objective, or null if no objective has been assigned yet.
     */


    public Objective getObjective() {
        return objective;
    }
    /**
     * Retrieves a copy of the HashMap linking cards to their coordinates.
     *
     * @return A copy of the `HashMap` containing card coordinates,
     *
     */
    public HashMap<Card, Integer[]> getMap() {
        return map;
    }
    /**
     * Returns a string representation of the player, including nickname, points, and resource/objective counts.
     *
     * @return A string representation of the player.
     * @Override This method overrides the default `toString()` method from the parent class.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Player: " +
                "nickName='" + nickName + '\'' +
                ", points=" + points + ", risorse: ");

        if(numOfResourceAndObject.isEmpty())
            return s.append("0").toString();
        for (Map.Entry<String, Integer> resObj:numOfResourceAndObject.entrySet()) {
            s.append("Element: ").append(resObj.getKey()).append(", quantity: ").append(resObj.getValue());
        }
        return s.toString();
    }


    /**
     * Card c has already been checked. Place it in the manuscript of the player
     * @param c card to place
     * @param x coordinates
     * @param y coordinates
     */
    public void addCardToMap(Card c, int x, int y){
        map.put(c,new Integer[]{x,y});
    }

    /**
     *
     * @param objective, chosen objective from a list of 2 objectives
     */
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    /**
        @param c, the card to be added to the table
     */
/*    public void addCardToTable(Card c){
        addToTable(c);
        System.out.println("STAMPO CARTE HAND FRONT E BACK");
        hand.forEach(System.out::println);
        if(c.equals(hand.get(0)))
            System.out.println("le due carte sono uguali");

        int index = -1;
        for (int i=0;i<hand.size();i++){
            if(hand.get(i).equals(c))
                index = i;
        }
        if(index == -1){
            for (int i=0;i<handBack.size();i++){
                if(handBack.get(i).equals(c))
                    index = i;
            }
        }

        handBack.forEach(System.out::println);
        System.out.println("\nSto rimuovendo "+c.toString()+", che sta in posizione "+index);
        removeBackCard(index);
        removeCard(index);
    }*/
    public int findCardInHand(Card c){

        int index = hand.indexOf(c);
        if(index == -1){
            index = handBack.indexOf(c);
        }
        return index;
    }

    /**
     *
     * @param index, the card that has just been chosen and has to be deleted from hand
     */
    private void removeBackCard(int index){
        handBack.remove(index);
    }

    /*private void removeBackCard(Card card){
    /**
     *
     * @param card, the card that has just been chosen and has to be deleted from hand
     */
    private void removeBackCard(Card card){
        Optional<Card> cardToRemove = handBack.stream()
                .filter(c -> c.getId() == card.getId())
                .findFirst();

        cardToRemove.ifPresent(handBack::remove);
    }


    /**
     *
     * @param index, the card that has just been chosen and has to be deleted from hand
     */
    private void removeCard(int index){
       hand.remove(index);
    }
    /**
     * Checks if the current player object is equal to another player object.
     *
     * @param p2 The player object to compare with.
     * @return True if the objects represent the same player, false otherwise.
     */
    public boolean equals(Player p2) {
        if (this == p2) {
            return true;
        }
        if (p2 == null || getClass() != p2.getClass()) {
            return false;
        }

        return this.nickName.equals(p2.nickName);
    }
    /**
     * Retrieves the player's game piece (pion).
     *
     * @return The player's `Pion` object.
     */
    public Pion getPion() {
        return pion;
    }

    public void setPion(Pion pion) {
        this.pion = pion;
    }

    /**
     * Sets the player's available positions to the plays card.
     *
     * @param newAvPos An `ArrayList` containing the new set of available positions (Point2D objects) for the player's pion.
     */
    public void resetAvPos(ArrayList<Point2D> newAvPos){
        this.availablePositions = newAvPos;
    }
    /**
     * Removes a specific coordinates from the player's list of available positions.
     *
     * @param toRemove A `Point2D` object representing the position to remove from the available list.
     */
    public void removeAvPos(Point2D toRemove){
        this. availablePositions.remove(toRemove);
    }
    /**
     * Adds a new available position to the player's coordinates list.
     *
     * @param toAdd A `Point2D` object representing the new available position for the player's pion.
     */
    public void addAvPos(Point2D toAdd){
        this.availablePositions.add(toAdd);
    }
    /**
     * Retrieves a copy of the player's list of available coordinates for place card.
     *
     * @return An unmodifiable `ArrayList` containing the player's available positions (Point2D objects).
     */
    public ArrayList<Point2D> getAvailablePositions(){return this.availablePositions;}
    /**
     * Retrieves the hand of the player's back cards.
     *
     * @return The hand of the player's back cards
     */
    public ArrayList<Card> getHandBack() {
        return handBack;
    }
    /**
     * Sets the hand of the player's back cards.
     *
     * @param handBack The hand of the player's back cards.
     */
    public void setHandBack(ArrayList<Card> handBack) {
        this.handBack = handBack;
    }

    public void remove(Point2D oldPoints) {
        availablePositions.remove(oldPoints);
    }
}
