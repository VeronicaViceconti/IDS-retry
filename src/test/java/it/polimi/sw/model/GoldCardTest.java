package it.polimi.sw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GoldCardTest {
    @Test
    public void shouldAnswerWithTrue() {


        /*assertTrue( true );*/
        ArrayList<Corner> corners = new ArrayList<>();
        corners.add(new Corner(Object.INKWELL, null,1, true, false));
        corners.add(new Corner(null, null,2, true, false));
        corners.add(new Corner(null, Resources.ANIMAL,3, true, false));
        corners.add(new Corner(Object.QUILL, null,4, true, false));
        int points = 5;
        int id = 1;
        String typeCard = "Type";
        boolean used = false;
        boolean drawed = false;
        boolean calculatedObjective = false;
        int side = 1;
        Resources permanentResource = Resources.FUNGI;
        Resources resource1=Resources.FUNGI;
        Resources resource2=Resources.INSECT;
        Resources resource3=Resources.PLANT;
        ArrayList<Resources> necessaryResource2=new ArrayList<>(Arrays.asList(resource1,resource2,resource3));
        String pointsMultiply = "1, funghi, qikwell";
        ArrayList<Resources> list = new ArrayList<>();
        list.add(Resources.FUNGI);
        GoldCard goldCard = new GoldCard(corners, points, id, typeCard, used, drawed, calculatedObjective, side, permanentResource, pointsMultiply,list);
        goldCard.setPermanentResources(Resources.INSECT);
        goldCard.setPointsMultiply(pointsMultiply);


        // Testing getter and setter methods
        assert goldCard.getPermanentResource().contains(Resources.INSECT) : "Permanent resources not set properly";
        assert goldCard.getNecessaryResource().contains(Resources.FUNGI) : "Necessary Resource initialization error";
        assert goldCard.getPointsMultiply().equals(pointsMultiply) : "PointsMultiply error";
        assert goldCard.getUsed()==false :"getUsed error";
        assert goldCard.getDrawn()==false : "getDrawed error";
        goldCard.setSide(2);
        assert goldCard.getSide()==2 : "getSide error ";
        goldCard.setcolour("red");
        assert goldCard.getcolour().equals("red") : "colour not set properly";
        goldCard.setNecessaryResource(necessaryResource2);
        assert goldCard.getNecessaryResource().containsAll(necessaryResource2) : "necessaryResource2 not set properly";
        // Testing inheritance from superclass (Card)
        assert goldCard.getPoints() == 5 : "Points initialization error";
        assert goldCard.getId() == 1 : "ID initialization error";
        goldCard.setCorner(corners);
        assert goldCard.getCorner().getFirst().equals(corners.getFirst()) : "corne not set properly";

        goldCard.setCalculatedObjective(true);

        assert goldCard.isCalculatedObjective()==true : "calc not set property";

        // Testing if Necessary resource is correctly set
        list.add(Resources.PLANT);
        goldCard.setNecessaryResource(list);
        assert goldCard.getNecessaryResource().contains(Resources.PLANT) : "Necessary Resource initialization error";

        //Test toString
         goldCard.toString().compareTo("GoldCard => " +super.toString()+
                 " permanentResources: " + permanentResource +
                 ", pointsMultiply: '" + pointsMultiply + '\'' +
                 ", necessaryResource: ");




        // If no AssertionError is thrown, all tests pass
        System.out.println("All tests passed successfully.");
    }
}
