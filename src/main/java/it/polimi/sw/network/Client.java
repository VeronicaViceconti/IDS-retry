package it.polimi.sw.network;

import it.polimi.sw.Observer.Observer;
import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.ClientModel.Match;
import it.polimi.sw.network.Message.ViewMessage.SendingChatMessage;
import it.polimi.sw.network.Message.serverMessage.*;
import it.polimi.sw.view.View;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This abstract class likely represents the client-side functionality for a game application.
 * It implements Observer, Runnable, and Serializable interfaces.
 *
 * @author (Your Name Here) // Add your name as the author (optional)
 */
public abstract class Client implements Observer, Runnable, Serializable {

    protected View view;
    protected Match match;
    private int idlobby;

    protected QueueHandlerClient queue;
    protected QueueHandlerClient queueChat;

    protected String nickName;

    private Thread chatThread;
    /**
     * Constructor for Client.
     * Takes reference to the View object and the player's nickname.
     * It creates separate QueueHandlerClient objects for server messages and chat messages (if used).
     * It also starts a chat thread (implementation might be hidden).
     *
     * @param view The View object used for UI.
     * @param nickName The player's nickname.
     * @throws RemoteException This exception is thrown if there is a problem creating the QueueHandlerClient objects.
     */
    public Client(View view,String nickName) {
        this.view = view;
        this.nickName = nickName;
        try {
            queue = new QueueHandlerClient();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            queueChat = new QueueHandlerClient();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        startChatThread();

    }

    /**
     * This abstract method likely sends a request to the server to play a card at the specified coordinates.
     *
     * @param card The Card object representing the card to be played.
     * @param coordinates An array of integers representing the coordinates on the game board where the card should be placed.
     * @throws IOException This exception is thrown if there is an error during communication with the server.
     */
    public abstract void placeCard(Card card, Integer[] coordinates) throws IOException;
    /**
     * This method likely sets up the game state for the client.
     * It creates a new Match object using the provided list of players (including the client) and the client's nickname.
     *
     * @param allPlayers An ArrayList of Player objects representing all players in the game (including the client).
     */
    public void startMatch(ArrayList<Player> allPlayers){
        match = new Match(allPlayers, nickName);
    }
    /**
     * This method returns the client's nickname.
     *
     * @return The client's nickname as a String.
     */
    public String getNickName() {
        return nickName;
    }
    /**
     * This method sets the ID of the lobby the client is connected to.
     *
     * @param idlobby An integer representing the lobby ID.
     */
    public void setIdlobby(int idlobby) {
        this.idlobby = idlobby;
    }

    /**
     * This abstract method likely sends the client's nickname to the server again.
     * The implementation for resending the nickname might involve using the queue or another communication channel.
     *
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void resendNickname() throws RemoteException;

    /**
     * This abstract method likely sends a request to the server to draw a card from the specified deck (Gold or Resource).
     *
     * @param whichDeck An integer representing the deck to draw from (1: Gold, 2: Resource).
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void drawCardFromDeck(int whichDeck) throws RemoteException;
    /**
     * This abstract method likely sends a request to the server to draw a face-up card from the specified deck (Gold or Resource) at the indicated position on the table.
     *
     * @param whichDekc An integer representing the deck to draw from (1: Gold, 2: Resource).
     * @param pose An integer representing the position of the card on the table.
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void drawCardFromTable(int whichDekc, int pose) throws RemoteException;
    /**
     * This abstract method likely sends a request to the server to retrieve the client's current points.
     *
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void getMyPoints() throws RemoteException;
    /**
     * This abstract method likely sends the chosen secret objective to the server.
     *
     * @param obj An Objective object representing the chosen secret objective.
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void setMySecretObjective(Objective obj) throws RemoteException;
    /**
     * This method sets the Match object for the client.
     * This might be used for internal purposes to keep track of the game state.
     *
     * @param match A Match object representing the current game state.
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    public int getIdlobby() {
        return idlobby;
    }

    /**
     * This method returns the QueueHandlerClient object used for receiving messages from the server.
     *
     * @return The QueueHandlerClient object for server messages.
     */
    public QueueHandlerClient getQueue() {
        return queue;
    }

    /**
     * This method sends a chat message to the server.
     * It checks if the message is public or private and calls the appropriate method (publicChatMessage or privateChatMessage) for handling.
     *
     * @param mes A SendingChatMessage object containing information about the chat message.
     */
    public void chatMessage(SendingChatMessage mes){
        if(mes.isPublic()){
            publicChatMessage(mes);
        }else{
            privateChatMessage(mes);
        }
    }
    /**
     * This abstract method likely sends a private chat message to the server.
     * The implementation for sending private messages might involve using the queue or another communication channel specific to chat.
     *
     * @param mes A SendingChatMessage object containing information about the private chat message.
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void privateChatMessage(SendingChatMessage mes);

    /**
     * This abstract method likely sends a public chat message to the server.
     * The implementation for sending public messages might involve using the queue or another communication channel designated for public chat.
     *
     * @param mes A SendingChatMessage object containing information about the public chat message.
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void publicChatMessage(SendingChatMessage mes);

    /**
     * This abstract method likely sends a request to the server to retrieve the current state of the game table.
     * The table might contain information about played cards, resources, and other relevant game elements.
     *
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void getMyTable() throws RemoteException;
    /**
     * This abstract method likely sends a request to the server to retrieve the client's current number of resources and objects.
     *
     * @throws RemoteException This exception is thrown if there is a problem with remote communication.
     */
    public abstract void getMyNumOfResourceAndObject() throws RemoteException;

    /**
     * Sends information about the number of players and a specific `Pion` (game piece) to a remote client.
     *
     * @param pion An object of type `Pion`, likely representing a game piece.
     * @param numOfPlayers An integer representing the number of players in the game.
     * @throws RemoteException If there's an error communicating with the remote client.
     */

    public abstract void sendNumPlayersAndPion(Pion pion, int numOfPlayers) throws RemoteException;
    /**
     * Handles disconnection logic, possibly notifying the server or other clients about a player leaving the game.
     *
     * @throws RemoteException If there's an error during the disconnection process.
     */
    public abstract void disconnection() throws RemoteException;
    /**
     * Sends a ping message to a remote client to check if it's still connected.
     *
     * @throws RemoteException If there's an error communicating with the remote client.
     */
    public abstract void pingClient() throws RemoteException;
    /**
     * Returns the view object, likely representing the visual elements of the game for this specific client.
     *
     * @return The view object.
     */
    public View getView() {
        return view;
    }

    /**
     * Returns the `Match` object, which likely holds information about the current game state.
     *
     * @return The `Match` object.
     */
    public Match getMatch() {
        return match;
    }
    /**
     * Overrides the `run()` method from the `Runnable` interface. This method seems to be the main loop of a thread
     * that continuously processes messages received from a server queue.
     */
    @Override
    public void run(){
        while (true) {
            SampleServerMessage messageToElaborate = queue.getNextMessage();
            if(messageToElaborate != null){
                try{
                    messageToElaborate.execute(this);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } catch ( NumberFormatException | IOException e){
                    e.printStackTrace();
                    System.err.println("Error processing message:" + e);
                }
            }
        }
    }




    /**
     * This method handles incoming `SampleServerMessage` objects
     * and sorts them based on their type. Chat messages are added to a separate chat queue, while other messages are
     * added to the main queue.
     *
     * @param message The received `SampleServerMessage` object.
     * @throws RemoteException If there's an error during remote communication.
     */

    @Override
    public void update(SampleServerMessage message) throws RemoteException {
        if(message.getType().equals(TypeMessageServer.CHAT_REPLY)){
            queueChat.appendMessage(message);
        }else{
            queue.appendMessage(message);
        }
    }
    /**
     * This method likely retrieves information about other players in the current game from the `Match` object and
     * updates the view with their state and map information.
     */
    @Override
    public void requestData() {
        for(Player player: getMatch().getTotPlayers()){
            if(!player.getNickName().equals(nickName)){
                view.showPlayerState(player);
                view.showPlayerTable(player.getMap());
            }
        }
    }
    /**
     * This method starts a separate thread dedicated to processing messages from the chat queue. It continuously checks
     * for new messages, executes them, and sleeps for a short period before checking
     * again.
     */
    public void startChatThread(){
        chatThread = new Thread(() -> {

            while (true) {
                try {

                    while (!queueChat.isEmpty()) {
                        SampleServerMessage userMessage = queueChat.getNextMessage();
                        userMessage.execute(this);
                    }



                    Thread.sleep(500);
                } catch (InterruptedException | RemoteException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        chatThread.start();
    }


}