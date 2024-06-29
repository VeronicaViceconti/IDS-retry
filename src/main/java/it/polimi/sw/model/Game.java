package it.polimi.sw.model;

import it.polimi.sw.Observer.Observable;
import it.polimi.sw.network.Message.serverMessage.*;

import java.util.*;

public class Game extends Observable {
    /**
     * Unique id of the game.
     */
    private final int id;
    /**
     * Players.
     */
    private int totPlayers;
    private ArrayList<Player> playersList;
    public Player currPlayer;
    /**
     * Decks of cards: face up are visible to everyone. face down are not, need to be initialised in the beginning of each game.
     */
    private ArrayList<Card> faceupGold;
    private ArrayList<Card> faceupResource;
    private ArrayList<Card> facedownGold;
    private ArrayList<Card> facedownResource;
    private ArrayList<Card> facedownInitial;
    private ArrayList<Card> faceupInitial;

    /** Sides of the cards*/
    private HashMap<Card, Card> sidesGoldCard; // key is the faceup and value is the facedown
    private HashMap<Card, Card> sidesResCard; // key is the faceup and value is the facedown
    private HashMap<Card, Card> sidesInitCard; // key is the faceup and value is the facedown
    /**
     * objectives. allObjectives need to be initialised in the beginning of each game.
     */
    private ArrayList<Objective> allObjective;
    private ArrayList<Objective> commonObjective;
    /**
     * The phase of the game.
     */
    private GameState gameState;

    /**
     * Retrieves the face-down gold cards.
     *
     * @return The face-down gold cards
     */
    public ArrayList<Card> getFacedownGold() {
        return facedownGold;
    }
    /**
     * Retrieves the face-up gold cards.
     *
     * @return The face-upgold cards
     */
    public ArrayList<Card> getFaceupGold() {
        return faceupGold;
    }
    /**
     * Retrieves the face-up resource cards.
     *
     * @return The face-up resource cards
     */
    public ArrayList<Card> getFaceupResource() {
        return faceupResource;
    }
    /**
     * Sets the face-down gold cards.
     *
     * @return The face-down gold cards
     */
    public void setFacedownGold(ArrayList<Card> facedownGold) {
        this.facedownGold = facedownGold;
    }
    /**
     * Retrieves the face-down resource cards.
     *
     * @return The face-down resource cards
     */
    public ArrayList<Card> getFacedownResource() {
        return facedownResource;
    }
    /**
     * Sets the face-down resource cards.
     */
    public void setFacedownResource(ArrayList<Card> facedownResource) {
        this.facedownResource = facedownResource;
    }


    /**
     * Retrieves a copy of the list of all players in the game.
     *
     * @return An unmodifiable `ArrayList` containing all the players in the game.
     */
    public ArrayList<Player> getTotPlayers() {
        return playersList;
    }


/*
    public int getPointsCurrPlayer() {
        return currPlayer.getPoints();
    }
*/
    /**
     * Retrieves the game unique identifier.
     *
     * @return The game ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves a copy of the list containing all possible objectives in the game.
     *
     * @return An unmodifiable `ArrayList` containing all Objective instances available in the game.
     */
    public ArrayList<Objective> getAllObjective() {
        return allObjective;
    }
    /**
     * Sets the list of all possible objectives in the game.
     *
     * @param allObjective An `ArrayList` containing all Objective instances available in the game.
     */
    public void setAllObjective(ArrayList<Objective> allObjective) {
        this.allObjective = allObjective;
    }
    /**
     * Constructor for the Game class.
     *
     * @param id Unique identifier for the game.
     * @param totPlayers Total number of players in the game.
     * @param playersList An `ArrayList` containing information about all players.
     * @param resource A `HashMap` mapping resource card types (Card) to their corresponding card objects (Card).
     * @param gold A `HashMap` mapping gold card types (Card) to their corresponding card objects (Card).
     * @param initial A `HashMap` mapping initial card types (Card) to their corresponding card objects (Card).
     * @param allObjective An `ArrayList` containing all possible Objective instances available in the game.
     */
    public Game(int id, int totPlayers, ArrayList<Player> playersList, HashMap<Card,Card> resource,
                HashMap<Card,Card> gold, HashMap<Card,Card> initial, ArrayList<Objective> allObjective) {
        this.facedownResource = new ArrayList<>();
        this.facedownInitial = new ArrayList<>();
        this.facedownGold = new ArrayList<>();
        this.commonObjective = new ArrayList<>();
        this.id = id;
        this.totPlayers = totPlayers;
        this.playersList = new ArrayList<>(playersList);
        this.allObjective = new ArrayList<>(allObjective);
        this.gameState = GameState.STARTGAME;
        this.faceupResource = new ArrayList<>();
        this.faceupGold = new ArrayList<>();
        this.faceupInitial = new ArrayList<>();
        sidesGoldCard = new HashMap<>(gold);
        sidesResCard = new HashMap<>(resource);
        sidesInitCard = new HashMap<>(initial);
        initialiseGame();
    }

