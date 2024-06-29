package it.polimi.sw.model;
/**
 * the class of ObjectiveTypes is used to specify the pattern that can be found in different {@link Objective} cards
 *
 * therefore we came up with four group of Objective:
 * {@link Resources} filling/collecting
 * {@link Object} filling/collecting
 * {@link Card} same "colour" positioning diagonally
 * {@link Card} mix "colour" positioning L shape
 */
public enum ObjectiveTypes {
    ResourceFilling,
    ObjectFilling,
    ThreeSameColourPlacing,
    TwoSameColourOneDiffersPlacing
}
