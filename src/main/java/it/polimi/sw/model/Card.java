package it.polimi.sw.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * the class of card in which there are attributes common to gold and resource card
 */






public abstract class Card implements Serializable {
    /**
     * four corners of the card
     */
    private ArrayList<Corner> corner;
    /**
     * number of points obtained when placing the card
     */
    private int points;
    /**
     * unique code of card
     */
    private int id;
    /**
     * the type of card: gold or resource
     */
    private String colour;
    /**
     * used = card was placed on a table
     */
    private boolean used;
    /**
     * drawn = card was drawn by a player
     */
    private boolean drawn;
    /**
     * a card was considered in final calculation of points
     */
    private boolean calculatedObjective;
    /**
     * back or front of a card
     */
    private int side;


    /**
     * The `permanentResource` list
     */
    private ArrayList<Resources> permanentResource;


    /**
     * Constructs a new Card object.
     *
     * @param corner an ArrayList containing Corner objects representing the card's corners
     * @param points the point value associated with completing the card
     * @param id a unique identifier for the card
     * @param colour the color of the card (e.g., "red", "blue")
     * @param used a boolean flag indicating if the card has been played this turn
     * @param drawn a boolean flag indicating if the card has been drawn from the deck
     * @param calculatedObjective a boolean flag indicating if the card has been used to fulfill an objective
     * @param side the orientation of the card
     */
    public Card(ArrayList<Corner> corner, int points, int id, String  colour, boolean used, boolean drawn, boolean calculatedObjective, int side) {
        this.corner = corner;
        this.points = points;
        this.id = id;
        this.colour = colour;
        this.used = used;
        this.drawn = drawn;
        this.calculatedObjective = calculatedObjective;
        this.side = side;
    }


    /**
     * Used to copy a card
     * @param c
     */
    public Card(Card c) {
        this.corner = c.corner;
        this.points = c.points;
        this.id = c.id;
        this.colour = c.colour;
        this.used = c.used;
        this.drawn = c.drawn;
        this.calculatedObjective = c.calculatedObjective;
        this.side = c.side;
        this.permanentResource = c.permanentResource;
    }


    /**
     * Default constructor for the `Card` class.
     */
    public Card() {
    }
    /**
     * Retrieves the current side of the card.
     *
     * This method returns the value of the `side` attribute.
     *
     * @return the integer value representing the card's side
     */
    public int getSide() {
        return side;
    }


    /**
     * Sets the side of the card.
     *
     * This method updates the `side` attribute of the Card object with the provided value.
     *
     * @param side the integer value representing the new side for the card
     */
    public void setSide(int side) {
        this.side = side;
    }
    /**
     * Checks if the card has been used to fulfill an objective.
     *
     * This method returns the value of the `calculatedObjective` attribute. A `true` value indicates that the card has contributed to completing at least one objective.
     *
     * @return true if the card has been used to fulfill an objective, false otherwise
     */


    public boolean isCalculatedObjective() {
        return calculatedObjective;
    }


    /**
     * Sets a flag indicating if the card has been used to fulfill an objective.
     *
     * @param calculatedObjective a boolean value indicating if the card is used for an objective (true) or not (false)
     */
    public void setCalculatedObjective(boolean calculatedObjective) {
        this.calculatedObjective = calculatedObjective;
    }
    /**
     * Retrieves the list of corners associated with the card.
     *
     * This method returns the `corner` attribute, which is likely an `ArrayList` containing `Corner` objects.
     *
     * @return an ArrayList containing Corner objects representing the card's corners
     */
    public ArrayList<Corner> getCorner() {
        return corner;
    }
    /**
     * Sets the list of corners associated with the card.
     *
     * @param corner an ArrayList containing Corner objects representing the card's corners
     */


