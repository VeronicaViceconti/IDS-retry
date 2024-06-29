package it.polimi.sw.model;

import java.util.ArrayList;

/**
 * the class GoldCard specifies a particular type of card
 */

public class GoldCard extends Card {
    /**
     * resource on the back of a card. cannot be removed after being added
     */
   private Resources permanentResources;
    /**
     * type of item, that is considered for multiplication. may be an object, resource or corners
     */
   private String pointsMultiply;
    /**
     * necessary resources for placing the card on a table. may contain 3-5 resources.
     */
    private ArrayList<Resources> necessaryResource;

    /**
     * Constructs a new `GoldCard` object with the specified attributes.
     *
     * @param corner An `ArrayList` of `Corner` objects associated with this card.
     * @param points An integer value representing the points this card grants.
     * @param id A unique identifier for this card.
     * @param colour The color associated with this card.
     * @param used A boolean flag indicating whether this card has been used.
     * @param drawn A boolean flag indicating whether this card has been drawn from the deck.
     * @param calculatedObjective A boolean flag indicating if this card has been calculated for the final points.
     * @param side An integer representing the side of the card.
     * @param permanentResources A `Resources` enum value representing the permanent resource this card provides.
     * @param pointsMultiply A String value representing the points multiplier associated with this card.
     * @param necessaryResource An `ArrayList` of `Resources` enum values representing the resources required to activate this card.
     *
     * @throws IllegalArgumentException if `corner`, `points`, `id`, or `colour` are null.
     */
    public GoldCard(ArrayList<Corner> corner, int points, int id, String colour, boolean used, boolean drawn, boolean calculatedObjective, int side, Resources permanentResources, String pointsMultiply, ArrayList<Resources> necessaryResource) {
        super(corner, (points), id, (colour), used, drawn, calculatedObjective,side);
        this.permanentResources = permanentResources;
        this.pointsMultiply = pointsMultiply;
        this.necessaryResource = necessaryResource;
    }
    /**
     * Constructs a new `GoldCard` object with default values for its attributes.
     */

    public GoldCard() {
    }

    /**
     * To copy a card
     * @param c the card to be copied
     */
    public GoldCard(GoldCard c) {
        super(c.getCorner(), c.getPoints(), c.getId(), c.getcolour(), c.getUsed(),c.getDrawn(), c.isCalculatedObjective(),c.getSide());
        this.permanentResources = c.permanentResources;
        this.pointsMultiply = c.pointsMultiply;
        this.necessaryResource = c.necessaryResource;

    }

    /**
     * Retrieves the permanent resource provided by this `GoldCard`.
     *
     * @return An `ArrayList` containing a single element, the `Resources` enum value representing the permanent resource
     *         provided by this card.
     * @Override This method overrides the default `getPermanentResource()` method from the parent class.
     */
    @Override
    public ArrayList<Resources> getPermanentResource() {
        if(permanentResources != null){
            ArrayList<Resources> permanentRes = new ArrayList<Resources>();
            permanentRes.add(permanentResources);
            return permanentRes;
        }
        return null;
    }

    /**
     * Sets the permanent resource provided by this `GoldCard`.
     *
     * @param permanentResources A `Resources` enum value representing the permanent resource provided by this card.
     */
    public void setPermanentResources(Resources permanentResources) {
        this.permanentResources = permanentResources;
    }
    /**
     * Retrieves the points multiplier associated with this `GoldCard`.
     *
     * @return The string value representing the points multiplier for this card, or null if no multiplier is defined.
     * @Override This method overrides the default `getPointsMultiply()` method from the parent class (if applicable).
     */
    @Override
    public String getPointsMultiply() {
        return pointsMultiply;
    }
    /**
     * Sets the points multiplier associated with this `GoldCard`.
     *
     * @param pointsMultiply A string value representing the points multiplier for this card.
     */
    public void setPointsMultiply(String pointsMultiply) {
        this.pointsMultiply = pointsMultiply;
    }
    /**
     * Retrieves the resources required to activate this `GoldCard`.
     *
     * @return An `ArrayList` of `Resources` enum values representing the resources required to activate this card,
     *         or null if no resources are required.
     * @Override This method overrides the default `getNecessaryResource()` method from the parent class.
     */
    @Override
    public ArrayList<Resources> getNecessaryResource() {
        return necessaryResource;
    }
    /**
     * Sets the resources required to activate this `GoldCard`.
     *
     * @param necessaryResource An `ArrayList` of `Resources` enum values representing the resources required to activate this card,
     *         or null if no resources are required.
     */
    public void setNecessaryResource(ArrayList<Resources> necessaryResource) {
        this.necessaryResource = necessaryResource;
    }
    /**
     * Returns a string representation of this `GoldCard` object.
     *
     * @return A string representation of this card, including its attributes inherited from the parent class and
     *         specific to `GoldCard`.
     * @Override This method overrides the default `toString()` method from the parent class.
     */

    @Override
    public String toString() {
        String s = "GoldCard => " +super.toString()+
                " permanentResources: " + permanentResources +
                ", pointsMultiply: '" + pointsMultiply + '\'' +
                ", necessaryResource: ";

        if(necessaryResource != null) {
            for (int i=0;i<necessaryResource.size();i++) {
                s += necessaryResource.get(i).name();
                if(i < necessaryResource.size()-1)
                    s += ", ";
            }
        }else s+="none";
        return s;
    }

}
