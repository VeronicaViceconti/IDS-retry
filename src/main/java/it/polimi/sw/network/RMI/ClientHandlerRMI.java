package it.polimi.sw.network.RMI;

import it.polimi.sw.Observer.Observer;
import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Message.ClientMessage.*;
import it.polimi.sw.network.Message.ViewMessage.SampleViewMessage;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;
import it.polimi.sw.network.QueueHandlerServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

/**
 * This class implements the `InterfaceClientHandlerRMI` interface and the `Observer` interface.
 * It acts as a remote object that handles communication between a client and the server using RMI (Remote Method Invocation).
 *
 */
public class ClientHandlerRMI extends UnicastRemoteObject implements InterfaceClientHandlerRMI, Observer {
    /**
     * A reference to the `QueueHandler` object on the server-side.
     * This object is likely responsible for managing the message queue.
     */
    private QueueHandlerServer queueServer;
    /**
     * The name of the client associated with this `ClientHandlerRMI` object.
     */
    private String  clientName;
    /**
     * A reference to the remote queue interface associated with the client.
     */
    private RemoteQueueInterface clientQueue;

    /**
     * Constructor for the `ClientHandlerRMI` class.
     *
     * @param queue A reference to the `QueueHandler` object on the server-side.
     * @throws RemoteException This exception is thrown if there is a problem exporting the object remotely.
     */
    public ClientHandlerRMI(QueueHandlerServer queue) throws RemoteException {
        this.queueServer = queue;
    }