    public void setCorner(ArrayList<Corner> corner) {
        this.corner = corner;
    }
    /**
     * Retrieves the point value associated with the card.
     *
     * This method returns the value of the `points` attribute, which represents the number of points of the card.
     * @return the integer value representing the card's point value
     */
    public Integer getPoints() {
        return points;
    }
    /**
     * Sets the point value associated with the card.
     *
     * This method updates the `points` attribute with the provided integer value.
     *
     * @param points the integer value representing the new point value for the card
     */
    public void setPoints(int points) {
        this.points = (points);
    }
    /**
     * Retrieves the unique identifier for the card.
     *
     * This method returns the value of the `id` attribute, which is likely a unique integer assigned to each card.
     *
     * @return the integer value representing the card's unique identifier
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the unique identifier for the card.
     *
     * This method updates the `id` attribute with the provided integer value.
     *
     * @param id the integer value representing the new unique identifier for the card
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Retrieves the color of the card.
     *
     * This method returns the value of the `colour` attribute, which represents the card's color (e.g., "red", "blue").
     *
     * @return the String value representing the card's color
     */
    public String getcolour() {
        return colour;
    }
    /**
     * Sets the color of the card.
     *
     * This method updates the `colour` attribute with the provided String value.
     *
     * @param colour the String value representing the new color for the card
     */
    public void setcolour(String colour) {
        this.colour = (colour);
    }
    /**
     * Checks if the card has been played during the current turn.
     *
     * This method returns the value of the `used` attribute. A `true` value indicates that the card has been played by a player this turn.
     *
     * @return true if the card has been played, false otherwise
     */
    public boolean getUsed() {
        return used;
    }
    /**
     * Sets a flag indicating if the card has been played during the current turn.
     *
     * This method updates the `used` attribute with the provided boolean value.
     * Setting it to `true` signifies that the card has been played.
     *
     * @param used a boolean value indicating if the card is used (true) or not (false)
     */
    public void setUsed(boolean used) {
        this.used = used;
    }
    /**
     * Checks if the card has been drawn from the deck.
     *
     * This method returns the value of the `drawn` attribute. A `true` value indicates that the card has been drawn from the deck during the game.
     *
     * @return true if the card has been drawn from the deck, false otherwise
     */
    public boolean getDrawn() {
        return drawn;
    }
    /**
     * Sets a flag indicating if the card has been drawn from the deck.
     *
     * This method updates the `drawn` attribute with the provided boolean value.
     * Setting it to `true` signifies that the card has been drawn from the deck during the game.
     *
     * @param drawn a boolean value indicating if the card is drawn from the deck (true) or not (false)
     */
    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }
    /**
     * Overrides the default `toString()` method to provide a string representation of the `Card`.
     *
     * It returns a string that includes the following information about the card:
     *  - Corners: String representation of the `corner` list.
     *  - Points: Point value associated with the card.
     *  - ID: Unique identifier of the card.
     *  - Color: Color of the card.
     *  - Used: Flag indicating if the card has been played.
     *  - Drawn: Flag indicating if the card has been drawn from the deck.
     *  - Calculated Objective: Flag indicating if the card has been used to fulfill an objective.
     *  - Side: Side of the card.
     *
     * @return a String representation of the Card object
     * @Override indicates this method overrides the default implementation from the parent class
     */
    @Override
    public String toString() {
        return " corners: " + corner.toString() +
                ", points:" + points+
                ", id: " + id +
                ", colour:'" + colour + '\'' +
                ", used:" + used +
                ", drawn:" + drawn +
                ", calculatedObjective:" + calculatedObjective +
                ", side:" + side+"\n";
    }


    /**
     * A static method that iterates through a list of `Card` objects and prints their string representations (using the `toString()` method) to the console.
     *
     * @param listofcards a List containing Card objects
     */
    public static void printAllCards(List<Card> listofcards){
        listofcards.forEach(item -> System.out.println(item.toString()));
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(!(o instanceof Card)) return false;
        return this.getId() == ((Card) o).getId() && this.getClass() == o.getClass();
    }


    @Override
    public int hashCode() {
        int result = getCorner() != null ? getCorner().hashCode() : 0;
        result = 31 * result + getPoints();
        result = 31 * result + getId();
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + (getUsed() ? 1 : 0);
        result = 31 * result + (getDrawn() ? 1 : 0);
        result = 31 * result + (isCalculatedObjective() ? 1 : 0);
        result = 31 * result + getSide();
        result = 31 * result + (getPermanentResource() != null ? getPermanentResource().hashCode() : 0);
        return result;
    }


    /**
     * Placeholder method for `getNecessaryResource()`.
     *
     * The current implementation likely returns `null`.
     *
     * @return currently returns null
     */


    public ArrayList<Resources> getNecessaryResource(){
        return null;
    }
    /**
     * Placeholder method for `getPointsMultiply()`.
     *
     * The current implementation likely returns `null`.
     *
     * @return currently returns null
     */
    public String getPointsMultiply() {
        return null;
    }


    /**
     * Abstract method declaration for `getPermanentResource()`.
     *
     * This method signature suggests it might return an `ArrayList` of `Resources` objects, but it's declared as abstract.
     *
     * @return (abstract) intended to be overridden by subclasses
     */
    abstract public ArrayList<Resources> getPermanentResource();
}