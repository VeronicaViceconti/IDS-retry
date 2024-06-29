package it.polimi.sw.model;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * the class InitialCard specifies the initial card of the game that is distributed at the beginning of a game
 */
public class InitialCard extends Card {
    /**
     * resource on the back of a card. cannot be removed after being added. may be 2-3 resources
     */
    private ArrayList<Resources> permanentResource;

    /**
     * Constructs a new `InitialCard` object with the specified attributes.
     *
     * @param corner An `ArrayList` of `Corner` objects associated with this card.
     * @param points An integer value representing the points.
     * @param id A unique identifier for this card.
     * @param colour The color associated with this card (if any).
     * @param used A boolean flag indicating whether this card has been used.
     * @param drawn A boolean flag indicating whether this card has been drawn from the deck.
     * @param calculatedObjective A boolean flag indicating whether the objectives for this card have been calculated.
     * @param side An integer representing the side of the card (if applicable in your game logic).
     * @param permanentResource An `ArrayList` of `Resources` enum values representing the permanent resources provided by this card.
     *
     * @throws IllegalArgumentException if `corner`, `points`, `id`, or `colour` are null.
     */
    public InitialCard(ArrayList<Corner> corner, int points, int id, String colour, boolean used, boolean drawn, boolean calculatedObjective, int side, ArrayList<Resources> permanentResource) {
        super(corner, (points), id, (colour), used, drawn, calculatedObjective, side);
        this.permanentResource = permanentResource;
    }

    /**
     * Constructs a new `InitialCard` object with default values for its attributes.
     */
    public InitialCard() {

    }
    /**
     * Sets the permanent resources provided by this `InitialCard`.
     *
     * @param permanentResource An `ArrayList` of `Resources` enum values representing the permanent resources provided by this card,
     *         or null if no permanent resources are provided.
     */
    public void setPermanentResource(ArrayList<Resources> permanentResource) {
        this.permanentResource = permanentResource;
    }
    /**
     * Returns a string representation of this `InitialCard` object.
     *
     * @return A string representation of this card, including its attributes inherited from the parent class and
     *         specific to `InitialCard`.
     * @Override This method overrides the default `toString()` method from the parent class.
     */
    @Override
    public String toString() {
        return "InitialCard => " +
                "permanentResource: " + permanentResource +
                ',' + super.toString();
    }

    /**
     * Retrieves the permanent resources provided by this `InitialCard`.
     *
     * @return An `ArrayList` of `Resources` enum values representing the permanent resources provided by this card,
     *         or null if no permanent resources are provided.
     * @Override This method overrides the default `getPermanentResource()` method from the parent class (if applicable).
     */

    @Override
    public ArrayList<Resources> getPermanentResource() {
        return permanentResource;
    }
}