    /**
     * This method attempts to connect the client to the server with the provided nickname.
     *
     * @param nickname The chosen nickname for the client.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void connectToServer(String nickname) throws RemoteException {
        System.out.println("Sto connettendo con server "+nickname+" con "+this.clientName);
        queueServer.appendMessage(new ConnectionRequest(nickname,this));

        //System.out.println("HANDLER - riempio la queue con sendinitialcard");
        //clients.get("Maria").appendMessage(new SendInitialCard(null,null,null));
    }
    /**
     * This method checks if a nickname is already in use by another client.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param nickname The nickname to check for duplicates.
     * @return true if the nickname is available, false otherwise.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the RMI registry.
     */
    @Override
    public boolean duplicateHandler(String nickname) throws RemoteException{

        InterfaceClientHandlerRMI clientHandlerRMI = null;
        try {
            clientHandlerRMI = new ClientHandlerRMI(QueueHandlerServer.getInstance());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("localhost",1700);
            for (String name : registry.list()){
                if(name.equals("ClientHandlerRMI_"+nickname)){
                    return false;
                }
            }
            registry.bind("ClientHandlerRMI_"+nickname, clientHandlerRMI);
            return true;
        } catch (RemoteException |AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * This method sends a request to the server to place a card in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the card is to be placed.
     * @param card The card object to be placed.
     * @param x The x-coordinate for card placement.
     * @param y The y-coordinate for card placement.
     * @param id_player The ID of the player placing the card.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void placeCard(int lobby,Card card, int x, int y,int id_player) throws RemoteException {
        queueServer.appendMessage(new PlaceCardRequest(lobby,card,x,y,id_player));
    }
    /**
     * This method sends a request to the server to draw a card from the deck in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the card is drawn from.
     * @param whichDeck The name of the deck to draw from (e.g., "mainDeck").
     * @param id_player The ID of the player drawing the card.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void drawCardFromDeck(int lobby,String whichDeck, int id_player) throws RemoteException {
        queueServer.appendMessage(new DrawCardRequest(lobby,whichDeck, id_player));
    }
    /**
     * This method sends a request to the server to draw a card from the table in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the card is drawn from.
     * @param whichDeck The name of the deck to draw from (e.g., "discardPile").
     * @param whichCard The index of the specific card to draw from the chosen deck (optional).
     * @param id_player The ID of the player drawing the card.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void drawCardFromTable(int lobby,String whichDeck, int whichCard,int id_player) throws RemoteException {
        queueServer.appendMessage(new DrawCardRequest(lobby,whichDeck,whichCard, id_player));
    }
    /**
     * This method sends a request to the server to get the player's points in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the points are requested.
     * @param id_player The ID of the player requesting their points.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void getMyPoints(int lobby, int id_player) throws RemoteException {
        queueServer.appendMessage(new MyPointsRequest(lobby,id_player));
    }
    /**
     * This method sends a request to the server to set the player's secret objective in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the objective is set.
     * @param id_player The ID of the player setting the objective.
     * @param obj The Objective object representing the player's secret objective.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void setMySecretObjective(int lobby, int id_player, Objective obj) throws RemoteException {
        queueServer.appendMessage(new SendPrivateObjective(lobby, id_player,obj));
    }

    /**
     * This method sends a private chat message to another player in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the chat message is sent.
     * @param receiver The nickname of the player to receive the message.
     * @param message The content of the private chat message.
     * @param id_player The ID of the player sending the message.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void privateChatMessage(int lobby,String receiver, String message,int id_player) throws RemoteException {
        queueServer.appendMessage(new PrivateChatRequest(lobby,receiver,message,id_player));
    }
    /**
     * This method sends a public chat message to all players in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     *  * @param lobby The lobby where the chat message is sent.
     * @param message The content of the public chat message.
     * @param id_player The ID of the player sending the message.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void publicChatMessage(int lobby,String message,int id_player) throws RemoteException {
        queueServer.appendMessage(new PublicChatRequest(lobby,message,id_player));
    }
    /**
     * This method sends information about the player (nickname, chosen pawn, number of players) to the server upon joining a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby being joined.
     * @param nickname The player's nickname.
     * @param pion The Pawn (game piece) chosen by the player.
     * @param numOfPlayers The total number of players in the lobby.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void sendNumPlayersAndPion(int lobby, String nickname,Pion pion, int numOfPlayers) {
        queueServer.appendMessage(new PlayerNumberAndPionRequest(lobby,nickname,numOfPlayers,pion));
    }

    /**
     * This method sends a request to the server to get the player's game board data in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the game board data is requested.
     * @param id_player The ID of the player requesting their game board data.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void getMyTable(int lobby, int id_player)throws RemoteException  {
        queueServer.appendMessage(new BoardDataRequest(lobby,id_player));
    }
    /**
     * This method sends a request to the server to get the player's resource and object counts in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the resource and object counts are requested.
     * @param id_player The ID of the player requesting their resource and object counts.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void getMyNumOfResourceAndObject(int lobby,int id_player)throws RemoteException  {
        queueServer.appendMessage(new NumOfResourceRequest(lobby,id_player));
    }

    /**
     * This method sends a disconnection request to the server for the player in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the player is disconnecting.
     * @param id_player The ID of the player disconnecting.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void disconnection(int lobby,int id_player) throws RemoteException {
        queueServer.appendMessage(new DisconnectionRequest(lobby,id_player));
    }
    /**
     * This method sends a ping request to the server to check the client's connection status in a lobby.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param lobby The lobby where the ping is sent.
     * @param id_player The ID of the player sending the ping.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void pingClient(int lobby,int id_player)throws RemoteException  {
        queueServer.appendMessage(new PingClient(lobby,id_player));
    }
    /**
     * This method might be intended to retrieve information about the player associated with this ClientHandlerRMI object.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @return However, the provided implementation always returns null.
     */
    @Override
    public Player getPlayer() {
        return null;
    }

    /**
     * Used to pass a queue reference from the client, to the server in order to allow the server to use the same queue of the client
     * @param stub
     * @param clientName
     */
    @Override
    public void register(RemoteQueueInterface stub, String clientName) {
        this.clientName = clientName;
        clientQueue = stub;
        System.out.println("Client registered: " + clientName);
    }
    /**
     * This method sends a request to the server to resend the client's nickname.
     * (Override from InterfaceClientHandlerRMI)
     *
     * @param nickName The client's nickname to be resent.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void resendNickname(String nickName) throws RemoteException {
        queueServer.appendMessage(new NicknameRequest(nickName,this));
    }
    /**
     * This method overrides the default `equals` method to compare ClientHandlerRMI objects.
     * (Override from Object class)
     *
     * @param o The object to compare with this ClientHandlerRMI object.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientHandlerRMI that = (ClientHandlerRMI) o;
        return Objects.equals(clientName, that.clientName);
    }
    /**
     * This method overrides the default `hashCode` method to generate a hash code for ClientHandlerRMI objects.
     * (Override from Object class)
     *
     * @return An integer hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientName);
    }

    /**
     * Receive notify from game and send append it to the client queue
     * @param message object to pass
     */
    @Override
    public void update(SampleServerMessage message) throws RemoteException {
        this.clientQueue.appendMessage(message);
    }

    //NOT USED
    @Override
    public void updateFromView(SampleViewMessage message) throws RemoteException {

    }

    @Override
    public void requestData() {

    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public String test(){
        return "Test";

    }
}
