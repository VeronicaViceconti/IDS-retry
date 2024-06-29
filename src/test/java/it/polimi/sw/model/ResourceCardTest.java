package it.polimi.sw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

public class ResourceCardTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {

        ArrayList<Corner> corners = new ArrayList<>();
        corners.add(new Corner(Object.INKWELL, null,1, true, null));
        int points = 5;
        int id = 1;
        String colour = "red";
        boolean used = false;
        boolean drawn = false;
        boolean calculatedObjective = false;
        int side = 1;
        Resources permanentResource = Resources.FUNGI;

        ResourceCard resourceCard = new ResourceCard(corners, points, id, colour, used, drawn, calculatedObjective, side, permanentResource);

        // Testing getter and setter methods
        assert resourceCard.getPermanentResource().contains(Resources.FUNGI) : "Permanent Resource initialization error";

        // Testing inheritance from superclass (Card)
        assert resourceCard.getPoints() == 5 : "Points initialization error";
        assert resourceCard.getId() == 1 : "ID initialization error";

        // Testing if permanent resource is correctly set
        resourceCard.setPermanentResource(Resources.PLANT);
        assert resourceCard.getPermanentResource().contains(Resources.PLANT) : "Permanent Resource update error";

        //Testing tostring method
        resourceCard.toString().compareTo("Resource card =>" +
                "permanentResource: " + permanentResource + super.toString());

        // If no AssertionError is thrown, all tests pass
        System.out.println("All tests passed successfully.");
    }
}
