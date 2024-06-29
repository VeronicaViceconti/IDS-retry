package it.polimi.sw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * the class Objective represents the card objective
 */

public class Objective implements Serializable {
    /**
     * number of given points for reaching a goal. may be 2-3
     */
    private int points;
    /**
     * the attribute condition specifies the condition to obtain the points of the objective
     */
    private Conditions condition;

    /**
     * chosen objective is active in a game. may be common or secret
     */
    private boolean chosen;
    /**
     * Constructs a new `Objective` object with the specified attributes.
     *
     * @param points An integer value representing the points awarded for completing this objective.
     * @param condition A `Conditions` object representing the conditions that need to be met to complete this objective.
     * @param chosen A boolean flag indicating whether this objective has been chosen by the player.
     */
    public Objective(int points, Conditions condition, boolean chosen) {
        this.points = points;
        this.condition = condition;
        this.chosen = chosen;
    }

    /**
     * Constructs a new `Objective` object with default values for its attributes.
     */

    public Objective() {
    }

    /**
     * Retrieves the number of points awarded for completing this objective.
     *
     * @return An integer value representing the points awarded for this objective.
     */
    public int getPoints() {
        return points;
    }
    /**
     * Sets the number of points awarded for completing this objective.
     *
     * @param points An integer value representing the new points awarded for this objective.
     */
    public void setPoints(int points) {
        this.points = points;
    }
    /**
     * Retrieves the conditions that need to be met to complete this objective.
     *
     * @return A `Conditions` object representing the conditions for this objective.
     */
    public Conditions getCondition() {
        return condition;
    }
    /**
     * Sets the conditions that need to be met to complete this objective.
     *
     * @param condition A `Conditions` object representing the new conditions for this objective.
     */
    public void setCondition(Conditions condition) {
        this.condition = condition;
    }
    /**
     * Checks whether this objective has been chosen by the player.
     *
     * @return True if the objective has been chosen by the player, false otherwise.
     */
    public boolean isChosen() {
        return chosen;
    }
    /**
     * Sets a flag indicating whether this objective has been chosen by the player.
     *
     * @param chosen A boolean flag indicating whether this objective has been chosen (true) or not (false).
     */
    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
    /**
     * Returns a string representation of this `Objective` object.
     *
     * @return A string representation of this objective, including its points, conditions (as a string from the Conditions object), and chosen flag.
     * @Override This method overrides the default `toString()` method from the parent class.
     */
    @Override
    public String toString() {
        return "Objective{" +
                "points=" + points +
                ", condition=" + condition.toString() +
                ", chosen=" + chosen +
                '}' +"\n";
    }
}
