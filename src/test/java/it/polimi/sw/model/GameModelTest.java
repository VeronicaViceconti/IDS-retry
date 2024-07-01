package it.polimi.sw.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameModelTest {

    private ArrayList<Player> players = new ArrayList<>();
    private HashMap<Card,Card> corrGold;
    private HashMap<Card,Card> corrRes;
    private HashMap<Card,Card> corrInit;
    private final ArrayList<Card> initialCard = new ArrayList<>();
    private ArrayList<Card> resourceCard = new ArrayList<>();
    private final ArrayList<Card> goldCard = new ArrayList<>();
    private final ArrayList<Objective> objectiveCards = new ArrayList<>();

    private Game game;
    @BeforeEach
    void setUp(){
        ObjectMapper objectMapper = new ObjectMapper();

        List<Card> newCards = null;
        List<InitialCard> newInitialCard = null;
        List<ResourceCard> newResourceCard = null;
        List<GoldCard> newGoldCard = null;
        ArrayList<Objective> newObjectiveCards = null;
        try {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("/ResourceCards.json");

            newResourceCard = objectMapper.readValue(resourceStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ResourceCard.class)
            );
            resourceStream = getClass().getClassLoader().getResourceAsStream("/GoldCards.json");

            newGoldCard = objectMapper.readValue(resourceStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, GoldCard.class)
            );
            resourceStream = getClass().getClassLoader().getResourceAsStream("/InitialCards.json");

            newInitialCard = objectMapper.readValue(resourceStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, InitialCard.class)
            );
            resourceStream = getClass().getClassLoader().getResourceAsStream("ObjectiveCards.json");

            newObjectiveCards = objectMapper.readValue(resourceStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Objective.class)
            );
            objectiveCards.addAll(newObjectiveCards);
            initialCard.addAll(newInitialCard);
            resourceCard.addAll(newResourceCard);
            goldCard.addAll(newGoldCard);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        players.add(new Player("Piero",Pion.pion_red));
        players.add(new Player("Pippo",Pion.pion_yellow));
        players.add(new Player("Maria",Pion.pion_green));

        corrGold = new HashMap<Card, Card>();
        corrRes = new HashMap<Card, Card>();
        corrInit = new HashMap<Card, Card>();

        for(int i=0,j=0; j < resourceCard.size()/2;i+=2,j++){
            corrRes.put(resourceCard.get(i),resourceCard.get(i+1));
            //System.out.println(j+" fronte-: "+(i)+" della carta "+resourceCard.get(i).getId()+" retro: "+(i+1)+" della carta "+resourceCard.get(i+1).getId());
        }
        for(int i=0,j=0; j < goldCard.size()/2;i++,j++){
            corrGold.put(goldCard.get(i),goldCard.get(i+40));
            //System.out.println("fronte--: "+(i)+" della carta "+goldCard.get(i).getId()+" retro: "+(i+40)+" della carta "+goldCard.get(i+40).getId());
        }

        //System.out.println(corrRes.keySet().size());
        for(int i=0; i< initialCard.size()/2; i++){
            corrInit.put(initialCard.get(i),initialCard.get(i+6));
        }
        /*for (Map.Entry<Card,Card> x:corrInit.entrySet()) {
            System.out.println("fronte init: "+x.getKey().getId()+" retro: "+x.getValue().getId());
        }*/

        game = new Game(1,3,players,corrRes,corrGold,corrInit,objectiveCards);
    }

    @Test
    void testConditionsInitialiseGame(){

        game.setGameState(GameState.DRAWCARD);
        assertEquals(GameState.DRAWCARD, game.getGameState());
        game.setPlayerCurrentTurn(players.get(0));
        assertEquals("Piero", game.getCurrPlayer().getNickName());

        assertEquals(resourceCard.size()/2-2,game.getFacedownResource().size());
        assertEquals(2,game.getFaceupResource().size());

    }

    @Test void testerDrawMethods(){
        // Testing draw methods
        game.drawFaceUpCard("Gold",1);
        assertEquals(2,game.getFaceupGold().size());
        assertEquals(goldCard.size()/2-3,game.getFacedownGold().size());

        game.drawFaceUpCard("Resource",0);
        assertEquals(2,game.getFaceupResource().size());
        assertEquals(resourceCard.size()/2-3,game.getFacedownResource().size());

        game.drawFaceDownCard("Gold");
        assertEquals(goldCard.size()/2-4,game.getFacedownGold().size());


    }

    @Test void TesterSetterGetter(){

        game.setPlayerCurrentTurn(new Player("Filippo",Pion.pion_black));

        assertEquals("Filippo",game.getCurrPlayer().getNickName());

        game.setFacedownGold(goldCard);
        assertEquals(goldCard,game.getFacedownGold());
        game.setFacedownResource(resourceCard);
        assertEquals(resourceCard,game.getFacedownResource());

        assertEquals(1,game.getId());

        assertEquals(3,game.getTotPlayers().size());

        Player player = new Player("Veronica",Pion.pion_blue);
        player.setPoints(5);
        game.setPlayerCurrentTurn(player);

        assertEquals(5,game.getCurrPlayer().getPoints());
        assertEquals("Veronica",game.getPlayerCurrentTurn().getNickName());

        Resources resource1=Resources.FUNGI;
        Resources resource2=Resources.FUNGI;
        Resources resource3=Resources.FUNGI;
        List<Resources> resourcesRequired=new ArrayList<>(Arrays.asList(resource1,resource2,resource3));
        Conditions condition2=new Conditions(97,0,ObjectiveTypes.ResourceFilling,resourcesRequired,null,null,null,null,null);
        Conditions condition1=new Conditions(101,3, ObjectiveTypes.ThreeSameColourPlacing,null,null,"blue",null,2,null);
        Objective objective=new Objective(2,condition1,false);
        Objective objective2=new Objective(3,condition2,false);
        ArrayList<Objective> objectives=new ArrayList<>(Arrays.asList(objective,objective2));
        game.setCommonObjective(objectives);
        game.getCommonObjective().getFirst();
        assertEquals(objective,game.getCommonObjective().getFirst());

        game.setMaxPlayersNumbers(2);
        assert game.getTotPlayers().size()==2 : "tot player set not properly";

        game.setAllObjective(objectives);
        assertEquals(objective,game.getAllObjective().getFirst());
    }

    @Test void testerFlipCard(){

        Card c = game.getFaceupResource().getFirst();
        //game.getFaceupResource().forEach(System.out::println);
        //System.out.println("c: "+c.getId());
        Card retroC = corrRes.get(c);
        //System.out.println(retroC.getId());

        assertEquals(retroC,game.flipCard(game.getFaceupResource().getFirst()));

        c = game.getFacedownResource().getFirst();
        for (Card cardWanted : corrRes.keySet()) {
            if (c.equals(corrRes.get(cardWanted))) {
                retroC = cardWanted;
            }
        }
        //System.out.println("retro: "+c.getId()+" fronte!:"+retroC.getId());
        assertEquals(retroC,game.flipCard(game.getFacedownResource().getFirst()));

    }
}
