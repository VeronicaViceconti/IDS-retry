package it.polimi.sw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class InitialCardTest {
        @Test
        public void shouldAnswerWithTrue()
        {
            /*assertTrue( true );*/
            ArrayList<Corner> corners = new ArrayList<>();
            corners.add(new Corner(null, null,1, true, false));
            corners.add(new Corner(null, null,2, true, false));
            corners.add(new Corner(null, null,3, true, false));
            corners.add(new Corner(null, null,4, true, false));

            int id = 1;
            String typeCard = "Initial Card";
            boolean used = false;
            boolean drawed = false;
            int side = 1;
            Resources resource1 = Resources.FUNGI;
            Resources resource2 = Resources.ANIMAL;
            Resources resource3 = Resources.INSECT;

            ArrayList<Resources> permanentResource=new ArrayList<>(Arrays.asList(resource1,resource2,resource3));

            InitialCard initialCard=new InitialCard(corners,0,id,null,used,drawed,false,side,permanentResource);


            // Testing getter and setter methods
           assert initialCard.getUsed()==false : "used not set properly";
           assert initialCard.getDrawn()==false : "drawn not set properly";
           assert initialCard.getcolour()==null : "color not set properly";
           assert initialCard.getPermanentResource().getFirst().equals(permanentResource.getFirst()) : "resource not set properly";
           assert initialCard.getPermanentResource().get(1).equals(permanentResource.get(1)) : "resource not set properly";
           assert initialCard.getPermanentResource().get(2).equals(permanentResource.get(2)) : "resource not set properly";
           assert initialCard.getSide() == 1 : "side initialization error";
           assert initialCard.getId() == 1 : "ID initialization error";
           initialCard.setId(43);
           assert initialCard.getId()==43 : "id not set properly";
           initialCard.setSide(2);
            assert initialCard.getSide() == 2 : "side not set properly";

            // Testing if permanent resource is correctly set
            Resources resource1_1 = Resources.FUNGI;
            Resources resource1_2 = Resources.PLANT;
            Resources resource1_3 = Resources.INSECT;
            ArrayList<Resources> permanentResource2=new ArrayList<>(Arrays.asList(resource1_1,resource1_2,resource1_3));
            initialCard.setPermanentResource(permanentResource2);
            assert initialCard.getPermanentResource().get(1) == Resources.PLANT : "Permanent Resource update error";

            initialCard.toString();

            // If no AssertionError is thrown, all tests pass
            System.out.println("All tests passed successfully.");
        }


    }
