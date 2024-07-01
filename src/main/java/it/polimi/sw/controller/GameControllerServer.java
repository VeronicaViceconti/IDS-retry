package it.polimi.sw.controller;

import it.polimi.sw.model.*;
import it.polimi.sw.network.CommonGameLogicServer;
import it.polimi.sw.network.Message.serverMessage.*;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.sw.model.GameState.*;


public class GameControllerServer  {
    /**
     * Instance of Game, the class in Model.
     */
    public Game currgame;
    /**
     * It's a reference of the match.
     */
    public int lobbyreference;
    private CommonGameLogicServer server;
    /**
     * It's a boolean that determines the last turn.
     */
    private boolean endGame=false; // per avviare ultimo turno

    /**
     * to add the part that receives back the answer. I don't want to wait the answer, players may do it in any order
     */
/*  done in game, initialise Game

    public void distributeSecretObjective() {
        for (Player p : currgame.getTotPlayers()) {
            currgame.notify(new SecretObjReply(currgame.getAllObjective().get(0), currgame.getAllObjective().get(1), p));

            currgame.getAllObjective().remove(0);
            currgame.getAllObjective().remove(1);
        }
    }*/

    /**
     * Retrieves the integer identifier for the game's lobby or room.
     *
     * This method returns the `lobbyreference` value associated with this `GameControllerServer` instance. The lobby reference is
     * likely an integer identifier that uniquely identifies the game's lobby.
     *
     * The value of the lobby reference is set using the `setLobbyreference` method.
     *
     * @return the integer identifier for the game's lobby or room
     */

    public int getLobbyreference() {
        return lobbyreference;
    }


    /**
     * Obtains the `Game` object instance associated with this game controller server.
     *
     * This method returns the `currgame` field, which holds a reference to the `Game` object managing the specific game state,
     * players.
     *
     * The `Game` object is likely created during the initialization of the `GameControllerServer` instance.
     *
     * @return the `Game` object associated with this game controller server
     */
    public Game getCurrgame() {
        return currgame;
    }



    /****************************Initialisation. PreGame phase ***************************************/

/*    public GameControllerServer(int numberOfPlayers, CommonGameLogicServer server, String creator, Pion pion) {
    }
    /**
     * Creates a new GameControllerServer instance for a new game.
     *
     * This constructor is used to create a new game server instance with the following details:
     *  - `numberOfPlayers`: The number of players in the game.
     *  - `server`: A reference to a `CommonGameLogicServer` object, which provides access to shared game logic.
     *  - `creator`: The username or name of the player who created the game.
     *  - `pion`: The game piece (likely a `Pion` object) representing the creator's piece on the board.
     *
     * @param numberOfPlayers the number of players in the game
     * @param server a reference to the CommonGameLogicServer object providing shared game logic
     * @param creator the username or name of the game creator
     * @param pion the game piece representing the creator's piece on the board
     */
    public GameControllerServer(int numberOfPlayers, CommonGameLogicServer server, String creator, Pion pion) {
        this.server = server;
        this.currgame = new Game(0, numberOfPlayers, new ArrayList<>(Arrays.asList(
                new Player(creator, pion))));
    }

    /**
     * Creates a new GameControllerServer instance.
     *
     * This constructor initializes the game controller server with the following information:
     *  - `idlobby`: An integer identifier for the game's lobby.
     *  - `server`: A reference to a `CommonGameLogicServer` object, which provides access to shared game logic.
     *
     * Internally, the constructor creates a new `Game` instance using the provided lobby ID and configuration data. This `Game` object likely manages the game state, players, and logic specific to this game instance.
     *
     * @param idlobby the integer identifier for the game's lobby or room
     * @param server a reference to the CommonGameLogicServer object providing shared game logic
     */
    public GameControllerServer(int idlobby, CommonGameLogicServer server) {
        this.lobbyreference = idlobby;
        this.currgame = new Game(idlobby,server.getCorrRes(),server.getCorrGold(),server.getCorrInit(), server.getObjectiveCards());
    }

    /**
     * Sets the lobby reference identifier for this game instance.
     *
     * This method assigns the provided `lobbyreference` to the internal `lobbyreference` field. This identifier likely refers to
     * the game's lobby.
     *
     * @param lobbyreference the integer identifier for the game's lobby
     */
    public void setLobbyreference(int lobbyreference) {
        this.lobbyreference = lobbyreference;
    }

    /**
     * when a player sends the chosen secret objective, the message in execute calls this method.
     * if all the players chose the secret objective, then we can start the game loop
     */
    public void addSecretObjective(){
        for (Player p: currgame.getTotPlayers()){
            if(p.getObjective() == null){
                return;
            }
        }

        currgame.setGameState(GameState.PLACECARD);
        currgame.notify(new TurnReply(currgame.getCurrPlayer(), currgame.getPlayerCurrentTurn().getAvailablePositions()));

    }

/*
    public void setSecretObjective() {
        currgame.notify();
    } //message can do it by it's own. no need of notify
*/

