package it.polimi.sw.model;

import java.util.ArrayList;
import java.util.Optional;

/**
 * the class ResourceCard specifies a particular type of card
 */

public class ResourceCard extends Card {
    /**
     *  resource on the back of a card. cannot be removed after being added.
     */
    private Resources permanentResource;
    /**
     * Constructs a new `ResourceCard` object with default values for its attributes.
     */
    public ResourceCard(){}

    /**
     * To create a copy
     * @param c the card to be copied
     */
    public ResourceCard(ResourceCard c) {
        super(c.getCorner(), c.getPoints(), c.getId(), c.getcolour(), c.getUsed(),c.getDrawn(), c.isCalculatedObjective(),c.getSide());
        this.permanentResource = c.permanentResource;
    }

    /**
     * Retrieves the permanent resource provided by this `ResourceCard`.
     *
     * @return An `ArrayList` containing a single element, the `Resources` enum value representing the permanent resource
     *         provided by this card, or null if no permanent resource is provided.
     * @Override This method overrides the default `getPermanentResource()` method from the parent class (if applicable).
     */
    @Override
    public ArrayList<Resources> getPermanentResource() {
        if(permanentResource != null){
            ArrayList<Resources> permanentRes = new ArrayList<Resources>();
            permanentRes.add(permanentResource);
            return permanentRes;
        }
        return null;
    }

    /**
     * Sets the permanent resource provided by this `ResourceCard`.
     *
     * @param permanentResource A `Resources` enum value representing the permanent resource provided by this card, or null if no permanent resource is provided.
     */
    public void setPermanentResource(Resources permanentResource) {
        this.permanentResource = permanentResource;
    }


    /**
     * Constructs a new `ResourceCard` object with the specified attributes.
     *
     * @param corner An `ArrayList` of `Corner` objects associated with this card.
     * @param points An integer value representing the points this card grants.
     * @param id A unique identifier for this card.
     * @param colour The color associated with this card (if any).
     * @param used A boolean flag indicating whether this card has been used.
     * @param drawn A boolean flag indicating whether this card has been drawn from the deck.
     * @param calculatedObjective A boolean flag indicating whether the objectives for this card have been calculated.
     * @param side An integer representing the side of the card (if applicable in your game logic).
     * @param permanentResource A `Resources` enum value representing the permanent resource provided by this card, or null if no permanent resource is provided.
     *
     * @throws IllegalArgumentException if `corner`, `points`, `id`, or `colour` are null.
     */
    public ResourceCard(ArrayList<Corner> corner, int points, int id, String colour, boolean used, boolean drawn, boolean calculatedObjective, int side, Resources permanentResource) {
        super(corner, (points), id, (colour), used, drawn, calculatedObjective,side);
        this.permanentResource = permanentResource;
    }
    /**
     * Returns a string representation of this `ResourceCard` object.
     *
     * @return A string representation of this card, including its attributes inherited from the parent class and
     *         specific to `ResourceCard`.
     * @Override This method overrides the default `toString()` method from the parent class.
     */
    @Override
    public String toString() {
        return "Resource card =>" +
                "permanentResource: " + permanentResource + super.toString();
    }
}
