package it.polimi.sw.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
/*@JsonInclude(JsonInclude.Include.NON_ABSENT)*/
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Conditions implements Serializable {
    private Integer id;

    /**
     *  The quantity required on the table to obtain the points
     */
    private Integer quantityRequiredToObtainPoints;

    /**
     * Type of objective to be obtained
     */
    private ObjectiveTypes typeOfObjective;
    private List<Resources> resourcesRequiredToObtainPoints;
    /**
     * Objects of the same type are added only once
     * in the case of different types of objects, the quantity remains unchanged, check the quantity for each object in the list
     */
    private List<Object> objectsRequiredToObtainPoints;

    /**
     *  The common colour of cards used for the "placing" objective
     */
    private String colour1;
    /**
     * The different colour of card for the "placing" objective
     */
    private String colour2;
    /**
     * Enumeration of corners 1-4, 1 is TopLeft, 2 TopRight, 3 BottomLeft, 4 BottomRight
     * the "covering" angle is considered to be the "central" one to the physical Card
     * 1: Nord- Ovest to Sud- Est
     * 2: Sud-Ovest to Nord-Est
     */
    private Integer firstOrientationCornerCheck;
    /**
     * The description of the objective
     */
    private String description;

    /**
     * This constructor creates a new Conditions object that represents a game condition or objective.
     * @param id Unique identifier of the 'objective card'.
     * @param quantityRequiredToObtainPoints The quantity required on the table to obtain the points
     * @param typeOfObjective Type of objective
     * @param resourcesRequiredToObtainPoints Resources required to obtain points
     * @param objectsRequiredToObtainPoints  Objects required to obtain points
     * @param colour1 The common colour of cards used for the "placing" objective
     * @param colour2 The different colour of card for the "placing" objective
     * @param firstOrientationCornerCheck Enumeration of corners 1-4, 1 is TopLeft, 2 TopRight, 3 BottomLeft, 4 BottomRight
     *the "covering" angle is considered to be the "central" one to the physical Card
     *1: Nord- Ovest to Sud- Est
     *2: Sud-Ovest to Nord-Est
     * @param description The description of the objective
     */
    public Conditions(Integer id, Integer quantityRequiredToObtainPoints, ObjectiveTypes typeOfObjective, List<Resources> resourcesRequiredToObtainPoints, List<Object> objectsRequiredToObtainPoints, String colour1, String colour2, Integer firstOrientationCornerCheck, String description) {
        this.id = id;
        this.quantityRequiredToObtainPoints = quantityRequiredToObtainPoints;
        this.typeOfObjective = typeOfObjective;
        this.resourcesRequiredToObtainPoints = (resourcesRequiredToObtainPoints);
        this.objectsRequiredToObtainPoints = objectsRequiredToObtainPoints;
        this.colour1 = colour1;
        this.colour2 = colour2;
        this.firstOrientationCornerCheck = firstOrientationCornerCheck;
        this.description = description;
    }
    /**
     * This constructor initializes the class's attributes to their default values.
     */
    public Conditions() {
    }
    /**
     * Retrieves the ID of the 'Objective card'.
     *
     * @return The ID of the 'Objective card' as an `Integer` value.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets the ID of the 'Objective card'.
     *
     * @param id The new ID of the 'Objective card' to be set.
     * @throws IllegalArgumentException If the provided `id` is null or less than 1.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the quantity of 'Objective card' required to obtain points.
     *
     * @return The quantity required to obtain points as an `Integer` value.
     */


    public Integer getQuantityRequiredToObtainPoints() {
        return quantityRequiredToObtainPoints;
    }

    /**
     * Sets the quantity of 'Objective card' required to obtain points.
     *
     * @param quantityRequiredToObtainPoints The new quantity required to obtain points.
     * @throws IllegalArgumentException If the provided `quantityRequiredToObtainPoints` is null or less than 1.
     */


    public void setQuantityRequiredToObtainPoints(Integer quantityRequiredToObtainPoints) {
        this.quantityRequiredToObtainPoints = quantityRequiredToObtainPoints;
    }
    /**
     * Retrieves the type of objective.
     *
     * @return The type of objective as an `ObjectiveTypes` enum value.
     */
    public ObjectiveTypes getTypeOfObjective() {
        return typeOfObjective;
    }
    /**
     * Sets the type of objective.
     *
     * @param typeOfObjective The new type of objective to be set.
     * @throws IllegalArgumentException If the provided `typeOfObjective` is null.
     */
    public void setTypeOfObjective(ObjectiveTypes typeOfObjective) {
        this.typeOfObjective = typeOfObjective;
    }
    /**
     * Retrieves the list of resources required to obtain points.
     *
     * @return List of the required resources of type `Resources`.
     */
    public List<Resources> getResourcesRequiredToObtainPoints() {
        return resourcesRequiredToObtainPoints;
    }
    /**
     * Sets the list of resources required to obtain points.
     *
     * @param resourcesRequiredToObtainPoints The new list of resources required to obtain points.
     * @throws NullPointerException If the provided `resourcesRequiredToObtainPoints` list is null.
     */
    public void setResourcesRequiredToObtainPoints(List<Resources> resourcesRequiredToObtainPoints) {
        this.resourcesRequiredToObtainPoints = resourcesRequiredToObtainPoints;
    }
    /**
     * Retrieves the list of objects required to obtain points.
     *
     * @return List of the required objects
     */

    public List<Object> getObjectsRequiredToObtainPoints() {
        return objectsRequiredToObtainPoints;
    }
    /**
     * Sets the list of objects required to obtain points.
     *
     * @param objectsRequiredToObtainPoints The new list of objects required to obtain points.
     * @throws NullPointerException If the provided `objectsRequiredToObtainPoints` list is null.
     */
    public void setObjectsRequiredToObtainPoints(List<Object> objectsRequiredToObtainPoints) {
        this.objectsRequiredToObtainPoints = objectsRequiredToObtainPoints;
    }

    /**
     * Retrieves the value of the first color attribute.
     *
     * @return The value of the first color attribute as a String.
     */
    public String getColour1() {
        return colour1;
    }
    /**
     * Sets the value of the first color attribute.
     *
     * @param colour1 The new value for the first color attribute.
     */
    public void setColour1(String colour1) {
        this.colour1 = colour1;
    }

    /**
     * Retrieves the value of the second color attribute.
     *
     * @return The value of the second color attribute as a String (or null if not set).
     */
    public String getColour2() {
        return colour2;
    }
    /**
     * Sets the value of the second color attribute.
     *
     * @param colour2 The new value for the second color attribute.
     */
    public void setColour2(String colour2) {
        this.colour2 = colour2;
    }

    /**
     * Retrieves the value of the `firstOrientationCornerCheck` attribute.
     *
     * @return The value of the `firstOrientationCornerCheck` attribute as an Integer.
     *
     */

    public Integer getFirstOrientationCornerCheck() {
        return firstOrientationCornerCheck;
    }


    /**
     * Sets the value of the `firstOrientationCornerCheck` attribute.
     *
     * @param firstOrientationCornerCheck The new value for the `firstOrientationCornerCheck` attribute.
     *
     */
    public void setFirstOrientationCornerCheck(Integer firstOrientationCornerCheck) {
        this.firstOrientationCornerCheck = firstOrientationCornerCheck;
    }
    /**
     * Retrieves the description of the 'Objective card'.
     *
     * @return The description of the 'Objective card' as a String.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description of the 'Condition' object.
     *
     * @param description The new description for this condition.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Returns a string representation of this `Condition` object.
     *
     * @return A string representation of this condition, including its attributes.
     * @Override This method overrides the default `toString()` method from the parent class.
     */
    @Override
    public String toString() {
        return "Condition:  " +
                "id=" + id +
                ", quantityRequiredToObtainPoints=" + quantityRequiredToObtainPoints +
                ", typeOfObjective=" + typeOfObjective +
                ", resourcesRequiredToObtainPoints=" + resourcesRequiredToObtainPoints +
                ", objectsRequiredToObtainPoints=" + objectsRequiredToObtainPoints +
                ", colour1=" + colour1 +
                ", colour2=" + colour2 +
                ", FirstOrientationCornerCheck=" + firstOrientationCornerCheck +
                ", description='" + description + '\'';
    }
}