    /****************************** GAME LOOP **********************************************************/
    /**
     * player asks to place a card in a position in his Manuscript
     * includes control if it is a good moment to place a card and if this card may be played
     * then updates the points and resources of the player
     *
     * after placing a card controller notifies everyone about the changes of the player p
     *
     * @param p player
     * @param card card to place
     * @param x    requested coordinates
     * @param y    requested coordinates
     */
    public void placeCard(Player p, Card card, int x, int y) {
        //controllo parametri esistenti
        if (p == null || card == null) {
            System.out.println("Test. GCS.Player is not found in the player list. p is null");
        }else{
            boolean thereIsPlayer = currgame.getTotPlayers().stream().anyMatch(player -> player.getNickName().equals(p.getNickName()));
            if (!thereIsPlayer) { //player is not in the list of all games
                System.out.println("Test. GCS.Player is not found in the list of players");

                return;
            }
            if(card instanceof InitialCard) {
                int index = p.findCardInHand(card);
                if(index == -1) {  //da testare
                    currgame.notify(new ErrorReply(ErrorType.CARD_NOT_IN_HAND, p));
                    return;
                }
                //update map
                p.addCardToMap(card, x, y);
                p.addToTimeline(card);

                System.out.println("Timeline init during placing card->");
                for(Card c: p.getTimeline())
                    System.out.println("Cartaa -> "+c.toString());

                //remove from hand
                p.getHand().remove(p.getHand().get(index));
                p.getHandBack().remove(p.getHandBack().get(index));

                updateAvailablePositions(p, x, y);
                card.setDrawn(true);
                card.setUsed(true);
                addResourcesOrObjects(card, p);

                p.addCard(currgame.flipCard(currgame.getFacedownGold().getFirst()));
                p.addCardBack(currgame.getFacedownGold().getFirst());
                currgame.getFacedownGold().removeFirst();

                p.addCard(currgame.flipCard(currgame.getFacedownResource().getFirst()));
                p.addCardBack(currgame.getFacedownResource().getFirst());
                currgame.getFacedownResource().removeFirst();

                p.addCard(currgame.flipCard(currgame.getFacedownResource().getFirst()));
                p.addCardBack(currgame.getFacedownResource().getFirst());
                currgame.getFacedownResource().removeFirst();

                ArrayList<Objective> ob12 = currgame.pickupSecretObjective();

                currgame.notify(new SecretObjReply(ob12.get(0), ob12.get(1), p, p.getHand(),p.getHandBack(),p.getMap())); //manda

                //if it covers at least one card. if there are no adjacent cards, then if statement is true and the permission id denied.
            }else if (!currgame.getPlayerCurrentTurn().equals(p)){
                currgame.notify(new NotYourTurnReply(p));
            /*

            check initial card to be placed to send messagge secrectobjreply
            * */
            } else if (!(currgame.getGameState() == GameState.PLACECARD)) {
                currgame.notify(new ErrorReply(ErrorType.NOT_THE_RIGHT_MOMENT_TO_DRAW_CARD, p));
            } else {
                //by this line we are sure that it is the right moment to place a card. we have to control if the placement is correct
                boolean canPlay = mayPlayThisCard(p, card, x, y);
                if (canPlay) {   //now we have to place the card, remove it from the hand,
                    // add points, resources, objects, change the state of the game
                    int index = p.findCardInHand(card);
                    if(index == -1) {  //da testare
                        currgame.notify(new ErrorReply(ErrorType.CARD_NOT_IN_HAND, p));
                        return;
                    }
                    //update map
                    p.addCardToMap(card, x, y);
                    p.addToTimeline(card);
                    //remove from hand
                    p.getHand().remove(p.getHand().get(index));
                    p.getHandBack().remove(p.getHandBack().get(index));

                    updateAvailablePositions(p, x, y);
                    card.setDrawn(true);
                    card.setUsed(true);
                    p.setPoints(p.getPoints() + calculatePointsForCard(card, p));
                    addResourcesOrObjects(card, p);
                    updateCoveredCard(card, p, x, y); //updates the corner "covered" and removes resource or object.


                    if(endGame) {
                        currgame.notify(new UpdatePlayersReply(p, p.getPoints(), card, x, y, p.getNumOfResourceAndObject(), p.getHand(), p.getHandBack(), false));
                        System.out.println("1 server ha mandato UpdatePlayer");
                        currgame.notify(new GameAlmostFinishedReply(currgame.currPlayer)); //doesn't have to draw, might cause issues

                        currgame.setGameState(NOT_MY_TURN);
                        nextTurn();

                    }else{
                        currgame.setGameState(DRAWCARD);
                        currgame.notify(new UpdatePlayersReply(p, p.getPoints(), card, x, y, p.getNumOfResourceAndObject(), p.getHand(), p.getHandBack(), true));
                        System.out.println("2 server ha mandato UpdatePlayer");

                        //currgame.setGameState(NOT_MY_TURN); //nobody can do anything
                        //nextTurn(); // includes notify turn
                    }
                }
            }
        }
    }

    /**
     * Checks if a player is allowed to play a specific card at a given position on the game board.
     *
     * This method performs various checks to determine if the player can play the specified `card` at the position
     * (`x`, `y`) on the board.
     *
     * If all checks pass, the method returns `true`, indicating the player can play the card at the specified position.
     * Otherwise, it returns `false` and notifies the game about the error type.
     *
     * @param p the player object
     * @param card the card the player wants to play
     * @param x the x-coordinate of the desired position
     * @param y the y-coordinate of the desired position
     * @return true if the player can play the card, false otherwise
     * @throws NullPointerException (implicitly) if any of the arguments are null
     */

