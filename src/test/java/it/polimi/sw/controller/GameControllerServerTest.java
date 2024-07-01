package it.polimi.sw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.sw.model.*;
import it.polimi.sw.model.Object;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerServerTest {
    private final ArrayList<Player> players = new ArrayList<>();
    private HashMap<Card,Card> corrGold;
    private HashMap<Card,Card> corrRes;
    private HashMap<Card,Card> corrInit;
    private final ArrayList<Card> initialCard = new ArrayList<>();
    private final ArrayList<Card> resourceCard = new ArrayList<>();
    private final ArrayList<Card> goldCard = new ArrayList<>();
    private final ArrayList<Objective> objectiveCards = new ArrayList<>();

    private Game game;
    GameControllerServer gc;
    @BeforeEach
    void setUp(){
        ObjectMapper objectMapper = new ObjectMapper();

        List<Card> newCards = null;
        List<InitialCard> newInitialCard = null;
        List<ResourceCard> newResourceCard = null;
        List<GoldCard> newGoldCard = null;
        ArrayList<Objective> newObjectiveCards = null;
        try {
            newResourceCard = objectMapper.readValue(new File("src/main/resources/InputResource_Json/ResourceCards.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ResourceCard.class)
            );
            newGoldCard = objectMapper.readValue(new File("src/main/resources/InputResource_Json/GoldCards.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, GoldCard.class)
            );
            newInitialCard = objectMapper.readValue(new File("src/main/resources/InputResource_Json/InitialCards.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, InitialCard.class)
            );
            newObjectiveCards = objectMapper.readValue(
                    new File("src/main/resources/InputResource_Json/ObjectiveCards.json"),
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
        players.add(new Player("Pippo",Pion.pion_green));
        players.add(new Player("Maria",Pion.pion_yellow));

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
        //for (Map.Entry<Card,Card> x:corrGold.entrySet()) {
        //System.out.println("fronteGold: "+x.getKey().getId()+" retro: "+x.getValue().getId());
        // }

        game = new Game(1,3,players,corrRes,corrGold,corrInit,objectiveCards);
    }
    @Test
    public void test1(){
        ArrayList<Corner> corners = new ArrayList<>();
        corners.add(new Corner(Object.INKWELL, null,0, true, false));
        ResourceCard resourceCard1=new ResourceCard(corners,2,21,"red",false,false,false,1,Resources.FUNGI);
        ResourceCard resourceCard2=new ResourceCard(corners,2,21,"red",false,false,false,1,Resources.FUNGI);
        ResourceCard resourceCard3=new ResourceCard(corners,2,21,"red",false,false,false,1,Resources.FUNGI);

        ResourceCard resourceCard1_1=new ResourceCard(corners,2,21,"red",false,false,false,2,Resources.FUNGI);

        ResourceCard resourceCard2_1=new ResourceCard(corners,2,21,"red",false,false,false,2,Resources.FUNGI);

        ResourceCard resourceCard3_1=new ResourceCard(corners,2,21,"red",false,false,false,2,Resources.FUNGI);
        HashMap<Card, Card> sidesRes= new HashMap<>();
        ArrayList<Card> resourceList=new ArrayList<>(Arrays.asList( resourceCard2,resourceCard3));
        ArrayList<Card> handBack=new ArrayList<>(Arrays.asList(resourceCard1_1,resourceCard2_1,resourceCard3_1));
        ArrayList<Card> hand=new ArrayList<>(Arrays.asList(resourceCard1,resourceCard2,resourceCard3));
        ArrayList<Card> facedownResource=new ArrayList<>(Arrays.asList(resourceCard1, resourceCard2, resourceCard3));
        ArrayList<Card> faceUpResource=new ArrayList<>(Arrays.asList(resourceCard3_1, resourceCard1_1));
        ArrayList<Card> facedownGold=new ArrayList<>(Arrays.asList(resourceCard2));
        ArrayList<Card> faceUpGold=new ArrayList<>(Arrays.asList(resourceCard1));
        gc = new GameControllerServer(3, null,"Piero",Pion.pion_red);

        //Testing getter and setter methods
        gc.currgame.setPlayerCurrentTurn(players.get(0));
        gc.addPlayertoGame(players.get(1));

        assertEquals(players.get(0),gc.currgame.getPlayerCurrentTurn());

        gc.currgame.currPlayer.setHand(hand);
        gc.currgame.currPlayer.setHandBack(handBack);

        gc.currgame.setFacedownResource(facedownResource);
        gc.currgame.setFacedownGold(facedownGold);
        gc.currgame.setFaceupGold(faceUpGold);
        gc.currgame.setFaceupResource(faceUpResource);

        int numCards = gc.currgame.getFacedownResource().size();
        gc.drawCardFromDeck(gc.currgame.currPlayer, "resource");

        assertEquals(3,gc.currgame.currPlayer.getHand().size());
        assertEquals(numCards,gc.currgame.getFacedownResource().size()); //deve rimanere uguale perchè l'utente non può pescare


        gc.currgame.setGameState(GameState.PLACECARD);
        assertEquals(GameState.PLACECARD, gc.currgame.getGameState());

        //piazzo carta dalla HAND
        gc.placeCard(gc.currgame.currPlayer,gc.currgame.currPlayer.getHand().getFirst(),0,0);
        assertEquals(2, gc.currgame.currPlayer.getHand().size());
        assertEquals(2, gc.currgame.currPlayer.getHandBack().size());
        assertEquals(2, gc.currgame.currPlayer.getPoints());
        assertEquals(true, resourceCard1.getDrawn());
        assertEquals(true, resourceCard1.getUsed());

        //dopo aver piazzato una carta, provo la parte di pescare una carta, deve aumentare la size della hand,la dimensione del facedowngold deve diminuire di 1

        gc.currgame.setFacedownResource(resourceList);

        sidesRes.put(resourceCard2, resourceCard2_1);
        sidesRes.put(resourceCard1, resourceCard1_1);
        sidesRes.put(resourceCard3, resourceCard3_1);

        int oldsize = gc.currgame.currPlayer.getHand().size();
        int oldsizeDeck = gc.currgame.getFacedownResource().size();

        gc.currgame.setSidesResCard(sidesRes);



        gc.drawCardFromDeck(gc.currgame.currPlayer,"Resource");
        gc.currgame.setPlayerCurrentTurn(players.get(0));

        assertEquals(oldsize+1,gc.currgame.currPlayer.getHand().size());
        assertEquals(oldsize+1,gc.currgame.currPlayer.getHandBack().size());

        assertEquals(oldsizeDeck-1,gc.currgame.getFacedownGold().size());



        gc.currgame.currPlayer.setHand(hand);
        gc.currgame.currPlayer.setHandBack(handBack);

        gc.placeCard(gc.currgame.currPlayer,gc.currgame.currPlayer.getHand().get(1),0,0 );

        gc.currgame.setFaceupResource(faceUpResource);
        gc. currgame.setFacedownResource(facedownResource);

        gc.drawCardFromTable(gc.currgame.currPlayer, 1, "Resource");

        gc.currgame.setPlayerCurrentTurn(players.getFirst());
        assertEquals(3, gc.currgame.currPlayer.getHand().size());
        assertEquals(3, gc.currgame.currPlayer.getHandBack().size());
        assertEquals(2, facedownResource.size());

        //test partita finita
        //aggiungo i vari campi a maria

        players.getLast().setHand(hand);
        players.getLast().setHandBack(handBack);

        Resources resource1=Resources.FUNGI;
        Resources resource2=Resources.FUNGI;
        Resources resource3=Resources.FUNGI;
        List<Resources> resourcesRequired=new ArrayList<>(Arrays.asList(resource1,resource2,resource3));

        Conditions condition1=new Conditions(101,3, ObjectiveTypes.ThreeSameColourPlacing,null,null,"blue",null,2,null);
        Objective objective=new Objective(2,condition1,false);

        Conditions condition2=new Conditions(97,0,ObjectiveTypes.ResourceFilling,resourcesRequired,null,null,null,null,null);
        Objective objective2=new Objective(3,condition2,false);
        ArrayList<Objective> objectives=new ArrayList<>(Arrays.asList(objective,objective2));

        gc.currgame.currPlayer.setObjective(objective);
        gc.currgame.setCommonObjective(objectives);
        gc.currgame.getTotPlayers().getFirst().setObjective(objective2);


        gc.currgame.setPlayerCurrentTurn(gc.currgame.getTotPlayers().getLast());
        assertEquals(gc.currgame.getTotPlayers().getLast(), gc.currgame.currPlayer);
        gc.currgame.currPlayer.setPoints(23);
        assertEquals(23,gc.currgame.currPlayer.getPoints());
        gc.setEndGame(false);
        assertEquals(false,gc.isEndGame());

        gc.nextTurn();







        gc.currgame.setMaxPlayersNumbers(2);
        assertEquals(true,gc.isItFull());

        System.out.println("All tests passed successfully.");

    }

    @Test
    public void test2(){
        GameControllerServer newGameC = new GameControllerServer(0,null,null,null);

        //inizializzo un player

        players.getFirst().addCard(resourceCard.get(2));
        players.getFirst().addCardBack(newGameC.currgame.flipCard(resourceCard.get(2)));
        players.getFirst().addCard(goldCard.get(9));
        players.getFirst().addCardBack(newGameC.currgame.flipCard(goldCard.get(9)));
        players.getFirst().addCard(resourceCard.get(5));
        players.getFirst().addCardBack(newGameC.currgame.flipCard(resourceCard.get(5)));
        players.getFirst().setPoints(0);
        players.getFirst().setId(1);
        players.getFirst().setObjective(objectiveCards.get(7));

    //inizializzare un altro player in modo da testare drawcardfromdeck

        players.get(1).addCard(resourceCard.get(10));
        players.get(1).addCardBack(newGameC.currgame.flipCard(resourceCard.get(10)));
        players.get(1).addCard(goldCard.get(24));
        players.get(1).addCardBack(newGameC.currgame.flipCard(goldCard.get(24)));
        players.get(1).addCard(resourceCard.get(4));
        players.get(1).addCardBack(newGameC.currgame.flipCard(resourceCard.get(4)));
        players.get(1).setPoints(0);
        players.get(1).setId(2);
        players.get(1).setObjective(objectiveCards.get(3));

        newGameC.currgame.setPlayerCurrentTurn(players.getFirst());
        newGameC.currgame.setGameState(GameState.PLACECARD);
       //initialisecard
        for (Card hand:newGameC.currgame.currPlayer.getHand()) {
            System.out.println("cartaX nella mano: "+hand.toString());
        }
        newGameC.placeCard(newGameC.currgame.currPlayer, newGameC.currgame.currPlayer.getHand().get(0) , 0,0);
        assertEquals(3,newGameC.currgame.currPlayer.getHand().size());
        assertFalse(newGameC.currgame.currPlayer.getNumOfResourceAndObject().isEmpty());
        assertEquals(0,newGameC.currgame.currPlayer.getPoints());


        //placecard di una carta che non posso mettere
        newGameC.currgame.setGameState(GameState.PLACECARD);
        newGameC.placeCard(newGameC.currgame.currPlayer,newGameC.currgame.currPlayer.getHand().get(1), 1,1);
        HashMap<String, Integer> numOfResourceAndObject=newGameC.currgame.currPlayer.getNumOfResourceAndObject();
        assertEquals(newGameC.currgame.currPlayer.getHand().size(),3); //la carta oro non può giocarla perchè non ha le risorse!
        assertFalse(newGameC.currgame.currPlayer.getNumOfResourceAndObject().isEmpty());

        //carta che posso mettere
        newGameC.currgame.setGameState(GameState.PLACECARD);
        newGameC.placeCard(newGameC.currgame.currPlayer,newGameC.currgame.currPlayer.getHand().get(2), 1,1);
        assertEquals(newGameC.currgame.currPlayer.getHand().size(),2); //la carta resource può giocarla perchè non ha le risorse!

        //drawcardfromtable
        newGameC.currgame.setGameState(GameState.DRAWCARD);
        System.out.println("Player"+newGameC.currgame.currPlayer.getNickName());
        newGameC.drawCardFromTable(newGameC.currgame.currPlayer,1,"Gold");
        System.out.println("now hand is "+newGameC.currgame.currPlayer.getHand().size());

        //impostato il giocatore successivo

        //initialisecard
        newGameC.currgame.setPlayerCurrentTurn(players.get(1));
        newGameC.currgame.setGameState(GameState.PLACECARD);
        System.out.println("Player"+newGameC.currgame.currPlayer.getNickName());
        newGameC.placeCard(newGameC.currgame.currPlayer, newGameC.currgame.currPlayer.getHand().get(0) , 0,0);
        assertEquals(newGameC.currgame.currPlayer.getHand().size(),3);
        assertFalse(newGameC.currgame.currPlayer.getNumOfResourceAndObject().isEmpty());
        assertEquals(newGameC.currgame.currPlayer.getPoints(),0);

        for (Card hand:newGameC.currgame.currPlayer.getHand()) {
            System.out.println("carta nella mano: "+hand.toString());
        }

        //placecard di una carta che non posso mettere
        newGameC.currgame.setGameState(GameState.PLACECARD);
        newGameC.placeCard(newGameC.currgame.currPlayer,newGameC.currgame.currPlayer.getHand().get(1), -1,-1);
        HashMap<String, Integer> numOfResourceAndObject1=newGameC.currgame.currPlayer.getNumOfResourceAndObject();
        assertEquals(newGameC.currgame.currPlayer.getHand().size(),3);
        assertFalse(newGameC.currgame.currPlayer.getNumOfResourceAndObject().isEmpty());

        //drawcardfromdeck
        newGameC.currgame.setGameState(GameState.DRAWCARD);
        newGameC.drawCardFromDeck(newGameC.currgame.currPlayer,"Resource");
        assertEquals(newGameC.currgame.currPlayer.getHand().size(),3); //hai ancora 3 carte, non puoi inserirne un altra
        assertFalse(newGameC.currgame.currPlayer.getNumOfResourceAndObject().isEmpty());


        //passo il turno e provo placecard di una carta che non posso mettere
        newGameC.currgame.setPlayerCurrentTurn(players.get(0));


        assertEquals(newGameC.currgame.currPlayer.getHand().size(),3);
        newGameC.currgame.setGameState(GameState.PLACECARD);
        newGameC.placeCard(newGameC.currgame.currPlayer,newGameC.currgame.currPlayer.getHand().get(2),1,1 ); //c'è già una carta, non posso metterla!
        assertEquals(newGameC.currgame.currPlayer.getHand().size(),3);

        //last turn
        newGameC.currgame.currPlayer.setPoints(20);
        newGameC.nextTurn();


        System.out.println("All tests passed successfully.");


    }

    @Test
    public void test3(){
        int i,j=0;

        //test setter e getter
        GameControllerServer gameControllerServer=new GameControllerServer(3,null,"Stefania",Pion.pion_green);
        gameControllerServer.addPlayertoGame(players.getFirst());


        //setLobbyReference
        gameControllerServer.setLobbyreference(2);
        assertEquals(2,gameControllerServer.getLobbyreference());




         gameControllerServer.currgame.setPlayerCurrentTurn(gameControllerServer.currgame.getTotPlayers().getFirst());
         Resources resource1=Resources.FUNGI;
         Resources resource2=Resources.FUNGI;
         Resources resource3=Resources.FUNGI;
         List<Resources> resourcesRequired=new ArrayList<>(Arrays.asList(resource1,resource2,resource3));
         Conditions condition2=new Conditions(97,0,ObjectiveTypes.ResourceFilling,resourcesRequired,null,null,null,null,null);
         Conditions condition1=new Conditions(101,3, ObjectiveTypes.ThreeSameColourPlacing,null,null,"blue",null,2,null);
         Objective objective=new Objective(2,condition1,false);
         Objective objective2=new Objective(3,condition2,false);
         ArrayList<Objective> objectives=new ArrayList<>(Arrays.asList(objective,objective2));



         gameControllerServer.currgame.getTotPlayers().getFirst().setObjective(objective);
         assertEquals(objective, gameControllerServer.currgame.getTotPlayers().getFirst().getObjective());
         gameControllerServer.currgame.getTotPlayers().get(1).setObjective(objective2);
         assertEquals(objective2, gameControllerServer.currgame.getTotPlayers().get(1).getObjective());
         gameControllerServer.currgame.setCommonObjective(objectives);

         //setEndGame
        gameControllerServer.setEndGame(true);
        assertEquals(true,gameControllerServer.isEndGame());

        //richiamo fine gioco
        gameControllerServer.currgame.setPlayerCurrentTurn(gameControllerServer.currgame.getTotPlayers().getLast());
        gameControllerServer.currgame.currPlayer.setPoints(22);
        gameControllerServer.nextTurn();

    }

    @Test
    public void test4(){
        int i,j=0;
        GameControllerServer gameControllerServer=new GameControllerServer(1,null,"stefania",Pion.pion_yellow);
        gameControllerServer.currgame.setMaxPlayersNumbers(3);
        gameControllerServer.addPlayertoGame(new Player("antonio",Pion.pion_blue));


        for(i=0;i<gameControllerServer.currgame.getTotPlayers().size();i++){
            if(gameControllerServer.currgame.getTotPlayers().get(i).getNickName().equals("antonio")){
                j=i;
            }
        }

        assertEquals("antonio",gameControllerServer.currgame.getTotPlayers().get(j).getNickName());
        assertEquals(Pion.pion_blue,gameControllerServer.currgame.getTotPlayers().get(j).getPion());

    }

}