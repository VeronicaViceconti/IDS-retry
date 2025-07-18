package it.polimi.sw.view.GraphicalUserInterface;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Message.ViewMessage.SendingCardPlayed;
import it.polimi.sw.network.Message.ViewMessage.SendingCardToDraw;
import it.polimi.sw.network.Message.ViewMessage.SendingPrivateObjective;
import it.polimi.sw.network.Message.serverMessage.ErrorType;
import it.polimi.sw.view.GraphicalUserInterface.main.Framework;
import it.polimi.sw.view.View;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the graphical user interface (GUI) for a client application.
 * It extends the `View` class and provides functionalities for displaying game information and potentially interacting with the user.
 */
public class GUI extends View {

    Timer timer = null;
    private Framework frame;
    private ArrayList<Card> backHand;
    private ArrayList<Card> frontHand;

    /**
     * Default constructor for the GUI class.
     * This constructor likely initializes the GUI components and internal data structures.
     */
    public GUI() {
        backHand = new ArrayList<>();
        frontHand = new ArrayList<>();
    }

    /**
     * Initializes the game window and sets the layout manager.
     * This method is likely called when the game starts or restarts.
     *
     * @Override
     */
    @Override
    public void initGame() {
        frame = new Framework(this);
        frame.setLayout(new BorderLayout());
    }

    /**
     * Method invocated at the beginning to create the pions and their positioning at 0 points
     * @param pions
     */
    @Override
    public void initializePions(int indexMe,ArrayList<Pion> pions){
        this.frame.initializePions(indexMe,pions);
    }