    private boolean mayPlayThisCard(Player p, Card card, int x, int y) {

        // if you have this card
        if (!p.getHand().contains(card) && !p.getHandBack().contains(card)) {
            currgame.notify(new ErrorReply(ErrorType.CARD_NOT_IN_HAND, p));
            return false;
        }

        //if the card was used before
        else if (card.getUsed()) {
            currgame.notify(new ErrorReply(ErrorType.USED_CARD, p));
            return false;
        }
        else if(!p.getAvailablePositions().contains(new Point2D.Double(x,y))){
            return false; //already precalculated and contain only adeguate positions
        }
/*        control cards that might be covered. control that all the corners under desired position are visible and not covered.
         if there is a card with not visible or covered corner underneath, then player may not put the card above
         */
        else if (!cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, x - 1, y - 1), 1) ||
                !cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, x - 1, y + 1), 3) ||
                !cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, x + 1, y - 1), 0) ||
                !cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, x + 1, y + 1), 2)) {
            currgame.notify(new ErrorReply(ErrorType.HIDDEN_CORNER_COVERED, p));

            return false;
        }

        // if you have enough resources for this GoldCard.
        else if (card.getNecessaryResource() != null ){
            HashMap<Resources, Integer> condition = new HashMap<>();

            //transforming the condition into a hash map to simplify the control
            for (Resources res : (card.getNecessaryResource())) {
                if (condition.containsKey(res)) {
                    condition.put(res, condition.get(res) + 1);
                } else {
                    condition.put(res, 1);
                }
            }
            //if player has less resources than asked, then the permission is denied.
            for (Map.Entry<Resources,Integer> entry: condition.entrySet()) {
                if (p.getNumOfResourceAndObject().get(entry.getKey().toString()) < entry.getValue()) {
                    currgame.notify(new ErrorReply(ErrorType.NOT_ENOUGH_RESOURCES, p));
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * true if present a card in a corner position
     * @param p player
     * @param x x
     * @param y y
     * @return boolean
     */
/*
    private boolean checkCrossedPositions(Player p, int x, int y){
        boolean flag = true;
        flag = (positionOccupied(p, x - 1, y - 1) || positionOccupied(p, x - 1, y + 1)
                || positionOccupied(p, x + 1, y - 1) || positionOccupied(p, x + 1, y + 1));

        return flag;
    }

    */
/**
 * true if there is an adjacent card
 * @param p player
 * @param x x
 * @param y y
 * @return boolean
 */
/*

    private boolean checkPlusPositions(Player p, int x, int y){
        boolean flag = true;
        flag = (positionOccupied(p, x, y - 1) || positionOccupied(p, x, y + 1)
                || positionOccupied(p, x + 1, y) || positionOccupied(p, x + 1, y));
        return flag;
    }

*/

    /**
     * returns a card in the position (x,y). if the card is not present, then return null
     */
    private Card cardInPosition(Player pl, Integer x, Integer y) {
        for(Map.Entry<Card,Integer[]> entry: pl.getMap().entrySet()){
            if(entry.getValue()[0].equals(x)&&entry.getValue()[1].equals(y)){
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * The method checks that the position choosen by the player is valid.
     * @param p The player that wants place the card.
     * @param x The coordinates x.
     * @param y The coordinates y.
     */
    private void updateAvailablePositions(Player p, int x, int y){

        ArrayList<Point2D> newAvailable = new ArrayList<>();
        //candidates to be available spots
        ArrayList<Point2D> pointsToCheck = new ArrayList<>();
        //save the Cross positions that are free, no duplicates in the list
        if (!p.getAvailablePositions().contains(new Point2D.Double(x + 1, y - 1)) && !positionOccupied(p, x+1, y-1)) {
            pointsToCheck.add(new Point2D.Double(x + 1, y + 1));
        }
        if (!p.getAvailablePositions().contains(new Point2D.Double(x - 1, y - 1))&& !positionOccupied(p, x-1, y-1)) {
            pointsToCheck.add(new Point2D.Double(x - 1, y - 1));
        }
        if (!p.getAvailablePositions().contains(new Point2D.Double(x + 1, y + 1))&& !positionOccupied(p, x+1, y+1)) {
            pointsToCheck.add(new Point2D.Double(x + 1, y - 1));
        }
        if (!p.getAvailablePositions().contains(new Point2D.Double(x - 1, y + 1))&& !positionOccupied(p, x-1, y+1)) {
            pointsToCheck.add(new Point2D.Double(x - 1, y + 1));
        }

        ArrayList<Point2D> toRemoveFromOld = new ArrayList<>();
        //remove cross positions if their cross position contains a Hidden corner
        pointsToCheck.removeIf(point -> !(cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int) point.getX() - 1, (int) point.getY() - 1), 1) &&
                cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int) point.getX() - 1, (int) point.getY() + 1), 3) &&
                cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int) point.getX() + 1, (int) point.getY() - 1), 0) &&
                cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int) point.getX() + 1, (int) point.getY() + 1), 2)));

        //looking for the points in available positions, that should be removed due to the placed card.
            for(Point2D oldPoints: p.getAvailablePositions()){
                if (!(cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int)oldPoints.getX() - 1, (int)oldPoints.getY() - 1), 1) &&
                        cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int)oldPoints.getX() - 1, (int)oldPoints.getY() + 1), 3) &&
                        cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int)oldPoints.getX() + 1, (int)oldPoints.getY() - 1), 0) &&
                        cornerOfCardIndexIsVisibleNotCovered(p, cardInPosition(p, (int)oldPoints.getX() + 1, (int)oldPoints.getY() + 1), 2))){

                    toRemoveFromOld.add(oldPoints);
                    //p.remove(oldPoints);
                }
            }

            for(Point2D remove: toRemoveFromOld){
                p.remove(remove);
            }

            for (Point2D poi: pointsToCheck){ // add the new good ones
                p.addAvPos(poi);
            }

        p.removeAvPos(new Point2D.Double(x,y)); // remove itself
    }

    /**
     * Method used for GoldCards and some Objectives.
     * @param pl The player that wants place the card
     * @param x The coordinates x.
     * @param y The coordinates y.
     * @return True if there is a card in position (x,y)
     * present in the player's manuscript.
     */
    private boolean positionOccupied(Player pl, int x, int y) {
        Integer[] positionToFind = {x, y};

        for (Integer[] value : pl.getMap().values()) {
            if (Arrays.equals(value, positionToFind)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param p The player that place the card.
     * @param card The card to checks.
     * @param ind The index of the corners.
     * @return True if player may cover that position because there is no other cards above and the corner is visible.
     */
    private boolean cornerOfCardIndexIsVisibleNotCovered(Player p, Card card, Integer ind) {
        if (card == null) {
            return true;
        } // the card does not exist, no need to control
        //if the card in the corner doesn't exist(null) return true, otherwise return false
        return card.getCorner().get(ind).getVisible() && (!card.getCorner().get(ind).getCovered());
    }

    /**
     * Is used when a player played a card, calculates how many points does the player gains for playing the card
     * @param c The card to checks.
     * @param player The player that place the card.
     * @return The points.
     */

    private int calculatePointsForCard(Card c, Player player) {
        int points = c.getPoints();

        if (c.getPointsMultiply() != null) {
            int multi;

            if (c.getPointsMultiply().equals("CORNER")) {
                multi = howManyCorners(c, player);
            } else {
                multi = player.getNumOfResourceAndObject().get(c.getPointsMultiply());
            }
            points = points * multi;
        }
        return points;
    }

    /**
     * Is used only if the condition of multiplier is CORNER, calculates the number of covered corners by the card.
     * @param c The card.
     * @param p The player that plays the card.
     * @return the number of covered corners.
     */
    private int howManyCorners(Card c, Player p) {
        int numCor = 0;

        Integer[] posizione = p.getMap().get(c);
        if (positionOccupied(p, posizione[0] - 1, posizione[1] - 1)) {
            numCor++;
        }
        if (positionOccupied(p, posizione[0] - 1, posizione[1] + 1)) {
            numCor++;
        }
        if (positionOccupied(p, posizione[0] + 1, posizione[1] - 1)) {
            numCor++;
        }
        if (positionOccupied(p, posizione[0] + 1, posizione[1] + 1)) {
            numCor++;
        }
        return numCor;
    }

    /**
     * Add all the resources and objects present in the card, that was just placed
     * @param card The card plays by the player.
     * @param p The player.
     */

    private void addResourcesOrObjects(Card card, Player p) {
        for (Corner cor : card.getCorner()) {
            if (cor.getVisible()) {
                if (cor.getObject() != null) {
                    p.addResourceOrObject(cor.getObject().toString(), 1);
                }
                if (cor.getResource() != null) {
                    p.addResourceOrObject(cor.getResource().toString(), 1);
                }
            }

            if (card.getSide() == 2) { // if it is the back of a card, then it has permanent resources
                List<Resources> resList = card.getPermanentResource();
                if (resList != null) {
                    for (Resources res : resList) {
                        p.addResourceOrObject(res.name(), 1);
                    }
                }
            }
        }
    }


    /**
     * The method checks the resource or object to removes after the cover of one corner.
     * @param card The card from which I remove the resource or object.
     * @param p The player that place the card.
     * @param x The coordinates x.
     * @param y The coordinates y.
     */

    private void updateCoveredCard(Card card, Player p, int x, int y) {
        int index;
        boolean occupied = positionOccupied(p, x + 1, y - 1);
        if (occupied) {
            System.out.println("trovata carta sotto in "+(x+1)+(y-1));
            index = 0;
            Card cardWanted = cardInPosition(p, x + 1, y - 1);
            System.out.println("cardWanted prima di if "+cardWanted.toString());
            if(cardWanted != null) {
                removeCoveredResourceOrObject(p, cardWanted, index);
                cardWanted.getCorner().get(index).setCovered();
                System.out.println("carta sotto ha angolo coperto con "+ cardWanted.getCorner().get(index).getCovered());
            }
        }
        occupied = positionOccupied(p, x - 1, y - 1);
        if (occupied) {
            index = 1;
            Card cardWanted = cardInPosition(p, x - 1, y - 1);
            if(cardWanted != null) {
                removeCoveredResourceOrObject(p, cardWanted, index);
                cardWanted.getCorner().get(index).setCovered();
            }
        }

        occupied = positionOccupied(p, x + 1, y + 1);
        if (occupied) {
            index = 2;
            Card cardWanted = cardInPosition(p, x + 1, y + 1);
            if(cardWanted != null) {
                removeCoveredResourceOrObject(p, cardWanted, index);
                cardWanted.getCorner().get(index).setCovered();
            }
        }

        occupied = positionOccupied(p, x - 1, y + 1);
        if (occupied) {
            index = 3;
            Card cardWanted = cardInPosition(p, x - 1, y + 1);
            if(cardWanted != null) {
                removeCoveredResourceOrObject(p, cardWanted, index);
                cardWanted.getCorner().get(index).setCovered();
            }
        }
    }

    /**
     * The method removes the resource or object after the cover of corner
     * @param p The player that place card.
     * @param c The card from which I remove the resource or object.
     * @param index The index of the covered corner.
     */


    private void removeCoveredResourceOrObject(Player p, Card c, Integer index) {
        if (c.getCorner().get(index).getObject() != null) {
            p.addResourceOrObject(c.getCorner().get(index).getObject().toString(), -1);
        }
        if (c.getCorner().get(index).getResource() != null) {
            p.addResourceOrObject(c.getCorner().get(index).getResource().toString(), -1);
        }
    }

    /**
     * Method that performs the functionality of drawing from the deck, 1 for gold, 2 for resource.
     * @param pWantsToDraw The player.
     * @param whichDeck Parameter taken from input that tells me which deck the player wants to draw
     */
    public void drawCardFromDeck(Player pWantsToDraw, String whichDeck) {

        if(pWantsToDraw == null){
            return;
        }
        if( (!whichDeck.equalsIgnoreCase("Gold") ) && !whichDeck.equalsIgnoreCase("Resource") ){
            currgame.notify(new ErrorReply(ErrorType.INCORRECT_PARAMETRES_DRAWCARD,pWantsToDraw));
            return;
        }
        boolean thereIsPlayer= currgame.getTotPlayers().stream().anyMatch(player -> player.getNickName().equals(pWantsToDraw.getNickName()));

        if (!thereIsPlayer) {
            return;
        }
        if(!pWantsToDraw.equals(currgame.getCurrPlayer())) {
            currgame.notify(new NotYourTurnReply(pWantsToDraw));
            return;
        }
        if (!(currgame.getGameState().equals(DRAWCARD))) {
            currgame.notify(new ErrorReply(ErrorType.NOT_THE_RIGHT_MOMENT_TO_PLACE_CARD, pWantsToDraw));
            return;
        }
        Card drawCard = null;

        if (currgame.getPlayerCurrentTurn().getHand().size() > 2) {
            currgame.notify(new ErrorReply(ErrorType.PLAYER_HAS_3_CARDS, currgame.getPlayerCurrentTurn()));
        }else {
            if (whichDeck.equals("Resource")) {
                if (currgame.getFacedownResource().isEmpty()) {
                    currgame.notify(new ErrorReply(ErrorType.EMPTY_DECK, currgame.getPlayerCurrentTurn()));
                } else {
                    drawCard = currgame.getFacedownResource().getFirst();
                    currgame.getCurrPlayer().addCardBack(drawCard);
                    currgame.getCurrPlayer().addCard(currgame.flipCard(drawCard));

                    currgame.getFacedownResource().removeFirst();

                    currgame.notify(new DrawCardReply(currgame.flipCard(drawCard),drawCard, currgame.getPlayerCurrentTurn(),
                            currgame.getFacedownGold()==null? null: currgame.getFacedownGold().getFirst(),
                            currgame.getFacedownResource()==null? null: currgame.getFacedownResource().getFirst(),
                            currgame.getFaceupGold()==null? null: currgame.getFaceupGold(),
                            currgame.getFaceupResource()==null? null: currgame.getFaceupResource()));
                    currgame.setGameState(NOT_MY_TURN);
                    nextTurn(); // includes notify
                }
            } else if (whichDeck.equals("Gold")) {
                if (currgame.getFacedownGold().isEmpty()) {
                    currgame.notify(new ErrorReply(ErrorType.EMPTY_DECK, currgame.getPlayerCurrentTurn()));
                } else {
                    drawCard = currgame.getFacedownGold().getFirst();
                    currgame.getCurrPlayer().addCardBack(drawCard);
                    currgame.getCurrPlayer().addCard(currgame.flipCard(drawCard));
                    currgame.getFacedownGold().removeFirst();

                    currgame.notify(new DrawCardReply(currgame.flipCard(drawCard),drawCard, currgame.getPlayerCurrentTurn(),
                            currgame.getFacedownGold()==null? null: currgame.getFacedownGold().getFirst(),
                            currgame.getFacedownResource()==null? null: currgame.getFacedownResource().getFirst(),
                            currgame.getFaceupGold()==null? null: currgame.getFaceupGold(),
                            currgame.getFaceupResource()==null? null: currgame.getFaceupResource()));

                    currgame.setGameState(NOT_MY_TURN);
                    nextTurn(); //includes notify
                }
            }
        }
    }

    /**
     * The method goes to draw a card from the table, refilling it.
     * @param whichCard Specifies which of the two cards the player wants to draw.
     * @param whichType Specifies what type of card the player wants to draw.
     */
    public void drawCardFromTable(Player pWantsToDraw, int whichCard, String whichType) {
        if( (whichCard>1 || whichCard<0) || (!whichType.equalsIgnoreCase("Gold") && !whichType.equalsIgnoreCase("Resource"))){
            currgame.notify(new ErrorReply(ErrorType.INCORRECT_PARAMETRES_DRAWCARD,pWantsToDraw));
            System.out.println("param error:");
            return;
        }
        System.out.println(currgame.getGameState());
        boolean thereIsPlayer= currgame.getTotPlayers().stream().anyMatch(player -> Objects.equals(player.getNickName(), pWantsToDraw.getNickName()));

        if (!thereIsPlayer) {
          //  currgame.notify(new ErrorReply(ErrorType.IGNORE_MESSAGE, pWantsToDraw)); just ignore it
            System.out.println("Ghost says 'booooooo'");
            return;
        }

        Card drawCard = null;
        if(!pWantsToDraw.equals(currgame.getCurrPlayer())) {
            System.out.println("Curr p error:");
            currgame.notify(new NotYourTurnReply(pWantsToDraw));

        }else if (!(currgame.getGameState().equals(DRAWCARD))) {
            System.out.println("not draw card now:");
            currgame.notify(new ErrorReply(ErrorType.NOT_THE_RIGHT_MOMENT_TO_DRAW_CARD, pWantsToDraw));

        }else if (currgame.getPlayerCurrentTurn().getHand().size() > 2) {
            System.out.println(">3 cards in hand:");
            currgame.notify(new ErrorReply(ErrorType.PLAYER_HAS_3_CARDS, currgame.getPlayerCurrentTurn()));

        }else {
            if (whichType.equals("Resource")) {
                if (currgame.getFaceupResource().isEmpty()) {
                    System.out.println("empty:");
                    currgame.notify(new ErrorReply(ErrorType.EMPTY_DECK, currgame.getPlayerCurrentTurn()));
                } else {
                    drawCard = currgame.getFaceupResource().get(whichCard);
                    currgame.getCurrPlayer().addCardBack(currgame.flipCard(drawCard));
                    currgame.getCurrPlayer().addCard(drawCard);
                    currgame.getFaceupResource().remove(whichCard);

                    for (Card c: currgame.getFaceupResource()) {
                        System.out.println("Prima di remove: "+c.toString());
                    }

                    addCardToTable("Resource",whichCard);

                    for (Card c: currgame.getFaceupResource()) {
                        System.out.println("Dopo di remove e add: "+c.toString());
                    }

                    currgame.notify(new DrawCardReply(drawCard,currgame.flipCard(drawCard), currgame.currPlayer,
                            currgame.getFacedownGold()==null? null: currgame.getFacedownGold().getFirst(),
                            currgame.getFacedownResource()==null? null: currgame.getFacedownResource().getFirst(),
                            currgame.getFaceupGold()==null? null: currgame.getFaceupGold(),
                            currgame.getFaceupResource()==null? null: currgame.getFaceupResource()));

                    currgame.setGameState(NOT_MY_TURN); //nobody can do anything
                    nextTurn(); // includes notify turn
                }

            } else if (whichType.equals("Gold")) {
                if (currgame.getFaceupGold().isEmpty()) {
                    System.out.println("empty deck");
                    currgame.notify(new ErrorReply(ErrorType.EMPTY_DECK, currgame.getPlayerCurrentTurn()));
                } else {

                    drawCard = currgame.getFaceupGold().get(whichCard);
                    currgame.getCurrPlayer().addCardBack(currgame.flipCard(drawCard));
                    System.out.println("test .gcs.Carte presenti DENTRO CONTROLLER:");
                    currgame.getCurrPlayer().addCard(drawCard);
                    currgame.getCurrPlayer().getHand().forEach(System.out::println);
                    currgame.getFaceupGold().remove(whichCard);
                    addCardToTable("Gold",whichCard);

                    currgame.notify(new DrawCardReply(drawCard,currgame.flipCard(drawCard), currgame.getPlayerCurrentTurn(),
                            currgame.getFacedownGold()==null? null: currgame.getFacedownGold().getFirst(),
                            currgame.getFacedownResource()==null? null: currgame.getFacedownResource().getFirst(),
                            currgame.getFaceupGold()==null? null: currgame.getFaceupGold(),
                            currgame.getFaceupResource()==null? null: currgame.getFaceupResource()));

                    currgame.setGameState(NOT_MY_TURN); //nobody can do anything
                    nextTurn();
                }
            }
        }
    }

    /**
     * Adds the card from one of the two decks (the one corresponding to the type of card taken) to the common table.
     * @param type The type of card,Resource or Gold.
     * @param index The index of the card 0, 1.
     */
    private void addCardToTable(String type, int index) {
        Card addCard;

        if (type.equals("Resource")) {
            if(currgame.getFacedownResource() != null && !currgame.getFacedownResource().isEmpty()){ // size > 0){
                addCard = currgame.getFacedownResource().getFirst();
                currgame.getFacedownResource().removeFirst();
                currgame.getFaceupResource().add(index, currgame.flipCard(addCard));
            }
        } else {
            if(currgame.getFacedownGold() != null  && !currgame.getFacedownGold().isEmpty()){
                addCard = currgame.getFacedownGold().getFirst();
                currgame.getFacedownGold().removeFirst();
                currgame.getFaceupGold().add(index, currgame.flipCard(addCard));
            }
        }
    }


/****************************** FINAL PHASE OF THE GAME **********************************************************/

    /**
     * Calculates the total number of objectives reached by a player.
     *
     * This method iterates through the following objectives to determine how many have been fulfilled by the player:
     *   - Two common objectives retrieved from the current game (`currgame.getCommonObjective()`)
     *   - The player's individual objective (`player.getObjective()`)
     *
     * For each objective, the `calculatePointsObj` method is called to determine how many times that objective has been reached.
     * The total count is accumulated and returned.
     *
     * @param player the player object containing information about their resources and objects
     * @return the total number of objectives reached by the player
     * @throws NullPointerException if the player object is null
     */
    private int updateFinalPointsObj(Player player) { //returns the num of objectives reached
        int numObjectivesComplete = 0;

        for (Objective comObj : currgame.getCommonObjective()) { // for each of 2 objectives
            numObjectivesComplete += calculatePointsObj(player, comObj);
            System.out.println("test cgs. update final "+numObjectivesComplete + player.getPoints());

        }
        numObjectivesComplete += calculatePointsObj(player, player.getObjective());
        System.out.println("test cgs. update final "+numObjectivesComplete + player.getPoints());
        return numObjectivesComplete;
    }

    /**
     * Calculates the number of objectives reached by a player for a specific objective.
     *
     * This method analyzes the type of the objective (`comObj`) and the player's resources and objects (`player`)
     * to determine how many times the objective has been fulfilled. It then grants points to the player based on
     * the number of objectives reached and the objective's point value.
     *
     * @param player the player object containing information about their resources and objects
     * @param comObj the objective object for which to calculate points
     * @return the number of objectives reached for the given objective
     */

    private int calculatePointsObj(Player player, Objective comObj){ //returns the num of objectives reached per objective
        int numObjectives = 0;
        switch (comObj.getCondition().getTypeOfObjective()) { // let's see the type of the objective
            case ResourceFilling:
                String requiredKeys = comObj.getCondition().getResourcesRequiredToObtainPoints().getFirst().toString();

                int count = player.getNumOfResourceAndObject().get(requiredKeys);

                numObjectives += count / 3;

                // devided by 3 because the objective asks triplets of resources

                player.addPoints(numObjectives * comObj.getPoints());
                break;
            case ObjectFilling:
                if (comObj.getCondition().getQuantityRequiredToObtainPoints().equals(1)) {
                    // requires to find the object with the minimum amount (might be zero) and multiply it by the points

                    numObjectives += player.getNumOfResourceAndObject().entrySet().stream()
                            .filter(entry -> entry.getKey().equals("QUILL") || entry.getKey().equals("INKWELL") || entry.getKey().equals("MANUSCRIPT"))
                            .mapToInt(Map.Entry::getValue)
                            .min().orElse(0);

                    player.addPoints(comObj.getPoints() * numObjectives);
                } else { // triplets of objects

                    String requiredKeys2 = comObj.getCondition().getObjectsRequiredToObtainPoints().getFirst().toString();

                    int count2 = player.getNumOfResourceAndObject().get(requiredKeys2);

                    numObjectives +=  count2 / 3;

                    player.addPoints(numObjectives * comObj.getPoints());
                }
                break;

            case ThreeSameColourPlacing://find all the cards of the given colour in the hash map and mark them as used
                for (Card c : player.getMap().keySet()) { //first, I sign all the cards as not yet used for this objective, since the rules say so
                    c.setCalculatedObjective(false);
                }

                for (Card maybeCard : player.getMap().keySet()) { // I want to find all the cards that might be considered for the objective
                    //enter in "if", if the card's colour corresponds to the searched one, and it was not yet used for evaluation
                    Card tempCard = player.getTimeline().getFirst(); //is always present. even if the player has only one card in the hand, this control does not create issues

                    if ((maybeCard != null) &&!(maybeCard instanceof InitialCard)
                            && maybeCard.getcolour().equals(comObj.getCondition().getColour1())
                            && !maybeCard.isCalculatedObjective()) {
                        switch (comObj.getCondition().getFirstOrientationCornerCheck()) {// see the description of each orientation in Conditions
                            case (1): //north-west to south-east
                                if (maybeCard.getcolour().equals(comObj.getCondition().getColour1())) { //worse to check
                                    Card northWestCard = maybeCard; // I will start analyzing combination from this one
                                    int counter = 0;

                                    Integer[] coordinates = player.getMap().get(northWestCard);
                                    System.out.println(" nW card"+ northWestCard.toString());
                                    //at this point I'd like to find the North-West card in the chain that has the searched colour
                                        while (positionOccupied(player, coordinates[0] -1,coordinates[1]+1 )) {
                                            tempCard = cardInPosition(player, (player.getMap().get(northWestCard)[0]) - 1, (player.getMap().get(northWestCard)[1]) + 1);
                                        if ((tempCard != null) && !(tempCard instanceof InitialCard) && tempCard.getcolour().equals(comObj.getCondition().getColour1())) {
                                            northWestCard = tempCard;
                                                coordinates = player.getMap().get(northWestCard);
                                            } else {
                                                break;
                                            }
                                        }

                                        while (positionOccupied(player, (player.getMap().get(northWestCard)[0]) + 1, (player.getMap().get(northWestCard)[1]) - 1)) {
                                            tempCard = cardInPosition(player, (player.getMap().get(northWestCard)[0]) + 1, (player.getMap().get(northWestCard)[1]) - 1);
                                            if ((tempCard != null) && !(tempCard instanceof InitialCard) && tempCard.getcolour().equals(comObj.getCondition().getColour1())) {
                                                tempCard.setCalculatedObjective(true);
                                            counter++;
                                        } else {
                                            break;
                                        }
                                    }

                                    numObjectives = counter / 3;

                                    player.addPoints(numObjectives * comObj.getPoints());
                                }

                                break;

                            case (2): //south-west to north-east
                                if (maybeCard.getcolour().equals(comObj.getCondition().getColour1())) { //worse to check
                                    Card southEastCard = maybeCard; // I will start analyzing combination from this one
                                    int counter = 0;

                                        Integer[] coordinates = player.getMap().get(southEastCard);
                                        //at this point I'd like to find the North-West card in the chain that has the searched colour
                                        while (positionOccupied(player, coordinates[0] -1,coordinates[1] -1 )) {
                                            tempCard = cardInPosition(player, (player.getMap().get(southEastCard)[0]) - 1, (player.getMap().get(southEastCard)[1]) - 1);
                                            if ((tempCard != null) && !(tempCard instanceof InitialCard) && tempCard.getcolour().equals(comObj.getCondition().getColour1())) {
                                            southEastCard = tempCard;
                                            } else {
                                                break;
                                            }
                                        }

                                        while (positionOccupied(player, (player.getMap().get(southEastCard)[0]) + 1, (player.getMap().get(southEastCard)[1]) + 1)) {
                                            tempCard = cardInPosition(player, (player.getMap().get(southEastCard)[0]) + 1, (player.getMap().get(southEastCard)[1]) + 1);
                                            if ((tempCard != null) && !(tempCard instanceof InitialCard) && tempCard.getcolour().equals(comObj.getCondition().getColour1())) {
                                                tempCard.setCalculatedObjective(true);
                                            counter++;
                                        } else {
                                            break;
                                        }
                                    }

                                    numObjectives = counter / 3;

                                    //add points and sigh that these cards were used for calculating this objective
                                    player.addPoints(numObjectives * comObj.getPoints());
                                }
                                break;
                        }
                    }
                }
                break;
            case TwoSameColourOneDiffersPlacing:
                for (Card c : player.getMap().keySet()) {
                    //first, I sign all the cards as not yet used for this objective, since the rules say so
                    c.setCalculatedObjective(false);
                }

                for (Card maybeCard : player.getMap().keySet()) {
                    // I want to find all the cards that might be considered for the objective
                    //enter in "if", if the card's colour corresponds to the searched one, and it was not yet used for evaluation
                    if (!(maybeCard == null) && !(maybeCard instanceof InitialCard) && maybeCard.getcolour().equals(comObj.getCondition().getColour2()) //different colour
                            && !maybeCard.isCalculatedObjective()) {
                        Integer[] coordinates = player.getMap().get(maybeCard);
                        switch (comObj.getCondition().getFirstOrientationCornerCheck()) { // see the description of each orientation in Conditions
                            case (1):
                                    if(ifTwoColoursArePresent(player, coordinates[0]-1,coordinates[1]+1, "red")){
                                    maybeCard.setCalculatedObjective(true);
                                    numObjectives += 1;
                                    player.addPoints(comObj.getPoints());
                                }
                                break;
                            case 2:
                                if(ifTwoColoursArePresent(player, coordinates[0]+1,coordinates[1]+1, "green")){
                                    maybeCard.setCalculatedObjective(true);
                                    numObjectives += 1;
                                    player.addPoints(comObj.getPoints());
                                }
                                break;
                            case 3:

                                if(ifTwoColoursArePresent(player, coordinates[0]-1,coordinates[1]-1, "blue")){
                                    maybeCard.setCalculatedObjective(true);
                                    numObjectives += 1;
                                    player.addPoints(comObj.getPoints());
                                }
                                break;
                            case 4:
                                if(ifTwoColoursArePresent(player, coordinates[0]+1,coordinates[1]-1, "purple")){
                                    maybeCard.setCalculatedObjective(true);
                                    numObjectives += 1;
                                    player.addPoints(comObj.getPoints());
                                }
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value type object: " + comObj.getCondition().getTypeOfObjective());
                        }
                    }
                }
        }
        return numObjectives;
    }

    /**
     * used to calculate if it is possible to complete the position objective of the condition TwoSameColourOneDiffersPlacing
     * I pass the coordinates where to look for the cards. returns true if in x,y and x,y-2 cards are present
     */
    private boolean ifTwoColoursArePresent(Player player, int x, int y, String colour){
        if(positionOccupied(player,x,y)){ //if it exists
            Card topOfCouple = cardInPosition(player, x, y); //then take it
        if (topOfCouple != null && topOfCouple.getcolour().equals(colour)) {
            Card bottomOfCouple = cardInPosition(player, x, y - 2);
            if (bottomOfCouple != null && topOfCouple.getcolour().equals(colour)) {
                topOfCouple.setCalculatedObjective(true);
                bottomOfCouple.setCalculatedObjective(true);
                return true;
            }
        }
        }

        return false;
    }

    /**
     * Determines the winner(s) of the current game.
     *
     * This method iterates through all players in the game (`currgame`) and calculates their final points using the
     * `updateFinalPointsObj` method. It then finds the maximum points achieved by any player.
     *
     * If there's a single player with the maximum points, they are declared the winner.
     *
     * If multiple players have the same maximum points, the method performs a tie-breaker check. It compares the number
     * of objectives completed by each player using a pre-calculated HashMap (`numObj`) created by `updateFinalPointsObj`.
     * The player(s) with the highest number of completed objectives are declared winners.
     *
     * Finally, the method notifies the game (`currgame`) about the winner(s) using a `WinnerReply` object.
     *
     * @throws RuntimeException if there are no players in the game
     */
    public void getWinner() {
        HashMap<Player, Integer> numObj = new HashMap<Player, Integer>();
        ArrayList<Player> totPlayers = currgame.getTotPlayers();
        for (Player p : totPlayers) {
            numObj.put(p, updateFinalPointsObj(p));
            currgame.notify(new FinalPointsReply(p, p.getPoints()));
        }

        int maxPoints = totPlayers.stream().mapToInt(Player::getPoints).max().orElseThrow(() -> new RuntimeException("No players"));
        ArrayList<Player> winners = totPlayers.stream()
                .filter(player -> player.getPoints() == maxPoints)
                .collect(Collectors.toCollection(ArrayList::new));
        if (winners.size() > 1) {
            int maxObj = numObj.values().stream().max(Integer::compareTo).orElseThrow(() -> new RuntimeException("No players"));
            winners.removeIf(p -> numObj.get(p) < maxObj);
        }
        System.out.println("prima della notify" + winners.toString());
        currgame.notify(new WinnerReply(winners));
        System.out.println("ha gia stampato winner list");
    }

    /**
     * Retrieves a list of all players in the current game.
     *
     * This method retrieves the list of players (`totPlayers`) from the `currgame` object. This list likely
     * contains information about each player, such as their name, score, or other relevant data.
     *
     * @return list of players in the current game
     */

    public List<Player> getPlayersList(){
        return this.currgame.getTotPlayers();
    }


    private void checkLast(){
        if(currgame.getFacedownResource().isEmpty() && currgame.getFacedownGold().isEmpty()){
            endGame=true;
        }else{
            for(int i=0; i<currgame.getTotPlayers().size(); i++){
                if (currgame.getTotPlayers().get(i).getPoints() >= 20) {
                    endGame = true;
                    break;
                }
            }
        }

    }


    /**
     * Advances the game to the next player's turn.
     *
     * This method checks if the current player (`currgame.getCurrPlayer()`) is the last player in the list (`currgame.getTotPlayers().getLast()`).
     *
     * If it's not the last player:
     *   - The game state is set to `PLACECARD`, indicating the next player should place a card.
     *   - The current player (`currgame.getPlayerCurrentTurn()`) is updated to the next player in the `totPlayers` list based on their ID.
     *   - The game (`currgame`) is likely notified about the change.
     *
     * If it is the last player:
     *   - If the `endGame` flag is not set, it signifies a new round.
     *      - The game state is set to `PLACECARD`.
     *      - The current player is set back to the first player in the `totPlayers` list.
     *      - The game is likely notified about the change.
     *   - If the `endGame` flag is set, it signifies the end of the game.
     *      - The `getWinner` method is called to determine and announce the winner(s).
     *
     */

    public void nextTurn(){

            if(currgame.getCurrPlayer().equals(currgame.getTotPlayers().getLast())) {
                if(!endGame){
                    checkLast();
                    currgame.setPlayerCurrentTurn(currgame.getTotPlayers().getFirst()); //notify is inside
                    //currgame.setGameState(PLACECARD);
                }else{
                    getWinner();
                }
            }else{
                currgame.setPlayerCurrentTurn(currgame.getTotPlayers().get(currgame.currPlayer.getId()+1)); //notify is inside
            }
    }

    /**
     * Checks if the current game is full.
     *
     * This method compares the number of current players in the game
     * with the total number of players that the game can accommodate.
     * If the numbers are equal, it means the game is full.
     *
     * @return {@code true} if the number of current players is equal to the total number of players
     *         that the game can accommodate, otherwise {@code false}.
     */
    public boolean isItFull(){
        //if zero, main player still hasn't send the number of players
        if(this.currgame.getMaxPlayersNumber() == 0)
            return true;
        return currgame.getTotPlayers().size() == this.currgame.getMaxPlayersNumber();
    }
    /**
     * Adds a player to the current game if the game is not full.
     *
     * @param p the player to be added to the game
     */
    public void addPlayertoGame(Player p){
        if(!isItFull()){
            currgame.getTotPlayers().add(p);
            if(isItFull()){
                gameflow();
            }
        }
    }

    /**
     * Initializes and starts the game.
     *
     * This method performs the following actions to begin the game:
     *   - Calls `currgame.initialiseGame()` to perform any necessary game setup.
     *   - Sets the game state to `STARTGAME`, indicating the game has begun.
     *
     * @throws NullPointerException (implicitly) if `currgame` is null
     */


    private void gameflow() {
        currgame.setGameState(STARTGAME);
        currgame.initialiseGame();
    }


    /**
     * Checks if the game has ended.
     *
     * This method simply returns the value of the `endGame` flag.
     * @return true if the game has ended, false otherwise
     */
    public boolean isEndGame() {
        return endGame;
    }


    /**
     * Sets the end-of-game flag.
     *
     * This method sets the internal `endGame` flag to the provided boolean value. This flag is used by other methods
     * in the game logic to determine if the game has finished.
     *
     * Setting `endGame` to true likely signifies the end of the current game, while setting it to false indicate
     * a new round or the beginning of the game.
     *
     * @param endGame true if the game has ended, false otherwise
     */
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }
}