    /**
     * Constructor for the Game class with simplified parameters.
     *
     * @param id Unique identifier for the game (might be assigned later).
     * @param numPlayers Total number of players in the game.
     * @param playersList An `ArrayList` containing information about all players.
     */
    public Game(int id, int numPlayers, ArrayList<Player> playersList){
        this.id = 0;
        this.totPlayers = numPlayers;
        this.playersList = new ArrayList<>(playersList);
    }

    /**
     * Constructor for the Game class focusing on card configuration.
     *
     * @param id Unique identifier for the game.
     * @param resource A `HashMap` mapping resource card types (Card) to their corresponding card objects (Card).
     * @param gold A `HashMap` mapping gold card types (Card) to their corresponding card objects (Card).
     * @param initial A `HashMap` mapping initial card types (Card) to their corresponding card objects (Card).
     * @param allObjective An `ArrayList` containing all possible Objective instances available in the game.
     */

    public Game(int id, HashMap<Card,Card> resource,
                HashMap<Card,Card> gold, HashMap<Card,Card> initial, ArrayList<Objective> allObjective){ //usiamo questo
        this.facedownResource = new ArrayList<>();
        this.facedownInitial = new ArrayList<>();
        this.facedownGold = new ArrayList<>();
        this.commonObjective = new ArrayList<>();
        this.id = id;
        this.playersList = new ArrayList<>();
        this.allObjective = new ArrayList<>(allObjective);
        this.faceupResource = new ArrayList<>();
        this.faceupGold = new ArrayList<>();
        this.faceupInitial = new ArrayList<>();
        sidesGoldCard = new HashMap<>(gold);
        sidesResCard = new HashMap<>(resource);
        sidesInitCard = new HashMap<>(initial);
    }

    /**
     ******************************** 1st phase of the game:
     * initialise game that includes following methods
     * build decks
     * shuffle all the decks
     * distribute cards
     * shuffle objectives
     * pick up common Objectives
     * set the first player
     * set faceupcards
     */
    public void initialiseGame() { //second step in rulebook
        initialiseDecks(facedownResource,sidesResCard);
        initialiseDecks(facedownInitial,sidesInitCard);
        initialiseDecks(facedownGold,sidesGoldCard);
        shuffleCards(facedownGold);
        shuffleCards(facedownInitial);
        shuffleCards(facedownResource); //fino qua ok
        shuffleObjective(allObjective);
        pickupCommonObjectives();
        //setting the two elements of the two deck visible to everyone!
        for (int i=0;i<=1;i++) {
            faceupGold.add(flipCard(facedownGold.getFirst()));
            facedownGold.removeFirst();
        }
        for (int i=0;i<=1;i++) {
            faceupResource.add(flipCard(facedownResource.getFirst()));
            facedownResource.removeFirst();
        }
        ArrayList<Pion> pions = new ArrayList<>();
        for (Player p:getTotPlayers()) {
            pions.add(p.getPion());
        }
        notify(new BoardDataReply(facedownGold.getFirst(), facedownResource.getFirst(), faceupGold, faceupResource,commonObjective, playersList,currPlayer,pions));
        this.currPlayer = playersList.getFirst();//fino qua ok
        distributeInitialCards(); //notify inside
    }
    /**
     * Initializes a deck of cards from a HashMap correlation. (likely private method)
     *
     * @param deck An `ArrayList` that will hold the cards for the deck.
     * @param deckCorrelation A `HashMap` that maps card types (Card) to their corresponding card objects (Card) used for deck population.
     */
    private void initialiseDecks(ArrayList<Card> deck,HashMap<Card,Card> deckCorrelation){

        //each retro saved into facedown arraylist
        deck.addAll(deckCorrelation.values());
    }

/*    public void setPlayersList(ArrayList<Player> playersList) {
        this.playersList = playersList;
    }*/
    /**
     * Selects two random common objectives from the list of all objectives.
     *
     */

