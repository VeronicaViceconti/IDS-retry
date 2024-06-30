package it.polimi.sw.model;

import java.io.Serializable;
import java.util.Optional;

/**
 * the class corner specifies the corner of a card
 */

public class Corner implements Serializable {
    /**
     * an object in a corner, if present
     */
   private Object object;
    /**
     * a resource in a corner, if present
     */
   private Resources resource;
    /**
     * enumeration of corners 1-4, 1 is TopLeft, 2 TopRight, 3 BottomLeft, 4 BottomRight
     */
    private int number;

    /**
     * visible if it is possible to place a card over the corner
     */
    private boolean visible;
    /**
     * corner may be covered by another card. may change during the game. by default a corner is not covered
     */
    private boolean covered;
    /**
     * Constructs a new `Corner` object with the specified attributes.
     *
     * @param object The object associated with this corner.
     * @param resource The resource associated with this corner.
     * @param number An integer value associated with this corner.
     * @param visible A boolean flag indicating whether this corner is visible or not.
     * @param covered A `Card` object representing the card covering this corner (if any). Can be null if not covered.
     */
    public Corner(Object object,Resources resource,int number,boolean visible,boolean covered) {
        this.object = object;
        this.resource = resource;
        this.number = number;
        this.visible = visible;
        this.covered = covered;
    }
    /**
     * Constructs a new `Corner` object with default values for its attributes.
     */

    public Corner() {
    }
    /**
     * Retrieves the resource associated with this corner.
     *
     * @return The resource associated with this corner as a `Resources` enum value.
     */

    public Resources getResource() {  return resource;
    }
    /**
     * Sets the resource associated with this corner.
     *
     * @param resource The new resource to be associated with this corner.
     */
    public void setResource(Resources resource) {
        this.resource = resource;
    }
    /**
     * Retrieves the integer value associated with this corner.
     *
     * @return The integer value associated with this corner. The meaning of this value depends on the specific game logic.
     */
    public int getNumber() {
        return number;
    }
    /**
     * Retrieves a boolean value indicating whether this corner is visible.
     *
     * @return True if the corner is visible, false otherwise.
     */
    public boolean getVisible() {
        return visible;
    }
    /**
     * Sets the integer value associated with this corner.
     *
     * @param number The new integer value to be associated with this corner.
     */
    public void setNumber(int number) {
        this.number = number;
    }
    /**
     * Sets the visibility state of this corner.
     *
     * @param covered A boolean value indicating whether the corner should be visible (true) or hidden (false).
     */

    public void setVisible(boolean covered) {
        this.visible = covered;
    }
    /**
     * Sets the card covering this corner (if any).
     *
     */
    public void setCovered() {
        this.covered = true;
    }
    /**
     * Retrieves the object associated with this corner.
     *
     * @return The object associated with this corner. The type of object can vary depending on the game logic.
     */
    public Object getObject() {
        return object;
    }
    /**
     * Sets the object associated with this corner.
     *
     * @param object The new object to be associated with this corner.
     */
    public void setObject(Object object) {
        this.object = object;
    }
    /**
     * Retrieves the card covering this corner (if any).
     *
     * @return A `Card` object representing the card that covers this corner, or null if not covered.
     */
    public boolean getCovered() {
        return covered;
    }
    /**
     * Returns a string representation of this `Corner` object.
     *
     * @return A string representation of this corner, including its attributes.
     * @Override This method overrides the default `toString()` method from the parent class.
     */
    @Override
    public String toString() {
        return "Corner{" +
                "object=" + object +
                ", resource=" + resource +
                ", number=" + number +
                ", visible=" + visible +
                ", covered=" + covered +
                '}';
    }
}