    /**
     *
     * Method that manage the drawing card phase of the player, ask the deck and the card he want to draw
     */
    @Override
    public void drawCard() {
        ArrayList<Integer> infoCard;
        infoCard = frame.drawCard();

        if(infoCard.size() == 1) {
            notify(new SendingCardToDraw(infoCard.get(0)));
        }else if(infoCard.size() == 2) {
            notify(new SendingCardToDraw(infoCard.get(0), infoCard.get(1)));
        }


        timer = new Timer(4 * 1000, e -> {
            frame.notYourTurn();
            if(timer != null)
                timer.stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     *This method is invocated internally by the HandAndObjectivesPanel class after player clicked the flip card button
     * @param oldSide the actual side of the card, 1 = front, 2 = back
     */
    @Override
    public void cardToFlip(String oldSide) throws RemoteException {

        if(oldSide.equals("1")) {
            this.addFlippedCard(backHand);
        }else {
            this.addFlippedCard(frontHand);
        }
    }

    /**
     * Used to ask the player the coordinates of where to play the card
     * @return the x,y coordinates of where to play the card
     */
    private Integer[] selectCardCoordinatesWhereToPut() {
        return frame.chooseCoordinates(availablePositions,nickname);
    }

    /**
     * Used to ask the player the card he wants to play
     * @return 1,2,3, the card he wants to play and the current side of the card to play
     */
    private Integer[] selectCardToPut() {
        return frame.chooseCardToPut();
    }

    /**
     * Method invocated at the beginning of the game to initialize the common table
     * @param fdG face down gold card of the game
     * @param fdR face down resource card of the game
     * @param fuG face up gold cards of the game
     * @param fuR face up resource cards of the game
     * @param commonObj 2 common objectives of the game
     */
    @Override
    public void setBoardData(Card fdG,Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,ArrayList<Objective> commonObj) {
        frame.initializeCommonTable(fdG,fdR,fuG,fuR,commonObj);
    }

    @Override
    public void addToTimeLine(Card timeLine) {

    }


    /**
     * Show the new player's hand
     * @param c player's hand
     */
    @Override
    public void addFlippedCard(ArrayList<Card> c) {
        this.showPlayerHand(c);
    }

    /**
     * Used to show to the user that rn is not his turn!
     */
    @Override
    public void notYourTurn() {
        frame.notYourTurn();
    }

    /**
     * The 2 params are the 2 objectives, and client have to choose one for the entire game
     * @param o1 first objective
     * @param o2 second objective
     */
    @Override
    public void secObjtoChoose(Objective o1, Objective o2)
    {

        int chosen = frame.choosePrivateObjective(o1,o2);

        if(chosen == 1) {
            notify(new SendingPrivateObjective(o1));
            showPrivateObjective(o1);
        }else
        {
            notify(new SendingPrivateObjective(o2));
            showPrivateObjective(o2);
        }
    }

    /**
     * Method invocated when server says it's the player's turn! Used to init the turn
     */
    @Override
    public void myTurnNotification(HashMap<Card, Integer[]> man, ArrayList<Card> hand,ArrayList<Card> handBack) {
        if(hand != null && handBack != null){
            this.frontHand = new ArrayList<>(hand);
            this.backHand = new ArrayList<>(handBack);
        }

        frame.turnNotification(this,nickname);

    }

    /**
     *
     * Method invocated when it's the player's turn and he can flip the cards and then click this button
     * It's viewed only during the play card phase
     */
    public void readyButtonClicked(int totCards) throws RemoteException {
        Integer[] whichCardAndSide = this.selectCardToPut();
        Integer[] coord = new Integer[2];
        if(totCards > 1)
             coord = this.selectCardCoordinatesWhereToPut();
        else {
            coord[0] = 0;
            coord[1] = 0;
        }

        frame.removeCardFromHand(whichCardAndSide[0]-1);


        if(whichCardAndSide[1] == 1)
            notify(new SendingCardPlayed(frontHand.get(whichCardAndSide[0]-1),coord));
        else
            notify(new SendingCardPlayed(backHand.get(whichCardAndSide[0]-1),coord));
    }


    @Override
    public void announceWinner(List<Player> winner) {
        frame.announceWinners(winner);
    }

    /**
     *
     * @param p the player to update
     * @param c the card to be added to the manuscript
     * @param x coordinate of the card
     * @param y coordinate of the card
     * @param numOfResourceAndObject all the resources and objects of the player
     * @param hand cards of the player
     * @param points points of the player
     */
    @Override
    public void updatePlayerPlayCard(Player p,Card c, Integer x, Integer y,
                                     HashMap<String, Integer> numOfResourceAndObject, ArrayList<Card> hand,ArrayList<Card> handBack,Integer points) throws RemoteException {
        if(hand != null && handBack != null){
            this.frontHand = new ArrayList<>(hand);
            this.backHand = new ArrayList<>(handBack);
            this.frame.updatePlayerResAndObj(numOfResourceAndObject);
        }

        System.out.println("Update manuscript");
        this.frame.updateManuscript(p.getNickName(),c,x,y);



        this.frame.updatePoints(p.getPion(),points);
    }


    /**
     * Used to add the points at the end of the game
     * @param p the player to update
     * @param points the new points of the player
     */
    @Override
    public void addPoints(Player p, int points) {
        this.frame.updatePoints(p.getPion(),points);
    }

    /**
     * Handles errors that occur during the game.
     * This method is likely called by the framework when an error is encountered.
     *
     * @param type The type of error that occurred.
     *
     * @Override
     */
    @Override
    public void dealWithError(ErrorType type) {
        frame.showError(type,this);
        this.showPlayerHand(frontHand);
    }

    /**
     * Method invocated when server replies with updated hand or the first random hand at the beginning of the game
     * @param hand, the player's hand to be shown
     */
    @Override
    public void showPlayerHand(ArrayList<Card> hand) {
        if(hand.getFirst().getSide() == 1)
            frontHand = new ArrayList<>(hand);
        if(frame.alreadyCreatedHand()){
            frame.updateHand(hand);
        }else{
            frame.addHand(hand);
            if(hand.size() == 1) {
                frame.addFlipButton(this);
            }
        }
    }

    @Override
    public void showPlayerTable(HashMap<Card, Integer[]> map) {

        for (Map.Entry<Card, Integer[] > card :map.entrySet()) {
            frame.updateManuscript(nickname,map.keySet().iterator().next(),card.getValue()[0],card.getValue()[1]);
        }
    }

    /**
     * Method invocated when server replies with updated common table
     * @param faceDownGold face down gold card of the game
     * @param faceDownResource face down resource card of the game
     * @param faceupGold face up gold cards of the game
     * @param faceupResource face up resource cards of the game
     *
     */
    @Override
    public void showCommonTable(Card faceDownGold, Card faceDownResource, ArrayList<Card> faceupGold, ArrayList<Card> faceupResource) {
        frame.updateCommoneTable(faceDownGold,faceDownResource,faceupGold,faceupResource);
    }


    /**
     *Show the private objective of the player
     * @param obj the objective to be shown
     */
    @Override
    public void showPrivateObjective(Objective obj) {
        this.frame.addPrivateObjective(obj,this);
    }

    /**
     * Displays a message in the game chat window.
     * This method is likely called whenever a new message is received for the game chat.
     *
     * @param message The message to be displayed in the chat window.
     *
     * @Override
     */
    @Override
    public void showGameChat(String message) {
        this.frame.showGameChat(message);

    }

    @Override
    public void showGameStart() {

    }

    @Override
    public void showPlayerState(Player p) {

    }

    /**
     * Notifies the user that it is their turn to play a card.
     * This method is likely called remotely by the server when it's the user's turn.
     *
     * @throws RemoteException This exception is thrown if a communication error occurs during the remote call.
     * @Override
     */
    @Override
    public void readPlayCardInput() {
        this.frame.turnNotification(this,nickname);
    }

    /**
     * Notifies the user that they can draw a card.
     * This method is likely called remotely by the server when the user can draw a card.
     *
     * @throws RemoteException This exception is thrown if a communication error occurs during the remote call.
     * @Override
     */
    @Override
    public void readDrawCardInput(){
        this.frame.drawCard();
    }

    @Override
    public void chatWait() {

    }

    @Override
    public void chatUnblockWait() {

    }

    @Override
    public void createTabbedManuscripts(ArrayList<String> names) {
        this.frame.createTabbedManuscripts(names);

    }

    @Override
    public void gameAlmostFinished() {

    }


    @Override
    public void showCommonObjectives(ArrayList<Objective> obj) {

    }

    @Override
    public void showAvailablePositions() {

    }

    /**
     * Updates the GUI's internal representation of the user's back hand.
     * This method likely receives the updated hand information from the server
     * and stores it for displaying or performing further actions.
     *
     * @param handBack The new list of cards in the user's back hand.
     */
    @Override
    public void savePlayerHandBack(ArrayList<Card> handBack) {
        this.backHand = new ArrayList<>(handBack);
    }

}
