package it.polimi.sw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.sw.model.*;
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

        //Card c=new Card();
        //gc = new GameControllerServer(game);

        //Testing getter and setter methods
        gc.currgame.setPlayerCurrentTurn(players.get(0));
        System.out.println(gc.currgame.getPlayerCurrentTurn().toString());
        assertEquals(players.get(0),gc.currgame.getPlayerCurrentTurn());


        //int numCards = gc.currgame.getFacedownResource().size();
        //gc.drawCardFromDeck(1); //resource deck
        //assertEquals(numCards,gc.currgame.getFacedownResource().size()); //deve rimanere uguale perchè l'utente non può pescare

        for (Card hand:gc.currgame.currPlayer.getHand()) {
            System.out.println("carta nella mano: "+hand.toString());
        }

        gc.currgame.setGameState(GameState.PLACECARD);
        //piazzo carta iniziale, dalla HAND deve andare in TABLE + punti rimanere 0 e aumentare risorse/oggetti di 1
        gc.placeCard(gc.currgame.currPlayer,gc.currgame.currPlayer.getHand().get(0),0,0);
        assertEquals(0, gc.currgame.currPlayer.getHand().size());
        assertEquals(0, gc.currgame.currPlayer.getHandBack().size());
        for (Map.Entry<Card,Integer[]> table:gc.currgame.currPlayer.getMap().entrySet()) {
            System.out.println("carta nel tavolo: "+table.getKey()+", coordinate: "+table.getValue()[0]+" "+table.getValue()[1]);
        }
        assertEquals(0,gc.currgame.currPlayer.getPoints());

        /*for (Map.Entry<String, Integer> element: gc.currgame.currPlayer.getNumOfResourceAndObject().entrySet()) {
            System.out.println("elemento: "+ element.getKey()+ " totale:"+element.getValue());
        }
        assertEquals(c.getPermanentResource().size(),gc.currgame.currPlayer.getNumOfResourceAndObject().size());
        for(Map.Entry<String,Integer> res :gc.currgame.currPlayer.getNumOfResourceAndObject().entrySet()){
            assertEquals(1,res.getValue());
        }*/
        assertEquals(GameState.DRAWCARD,gc.currgame.getGameState());

        //dopo aver piazzato una carta, provo la parte di pescare una carta, deve aumentare la size della hand,
        //la dimensione del facedowngold deve diminuire di 1
        int oldsize = gc.currgame.currPlayer.getHand().size();
        int oldsizeDeck = gc.currgame.getFacedownGold().size();
        gc.drawCardFromDeck(gc.currgame.currPlayer,"Gold"); //pesco da gold
        assertEquals(oldsize+1,gc.currgame.currPlayer.getHand().size());
        assertEquals(oldsize+1,gc.currgame.currPlayer.getHandBack().size());
        for (Card card:gc.currgame.currPlayer.getHand()) {
            System.out.println("carta pescata: "+card.toString());
            System.out.println("carta pescata back: "+gc.currgame.currPlayer.getHandBack().get(0).toString());
        }
        assertEquals(oldsizeDeck-1,gc.currgame.getFacedownGold().size());

        //ora provo a pescare dal tavolo, tanto ho meno di 3 carte in mano quindi me lo fa fare
        //controllo che aumenti la dimensione di hand e che rimanga uguale quella di faceupResource perchè c'è il refill
        gc.currgame.setGameState(GameState.DRAWCARD);
        oldsize = gc.currgame.currPlayer.getHand().size();
        oldsizeDeck = gc.currgame.getFaceupResource().size();
        gc.drawCardFromTable(gc.currgame.currPlayer,1,"Resource"); //prima carta dall'array a terra
        gc.currgame.setPlayerCurrentTurn(players.get(1));
        assertEquals(oldsize+1,gc.currgame.currPlayer.getHand().size());
        assertEquals(oldsize+1,gc.currgame.currPlayer.getHandBack().size());
        for (Card table:gc.currgame.currPlayer.getHand()) {
            System.out.println("carte in mano secondo giro : "+table.toString());
        }
        for (Card table:gc.currgame.currPlayer.getHandBack()) {
            System.out.println("carte back in mano secondo giro : "+table.toString());
        }
        assertEquals(oldsizeDeck,gc.currgame.getFaceupResource().size());

        //provo a mettere in table una goldCard per testare se posso metterla DA CONTINUARE
        gc.placeCard(gc.currgame.currPlayer,gc.currgame.currPlayer.getHand().getFirst(),0,1);
        //TESTARE LA PARTE DI getWINNER PIU' METODI PRIVATI LEGATI A GETWINNER

        //testing the flip method
        //Card card = gc.currgame.currPlayer.getHand().getFirst();
        //card = corrGold.get(card);
        //assertEquals(card,gc.flipCardHand(gc.currgame.currPlayer.getHand().getFirst()));

        // If no AssertionError is thrown, all tests pass

        gc.currgame.setMaxPlayersNumbers(3);
        assertEquals(true,gc.isItFull());

        System.out.println("All tests passed successfully.");

    }

    @Test
    public void test2(){
        GameControllerServer newGameC = new GameControllerServer(0,null,null,null);
      //inizializzazione del player che esegue un turno


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
        GameControllerServer gameControllerServer=new GameControllerServer(0,null,null,null);

        gameControllerServer.setLobbyreference(2);
        assertEquals(2,gameControllerServer.getLobbyreference());

        gameControllerServer.setLobbyreference(1);
        assertEquals(1,gameControllerServer.getLobbyreference());

        gameControllerServer.setEndGame(true);
        assertEquals(true,gameControllerServer.isEndGame());

        gameControllerServer.currgame.setPlayerCurrentTurn(gameControllerServer.currgame.getTotPlayers().get(1));
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
        gameControllerServer.currgame.getTotPlayers().get(1).setObjective(objective2);
        gameControllerServer.currgame.getTotPlayers().get(2).setObjective(objective);
        gameControllerServer.currgame.setCommonObjective(objectives);


       gameControllerServer.currgame.setPlayerCurrentTurn(players.getLast());
       gameControllerServer.currgame.currPlayer.setPoints(22);
       gameControllerServer.nextTurn();

    }

    @Test
    public void test4(){
        int i,j=0;
        GameControllerServer gameControllerServer=new GameControllerServer(0,null,null,null);
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