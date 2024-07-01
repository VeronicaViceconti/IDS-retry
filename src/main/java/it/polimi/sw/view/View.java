package it.polimi.sw.view;
import it.polimi.sw.Observer.Observable;
import it.polimi.sw.model.Card;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.model.Objective;
import it.polimi.sw.network.Message.ViewMessage.ResendingNickname;
import it.polimi.sw.network.Message.ViewMessage.SendingNumPlayersAndPion;
import it.polimi.sw.network.Message.serverMessage.ErrorType;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This interface represents the view for displaying game information and interacting with the user.
 */
public abstract class View extends Observable {
    private int numOfPlayers;
    private ArrayList<Card> timeline;
    protected ArrayList<Point2D> availablePositions;
    protected String nickname;

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public View() {
        availablePositions = new ArrayList<>();
        availablePositions.add(new Point2D.Double());
        timeline = new ArrayList<>();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * Shows the player's hand
     * @param hand cards of the player
     */
    abstract public void showPlayerHand(ArrayList<Card> hand);




    /**
     * Shows the player's private manuscript
     * @param map key -> card in the manuscript, value -> coordinates of the card in the manuscript
     */
    abstract public void showPlayerTable(HashMap<Card, Integer[]> map);

    /**
     * Shows the common table
     * @param faceDownGold face down gold card of the game
     * @param faceDownResource  face down resource card of the game
     * @param faceupGold  face up gold cards of the game
     * @param faceupResource  fuR face up resource cards of the game
     */
    abstract public void showCommonTable(Card faceDownGold, Card faceDownResource, ArrayList<Card> faceupGold, ArrayList<Card> faceupResource);
    /**
     * This abstract method likely defines how common objectives (game goals or requirements) should be displayed.
     * Subclasses inheriting from the class containing this method need to implement the specific logic for displaying the objectives.
     *
     * @param obj An ArrayList containing the `Objective` objects representing the common objectives.
     */
    abstract public void showCommonObjectives(ArrayList<Objective> obj);

    /**
     * Shows the available coordinates of where to play the card in the manuscript. array list is saved in tui and gui
     */
    abstract public void showAvailablePositions();


    /**
     * prints the player's objective
     * @param obj
     */
    abstract public void showPrivateObjective(Objective obj);


    //data una lista di messaggi, li visualizza
    //non so se dobbiamo mettere un attributo in TUI che è la lista di GameMessage oppure lo passiamo come param e bona

    /**
     * given a message, if flag is true, then print it, otherwise add to the list
     * @param message
     */
    public abstract void showGameChat(String message);
    /**
     * This abstract method likely defines the initialization process for a game.
     *
     * This method is typically called before the game loop starts. It might involve actions such as:
     *  * Shuffling decks of cards
     *  * Dealing cards to players
     *  * Initializing game board elements
     *  * Setting up player information
     *  * Resetting game variables
     */
    abstract public void initGame();
    abstract public void initializePions(int indexMe,ArrayList<Pion> pions);

    /**
     * message printed at the beginning of the game such as "WELCOME TO THIS FANTASTIC GAME"
      */
    abstract public void showGameStart();


    /**
     * It prints player's nickname, points, num of resources , hand
     */
    abstract public void showPlayerState(Player p);


    //pescaggio generale, al suo interno verrà chiesto in quale mazzo e/o quale carta

    /**
     * Used to allow the user to select a card to draw from one deck
     */
    abstract public void drawCard();

    /**
     * Set the board data of the game, the common table
     *
     * @param fdG face down gold card of the game
     * @param fdR face down resource card of the game
     * @param fuG face up gold cards of the game
     * @param fuR face up resource cards of the game
     * @param commonObj 2 common objectives of the game
     */

    abstract public void setBoardData(Card fdG, Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,ArrayList<Objective> commonObj);
    abstract public void addToTimeLine(Card timeLine);
    /**
     * Used to add the points at the end of the game
     * @param p the player to update
     * @param points the new points of the player
     */
    abstract public void addPoints(Player p, int points);
    /**
     * Show the new player's hand
     * @param c player's hand
     */
    abstract public void addFlippedCard(ArrayList<Card> c);
    /**
     * This method handles the scenario where a chosen nickname is not available.
     * It prompts the user to enter a new nickname until a valid one is provided.
     *
     * @param ok A boolean flag indicating if the nickname was available (false) or not (true).
     */
    public void isNicknameAvailable(boolean ok){
        String nickname;
        Matcher matcher;
        if(!ok){
            do {
                try {
                    System.out.print("\nI'm sorry, the nickname is not available. \nChoose another nickname:");
                    nickname =  reader.readLine();
                    Pattern pattern = Pattern.compile("[A-Za-z]");
                    matcher = pattern.matcher(nickname);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid input!");
                    throw new IllegalArgumentException();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }while (!matcher.find());

            notify(new ResendingNickname(nickname));
        }
    }
    /**
     * Used to show to the user that rn is not his turn!
     */
    abstract public void notYourTurn(); // i doubt that we should use the message, since the view may see the currplayer in local model

    //in GUI lo devo usare perchè è il bottone che in modo asincrono viene cliccato e viene chiamato questo metodo
    //nella tui penso verrà usato selectCardToFlip()
    /**
     *This method is invocated internally by the HandAndObjectivesPanel class after player clicked the flip card button
     *
     */
    abstract public void cardToFlip(String side) throws RemoteException;
    /**
     * The 2 params are the 2 objectives, and client have to choose one for the entire game
     * @param o1 first objective
     * @param o2 second objective
     */
    abstract public void secObjtoChoose(Objective o1, Objective o2) throws RemoteException;
    /**
     * Method invocated when server says it's the player's turn! Used to init the turn
     */
    abstract public void myTurnNotification(HashMap<Card, Integer[]> man, ArrayList<Card> hand,ArrayList<Card> handBack) throws RemoteException;
    /**
     * This abstract method defines how the winner(s) of a game should be announced.
     * Subclasses inheriting from the class containing this method need to implement the specific logic for announcing the winner(s).
     *
     * @param winner A List containing the `Player` objects representing the winners.
     *  * The List might contain a single player for a sole winner or multiple players for a tie.
     */
    abstract public void announceWinner(List<Player> winner);
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
    abstract public void updatePlayerPlayCard(Player p,Card c, Integer x, Integer y,
                              HashMap<String, Integer> numOfResourceAndObject,ArrayList<Card> hand,ArrayList<Card> handBack ,Integer points) throws RemoteException;
    /**
     * This abstract method defines how different types of errors should be handled.
     * Subclasses inheriting from the class containing this method need to implement the specific logic for handling each error type.
     *
     * @param type An `ErrorType` enum value representing the specific error that occurred.
     */
    abstract public void dealWithError(ErrorType type); //switch with all error types

    /**
     * Method used to save the back of the cards for the flip button
     * @param handBack the back of the cards in the hand
     */
    abstract public void savePlayerHandBack(ArrayList<Card> handBack);

    /**
     * This abstract method defines how the client should handle user input for playing a card.
     * @throws RemoteException This exception is thrown if there's an issue communicating with the server during input handling.
     */
    abstract public void readPlayCardInput () throws RemoteException; // excepts only play card and chat
    /**
     * This abstract method defines how the client should handle user input for drawing a card.
     * @throws RemoteException This exception is thrown if there's an issue communicating with the server during input handling.
     */
    abstract public void readDrawCardInput () throws RemoteException; //excepts only draw card and chat
    /**
     * This abstract method likely pauses or disables chat functionality while the user is performing an action in the Text User Interface (TUI).
     * It's used to prevent overwhelming the user with information during actions like selecting a pion or playing a card.
     */
    abstract public void chatWait(); // while in active fase in tui, stop prnting
    /**
     * This abstract method likely resumes or enables chat functionality after an action is completed in the Text User Interface (TUI).
     * It allows the user to chat again after a temporary pause during actions.
     */
    abstract public void chatUnblockWait(); // chat restarts printing and not storing
    /**
     * This method updates the list of available positions (likely for placing game pieces) with the provided `newAvPos` ArrayList.
     * The specific use of these positions will depend on the game logic.
     *
     * @param newAvPos The new ArrayList of available positions to be set.
     */
    public void resetAvPos(ArrayList<Point2D> newAvPos){
        this.availablePositions = newAvPos;
    }
    /**
     * This method handles pion selection logic for the console interface using a `BufferedReader` to read user input.
     * It displays available pions, prompts the user for a selection, validates the input, and sends a `SendingNumPlayersAndPion` message containing the chosen pion.
     *
     * @param availablePions The ArrayList of available pions to display and select from.
     */
    public void selectPion(ArrayList<Pion> availablePions) {
        //che facciamo, lo gestiamo a prescindere come console con scanner? ahahah
        int selection = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("These are the available pions:");
        for (Pion pion:availablePions) {
            System.out.println("-"+pion.toString().replace("_"," "));
        }

        do {
            System.out.println("Write which pion do you want from 1 to "+availablePions.size()+":");
            try {
                selection = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println("Enter a number!");
            }
        } while (selection < 1 || selection > availablePions.size());

        //notify di numOfplayers e pion
        if(numOfPlayers == 0){
            notify(new SendingNumPlayersAndPion(0,availablePions.get(selection-1)));
        }else{
            notify(new SendingNumPlayersAndPion(numOfPlayers,availablePions.get(selection-1)));
        }
    }

    /**
     *
     * @return number of players for the match
     */
    public void selectNumberOfplayerInMatch(){
        numOfPlayers = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


        do {
            System.out.println("Write the number of players you want in the match from 2 to 4:");
            try {
                numOfPlayers = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println("Enter a number!");
            }
        } while (numOfPlayers <= 1 || numOfPlayers > 4);
    }


    abstract public void createTabbedManuscripts(ArrayList<String> names) ; //used in gui to set up the tab panes
    abstract public void gameAlmostFinished();
}
