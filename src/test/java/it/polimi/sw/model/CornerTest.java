package it.polimi.sw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;


public class CornerTest {

    @Test
    public void shouldAnswerWithTrue(){



        int number=3;
        boolean visible=true;
        Resources resource=null;
        Object object=Object.QUILL;
        Optional<Card> covered=null;

        Corner corner=new Corner(object, null,number, true, false);

        // Testing getter and setter methods
        assert corner.getVisible()==visible : "it's not true";
        assert corner.getObject() == Object.QUILL : "the object it's not set properly";
        assert corner.getResource() ==resource : "resource initialization error";
        assert corner.getNumber()==number : "number initialization error";
        //assert corner.getCovered()==covered : "covered initialization error";

        corner.setNumber(2);
        assert corner.getNumber()==2 : "set of number it's not correct";

        corner.setVisible(false);
        assert corner.getVisible()==false : "set of visible it's not correct";
 /*       Card c = new GoldCard();
        corner.setCovered(c);
        assert corner.getCovered().equals(c): "set of covered it's not correct";
*/
        // Testing if resource is correctly set
        corner.setResource(Resources.PLANT);
        assert corner.getResource()==Resources.PLANT : "update of resource it's not correct";

        // Testing if object is correctly set
        corner.setObject(Object.MANUSCRIPT);
        assert corner.getObject()==Object.MANUSCRIPT : "the object it's not set properly";




        // If no AssertionError is thrown, all tests pass
        System.out.println("All tests passed successfully.");

    }
}