    private void pickupCommonObjectives() {
        Collections.shuffle(allObjective);

        for (int i=0;i<=1;i++) {
            commonObjective.add(allObjective.getFirst());
            allObjective.removeFirst();
        }
    }
    /**
     * Retrieves two random secret objectives for a player.
     *
     * @return An `ArrayList` containing two secret Objective instances,
     *         or null if there are not enough objectives remaining.
     */
    public ArrayList<Objective> pickupSecretObjective() {

            ArrayList<Objective> objectives = new ArrayList<>();
            objectives.add(allObjective.getFirst());
            allObjective.removeFirst();
            objectives.add(allObjective.getFirst());
            allObjective.removeFirst();

            return objectives;

    }
    /**
     * Selects a random player to be the first player.
     *
     */
    private void randomFirstPlayer() {
        // Create Random object
        Random random = new Random();
        this.currPlayer = playersList.get( random.nextInt(totPlayers) );
    }

    /**
     * Shuffles the list of cards.
     *
     * @param listCard The list of cards to be shuffled. This list can contain instances of
     *                 {@link Card} or its subclasses.
     * @throws NullPointerException if listCard is null.
     */
    private void shuffleCards(ArrayList<Card> listCard) {//ArrayList<Card> listCard
        Collections.shuffle(listCard);
    }

    /**
     * Distributes initial cards to each player.
     * <p>
     * This method distributes the initial cards from the facedownInitial list to each player
     * in the playersList. Each player receives one initial card.
     * <p>
     * After distributing the cards, the distributed cards are removed from the facedownInitial list.
     */
    private void distributeInitialCards() {
//fino qua ok
        for (int i=0;i<6;i++) {
            faceupInitial.add(flipCard(facedownInitial.get(i)));
        }
        for (Player p : playersList) {//fino qua ok
            p.addCard(faceupInitial.get(0));//ok
            p.addCardBack(facedownInitial.get(0)); //ordine fdI and fuI is the same. id+6
            System.out.println("Carta fronte nella mano: "+p.getHand().get(0));
            System.out.println("Carta back nella mano: "+p.getHandBack().get(0));
            notify(new SendInitialCard(p.getHand().get(0),p.getHandBack().get(0), p,p.getId()));

            facedownInitial.remove(flipCard(faceupInitial.get(0)));
            faceupInitial.remove(0);
        }
    }

    /**
     * Shuffles the list of Objective.
     *
     * @param listOb The list of Objective to be shuffled. This list can contain instances of
     *               {@link Objective}.
     * @throws NullPointerException if listCard is null.
     */
    private void shuffleObjective(ArrayList<Objective> listOb) {
        Collections.shuffle(listOb);
    }

    /**
     * Retrieves the player whose turn it is currently.
     *
     * @return The `Player` object representing the current player.
     */

    public Player getCurrPlayer() {
        return currPlayer;
    }
    /**
     * Retrieves a copy of the list containing the common objectives for all players.
     *
     * @return An unmodifiable `List` containing the common Objective instances.
     *         Changes to the returned list will not affect the original list of common objectives.
     */
    public List<Objective> getCommonObjective() {

        return commonObjective;
    }
    /**
     * Sets the list of common objectives for all players.
     *
     * @param commonObjective An `ArrayList` containing the common Objective instances.
     */
    public void setCommonObjective(ArrayList<Objective> commonObjective) {
        this.commonObjective = commonObjective;
    }

