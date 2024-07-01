package it.polimi.sw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlayerTest {

    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;
    private final ArrayList<Corner> corners = new ArrayList<>();
    private Card c;
    private Card c2;
    private ArrayList<Card> hand;
    private ArrayList<Card> timeLine = new ArrayList<>();
    @BeforeEach
    void setUp() {
        p1 = new Player("Piero", Pion.pion_blue);
        p2 = new Player("Pluto", Pion.pion_black);
        p3 = p2;
        p4 = null;


        corners.add(new Corner(Object.INKWELL, null, 1, true, false));
        corners.add(new Corner(null, null, 2, false, false));
        corners.add(new Corner(null, Resources.PLANT, 3, true, false));
        corners.add(new Corner(null, null, 4, false, false));
        c = new ResourceCard(corners, 2, 1, "green", false, false, false, 1, Resources.PLANT);
        c2 = new GoldCard(corners, 1, 24, "red", false, false, false, 1, null, "INKWELL", null);
        hand = new ArrayList<>(Arrays.asList(c, c2));

        p3 = p2;
        p4 = null;




    }

    @Test
    public void TesterGetterAndSetter(){
        //Testing get/set methods
        p1.setPoints(3);
        assert p1.getPoints() == 3 : "Bad setpoints implementation";
        p1.setNickName("Pippo");
        p1.setObjective(new Objective(1, new Conditions(), true));
        p1.setNumOfResourceAndObject(new HashMap<>(){ {put("ANIMAL", 3);}} );
        p1.addPoints(3);
        p1.setId(1);
        p1.setHand(hand);
        p1.setHandBack(hand);
        p1.setPion(Pion.pion_red);
        this.timeLine.add(c);
        p1.setTimeline(timeLine);


        assert p1.findCardInHand(c)==0 : "it's incorrect";
        assert p1.getTimeline().getFirst().equals(c) : "no";
        p1.addToTimeline(c2);
        assert p1.getTimeline().get(1).equals(c2) : "no";
        assert p1.getPion().equals(Pion.pion_red) : "pion not set properly";
        assert p1.getHandBack().getFirst().equals(c) : "handBack not set properly";
        assert p1.getHandBack().get(1).equals(c2) : "handBack not set properly";
        assert p1.getHand().getFirst().equals(c) : "hand not set properly";
        assert p1.getHand().get(1).equals(c2) : "hand not set properly";
        assert p1.getId()==1 : "id not set properly";
        assert p1.getPoints() == 6 : "Bad addPoints implementation";
        assert p1.getNickName().equals("Pippo") : "Bad setnickname implementation";
        assert p1.getObjective().getPoints() == 1 : "Bad setObjective implementation";
        assert p1.getNumOfResourceAndObject().get("ANIMAL") == 3 : "Bad setRO implementation";
        assert !p1.equals(p2) : "LOL they should not be equals";
    }

    @Test
    public void TesterPlayerSHand(){
        p1.addCard(c);
        //Testing if the card is added correctly in the player's hand
        assert p1.getHand().size() == 1 : "Not added the card in the hand of the player";
        assert p1.getHand().get(0).equals(c) : "Bad insertion of card in hand, they are not equals";
    }

    @Test
    public void TesterPlayerSTable(){
        p1.addCard(c);
        p1.addCardBack(c);
        p1.addCardToMap(c,0,0);
        p1.addResourceOrObject("INKWELL",p1.getNumOfResourceAndObject().get("INKWELL") == null ? 1 : p1.getNumOfResourceAndObject().get("INKWELL") + 1);
        p1.addResourceOrObject("PLANT",p1.getNumOfResourceAndObject().get("PLANT") == null ? 1 : p1.getNumOfResourceAndObject().get("PLANT") +1 );

        //Testing if the card is added correctly in the table and its consequences
        assert p1.getMap().containsKey(c) : "Bad insertion of card in the map";
        assert p1.getMap().get(c)[0] == 0 && p1.getMap().get(c)[1] == 0 : "Values are wrong or they not correspond to the right card";
        assert p1.getNumOfResourceAndObject().get("INKWELL") == 1 : "The quantity of INKWELL is wrong!";
        assert p1.getNumOfResourceAndObject().get("PLANT") == 1: "The quantity of PLANT is wrong!";

    }
    @Test
    public void TestingEquals(){

        assert p3.equals(p2) : "LOL they should be equals";
        assert !p3.equals(p4) : "LOL they should NOT be equals";
    }
}

