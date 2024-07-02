package it.polimi.sw.network.RMI;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Message.ClientMessage.DisconnectionRequestGameEnding;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * This interface defines methods for a client-side object to interact with the server remotely.
 */
public interface InterfaceClientHandlerRMI extends Remote {
    /**
     * A test method that might be used for debugging purposes.
     *
     * @return A String value (likely for testing purposes).
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    String test() throws RemoteException;
    /**
     * This method attempts to connect the client to the server with the provided nickname.
     *
     * @param nickname The chosen nickname for the client.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void connectToServer(String nickname) throws RemoteException;
    /**
     * This method checks if a nickname is already in use by another client.
     *
     * @param nickname The nickname to check for duplicates.
     * @return true if the nickname is available, false otherwise.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the RMI registry.
     */
    boolean duplicateHandler(String nickname) throws RemoteException;
    /**
     * This method sends a request to the server to place a card in a lobby.
     *
     * @param lobby The lobby where the card is to be placed.
     * @param card The card object to be placed.
     * @param x The x-coordinate for card placement.
     * @param y The y-coordinate for card placement.
     * @param id_player The ID of the player placing the card.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void placeCard(int lobby,Card card, int x, int y, int id_player) throws RemoteException;
    /**
     * This method sends a request to the server to draw a card from the deck in a lobby.
     *
     * @param lobby The lobby where the card is drawn from.
     * @param whichDeck The name of the deck to draw from (e.g., "mainDeck").
     * @param id_player The ID of the player drawing the card.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void drawCardFromDeck(int lobby,String whichDeck, int id_player) throws RemoteException;
    /**
     * This method sends a request to the server to draw a card from the table in a lobby.
     *
     * @param lobby The lobby where the card is drawn from.
     * @param whichDeck The name of the deck to draw from (e.g., "discardPile").
     * @param whichCard The index of the specific card to draw from the chosen deck (optional).
     * @param id_player The ID of the player drawing the card.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void drawCardFromTable(int lobby,String whichDeck, int whichCard, int id_player) throws RemoteException;
    /**
     * This method sends a request to the server to get the player's points in a lobby.
     *
     * @param lobby The lobby where the points are requested.
     * @param id_player The ID of the player requesting their points.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void getMyPoints (int lobby, int id_player) throws RemoteException;
    /**
     * This method sends a request to the server to set the player's secret objective in a lobby.
     *
     * @param lobby The lobby where the objective is set.
     * @param id_player The ID of the player setting the objective.
     * @param obj The Objective object representing the player's secret objective.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void setMySecretObjective (int lobby, int id_player, Objective obj)throws RemoteException;
    /**
     * This method sends a private chat message to another player in a lobby.
     *
     * @param lobby The lobby where the chat message is sent.
     * @param receiver The nickname of the player to receive the message.
     * @param message The content of the private chat message.
     * @param id_player The ID of the player sending the message.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void privateChatMessage(int lobby,String receiver, String message,int id_player) throws RemoteException;
    /**
     * This method sends a public chat message to all players in a lobby.
     *
     * @param lobby The lobby where the chat message is sent.
     * @param message The content of the public chat message.
     * @param id_player The ID of the player sending the message.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void publicChatMessage(int lobby,String message, int id_player) throws RemoteException;

    /**
     * This method sends information about the player (nickname, chosen pawn, number of players) to the server upon joining a lobby.
     *
     * @param lobby The lobby being joined.
     * @param nickname The player's nickname.
     * @param pion The Pawn (game piece) chosen by the player.
     * @param numOfPlayers The total number of players in the lobby.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void sendNumPlayersAndPion(int lobby, String nickname,Pion pion, int numOfPlayers) throws RemoteException;
    /**
     * This method sends a request to the server to get the player's game board data in a lobby.
     *
     * @param lobby The lobby where the game board data is requested.
     * @param id_player The ID of the player requesting their game board data.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void getMyTable(int lobby, int id_player) throws RemoteException;
    /**
     * This method sends a request to the server to get the player's resource and object counts in a lobby.
     *
     * @param lobby The lobby where the resource and object counts are requested.
     * @param id_player The ID of the player requesting their resource and object counts.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void getMyNumOfResourceAndObject(int lobby, int id_player) throws RemoteException;
    /**
     * This method sends a disconnection request to the server for the player in a lobby.
     *
     * @param lobby The lobby where the player is disconnecting.
     * @param id_player The ID of the player disconnecting.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void disconnection(int lobby,int id_player) throws RemoteException;
    /**
     * This method sends a ping request to the server to check the client's connection status in a lobby.
     *
     * @param lobby The lobby where the ping is sent.
     * @param id_player The ID of the player sending the ping.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void pingClient(int lobby, int id_player) throws RemoteException;
    /**
     * This method might be intended to retrieve information about the player associated with this ClientHandlerRMI object.
     * (Override from Object class)
     *
     * @return However, the provided implementation always returns null.
     */
    Player getPlayer() throws RemoteException;
    /**
     * This method registers a remote queue interface (likely on the server-side) and the client name with the server.
     *
     * @param stub A reference to the remote queue interface object on the server.
     * @param client1 The client's name.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void register(RemoteQueueInterface stub, String client1) throws RemoteException;
    /**
     * This method sends a request to the server to resend the client's nickname.
     *
     * @param nickName The client's nickname to be resent.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    void resendNickname(String nickName) throws RemoteException;

    public void sendDisconnectionGameEnding(int lobby) throws RemoteException;
}