    /**
     * flips a given card. is used to reveal a card after drawing one.
     * @param card
     * @return the card flipped
     */
    public Card flipCard(Card card) {
        //tolgo package
        String s = card.getClass().getName().substring(19);

        switch (s) {
            case "InitialCard":
                if (card.getSide() == 1) { // face up, I want face down
                    return sidesInitCard.containsKey(card) ? sidesInitCard.get(card) : null;
                }
                else { // face down, I want face up
                    if(sidesInitCard.containsValue(card)){
                        //finding the faceUp from the keys
                        for (Card cardWanted : sidesInitCard.keySet()) {
                            if (card.equals(sidesInitCard.get(cardWanted))) {
                                return cardWanted;
                            }
                        }
                    }
                }
                break;
            case "GoldCard":
                if (card.getSide() == 1) { // face up
                    return sidesGoldCard.containsKey(card) ? sidesGoldCard.get(card) : null;
                } else { // face down
                    if(sidesGoldCard.containsValue(card)){
                        for (Card cardWanted : sidesGoldCard.keySet()) {
                            if (card.equals(sidesGoldCard.get(cardWanted))) {
                                return cardWanted;
                            }
                        }
                    }
                }
                break;
            case "ResourceCard":
                if (card.getSide() == 1) { // face up
                    return sidesResCard.containsKey(card) ? sidesResCard.get(card) : null;
                } else { // face down
                    if(sidesResCard.containsValue(card)){
                        for (Card cardWanted : sidesResCard.keySet()) {
                            if (card.equals(sidesResCard.get(cardWanted))) {
                                return cardWanted;
                            }
                        }
                    }
                }
                break;
            default:
                System.out.println("None");
                return null;
        }
        return null;
    }


    /**
     * ******************************game flow**********************************
     *
     */

    /**
     * Retrieves the player whose turn it is currently.
     *
     * @return The `Player` object representing the current player.
     */

    public Player getPlayerCurrentTurn() {
        return this.currPlayer;
    }
    /**
     * Sets the current player and notifies other players about the turn change.
     *
     * @param playerX The `Player` object representing the player whose turn it is now.
     *                 The controller likely verifies if this player is connected before passing it.
     */

    public void setPlayerCurrentTurn(Player playerX) {
        //controller passa un giocatore playerX dopo il controllo che quello sia connesso
        this.currPlayer = playerX;
        notify(new TurnReply(playerX, playerX.getAvailablePositions()));
    }


    /**
     *
     * @param which 1/2 indicates the number of card chosen
     */
    public void drawFaceUpCard(String deckType, int which) {
        if(!(which == 0 || which ==1) )
            throw new ArrayStoreException();
        else{
            if (Objects.equals(deckType, "Gold")) {
                if (faceupGold.isEmpty()) {
                    throw new ArrayStoreException();
                }

                currPlayer.addCard(faceupGold.get(which));
                currPlayer.addCardBack(flipCard(faceupGold.get(which)));
                faceupGold.remove(which);
                faceupGold.add(flipCard(facedownGold.getFirst()));
                facedownGold.removeFirst();
            } else if (Objects.equals(deckType, "Resource")) {
                if (faceupResource.isEmpty()) {
                    throw new ArrayStoreException();
                }
                currPlayer.addCard(faceupResource.get(which));
                currPlayer.addCardBack(flipCard(faceupResource.get(which)));
                faceupResource.remove(which);
                faceupResource.add(facedownResource.getFirst());
                facedownResource.removeFirst();
            }else throw new ArrayStoreException();
        }
    }

    /**
     * @param deckType from which deck does client want to draw a card? Possible options: Gold and Resource
     */
    public void drawFaceDownCard(String deckType) {
        if (Objects.equals(deckType, "Gold")) {
            if (facedownGold.isEmpty()) {
                return;
            }
            currPlayer.addCard(flipCard(facedownGold.get(0)));
            currPlayer.addCardBack(facedownGold.get(0));
            facedownGold.remove(0);
        }
        if (Objects.equals(deckType, "Resource")) {
            if (facedownResource.isEmpty()) {
                return;
            }
            currPlayer.addCard(flipCard(facedownResource.get(0)));
            currPlayer.addCardBack(facedownResource.get(0));
            facedownResource.remove(0);
        }
    }
    /**
     * Retrieves the current game state.
     *
     * @return The current `GameState` of the game.
     */
    public GameState getGameState() {
        return gameState;
    }
    /**
     * Sets the game state and potentially notifies the current player.
     *
     * @param gameState The new `GameState` for the game.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        //notifyOne(gameState, currPlayer);
    }
    /**
     * Retrieves the maximum number of players allowed in the game.
     *
     * @return The maximum number of players.
     */
    public int getMaxPlayersNumber(){
            return totPlayers;
    }

    /**
     * Sets the maximum number of players allowed in the game (likely for internal use).
     *
     * @param totPlayers The maximum number of players.
     */
    public void setMaxPlayersNumbers(int totPlayers) {
        this.totPlayers = totPlayers;
    }

}
