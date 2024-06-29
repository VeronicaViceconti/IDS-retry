package it.polimi.sw.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.*;
import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;
import it.polimi.sw.network.RMI.ClientHandlerRMI;

import it.polimi.sw.network.Socket.ClientHandlerSOCKET;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * This class, likely named `CommonGameLogicServer`, implements the `Runnable` interface and likely represents the core logic server for a game.
 * It handles client connections, processes messages, and manages game state.
 */
public class CommonGameLogicServer implements Runnable{
    /**
     * The port on which the server listens for connections (final).
     */
    private final int serverPort;

    /**
     * The server socket object used for accepting connections.
     */
    private ServerSocket serverSocket;

    /**
     * A map that stores references to client handlers for socket connections (key: client nickname, value: `ClientHandlerSOCKET` object).
     */
    private Map<String, ClientHandlerSOCKET> handlerForClient_Map;

    /**
     * A map that stores references to client handlers for RMI connections (key: client nickname, value: `ClientHandlerRMI` object).
     */
    private Map<String, ClientHandlerRMI> handlerForClient_Map_RMI;

    /**
     * A list containing references to `GameControllerServer` objects representing game lobbies.
     */
    private List<GameControllerServer> lobbyReference;

    /**
     * A list containing all messages (chat or event, private or public) for the lobby.
     */
    private List<SampleClientMessage> lobbyAllMessage;

    /**
     * An `ArrayList` containing objective cards for the game (can be null).
     */
    private ArrayList<Objective> ObjectiveCards;

    /**
     * HashMaps representing card correspondence (details unclear without further context).
     */
    private HashMap<Card, Card> corrGold;
    private HashMap<Card, Card> corrRes;
    private HashMap<Card, Card> corrInit;


    /**
     * Constructor initializing the server with the specified port number.
     *
     * @param serverPort The port number.
     */
    public CommonGameLogicServer(int serverPort) {
        super();
        this.serverPort = serverPort;
        this.handlerForClient_Map = new HashMap<>();
        this.handlerForClient_Map_RMI = new HashMap<>();
        this.lobbyReference = new ArrayList<>();
        this.lobbyAllMessage = new ArrayList<>();
    }
    /**
     * Returns a copy of the `ObjectiveCards` list.
     *
     * @return A copy of the `ObjectiveCards` list.
     */
    public ArrayList<Objective> getObjectiveCards() {
        return new ArrayList<>(ObjectiveCards);
    }
    /**
     * Returns a copy of the `corrGold` HashMap.
     *
     * @return A copy of the `corrGold` HashMap.
     */
    public HashMap<Card, Card> getCorrGold() {
        return new HashMap<>(corrGold);
    }
    /**
     * Returns a copy of the `corrRes` HashMap.
     *
     * @return A copy of the `corrRes` HashMap.
     */

    public HashMap<Card, Card> getCorrRes() {
        return new HashMap<>(corrRes);
    }
    /**
     * Returns a copy of the `corrInit` HashMap.
     *
     * @return A copy of the `corrInit` HashMap.
     */
    public HashMap<Card, Card> getCorrInit() {
        return new HashMap<>(corrInit);
    }
    /**
     * Returns a copy of the `lobbyReference` list.
     *
     * @return A copy of the `lobbyReference` list.
     */
    public List<GameControllerServer> getLobbyReference() {
        return lobbyReference;
    }

    public Map<String, ClientHandlerSOCKET> getHandlerForClient_Map() {
        return handlerForClient_Map;
    }

    public void setHandlerForClient_Map(Map<String, ClientHandlerSOCKET> handlerForClient_Map) {
        this.handlerForClient_Map = handlerForClient_Map;
    }
    /**
     * Returns the `handlerForClient_Map`.
     *
     * @return The `handlerForClient_Map`.
     */
    public Map<String, ClientHandlerRMI> getHandlerForClient_Map_RMI() {
        return handlerForClient_Map_RMI;
    }
    /**
     * Sets the `handlerForClient_Map`.
     *
     * @param handlerForClient_Map_RMI The new `handlerForClient_Map`.
     */
    public void setHandlerForClient_Map_RMI(Map<String, ClientHandlerRMI> handlerForClient_Map_RMI) {
        this.handlerForClient_Map_RMI = handlerForClient_Map_RMI;
    }
    /**
     * This method adds a client handler for a socket connection to the `handlerForClient_Map`. It checks if a handler
     * already exists for the provided nickname before adding the new one.
     *
     * @param nickname The client's nickname.
     * @param clientHandlerSOCKET The `ClientHandlerSOCKET` object for the client.
     */
    public void addSocketHandler(String nickname, ClientHandlerSOCKET clientHandlerSOCKET) {
        if(!handlerForClient_Map_RMI.containsKey(nickname))
            handlerForClient_Map.put(nickname,clientHandlerSOCKET);

    }

    /**
     * This method adds a client handler for an RMI connection to the `handlerForClient_Map_RMI`. It checks if a handler
     * already exists for the provided nickname before adding the new one.
     *
     * @param nickname The client's nickname.
     * @param clientHandlerRMI The `ClientHandlerRMI` object for the client.
     */
    public void addRMIHandler(String nickname, ClientHandlerRMI clientHandlerRMI) {
        if(!handlerForClient_Map_RMI.containsKey(nickname))
            handlerForClient_Map_RMI.put(nickname,clientHandlerRMI);
    }

    /**
     * The main loop of the server. It performs the following:
     *  1. Loads game data (likely `saveallcard`).
     *  2. Creates a server socket and starts a thread for queue processing.
     *  3. Continuously accepts new client connections, creates `ClientHandlerSOCKET` objects, and starts threads for handling each client.
     *  4. Terminates the loop when the thread is interrupted.
     *
     * @Override This method overrides the `run()` method from the `Runnable` interface.
     */

    @Override
    public void run(){
        saveallcard();
        try{
            serverSocket = new ServerSocket(serverPort);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        System.out.println("Server ready: ");
        QueueHandlerServer queueHandler = QueueHandlerServer.getInstance();
        ProcessQueue queueProcessor = new ProcessQueue(queueHandler, this);
        Thread queueThread = new Thread(queueProcessor, "QueueProcessorThread");
        queueThread.start();
        while(!Thread.currentThread().isInterrupted()){
            try{
                Socket client = serverSocket.accept();
                System.out.println("Another socket accepted!");
                client.setSoTimeout(0);
                ClientHandlerSOCKET clientHandlerSOCKET = new ClientHandlerSOCKET(client,this, QueueHandlerServer.getInstance());

                System.out.println("Client connected from address: "+client.getInetAddress());
                Thread thread = new Thread(clientHandlerSOCKET, "handler" +client.getInetAddress());
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        queueThread.interrupt();
    }

    /**
     * This method likely loads game card data from JSON files. It reads resource cards, gold cards, initial cards, and objective cards from separate files
     * and populates corresponding ArrayLists. It then creates HashMaps to map the "front" side of cards to their "back" sides based on their position in the loaded lists.
     */
    public void saveallcard(){
         ArrayList<InitialCard> newInitialCard = new ArrayList<>();
         ArrayList<ResourceCard> newResourceCard = new ArrayList<>();
         ArrayList<GoldCard> newGoldCard = new ArrayList<>();
         ArrayList<Objective> newObjectiveCards = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
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

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        corrGold = new HashMap<Card, Card>();
        corrRes = new HashMap<Card, Card>();
        corrInit = new HashMap<Card, Card>();
        ObjectiveCards = new ArrayList<>(newObjectiveCards);


        for(int i=0,j=0; j < newResourceCard.size()/2;i+=2,j++){
            corrRes.put(newResourceCard.get(i),newResourceCard.get(i+1));
        }
        for(int i=0,j=0; j < newGoldCard.size()/2;i++,j++){
            corrGold.put(newGoldCard.get(i),newGoldCard.get(i+40));
        }

        for(int i=0; i< newInitialCard.size()/2; i++){
            corrInit.put(newInitialCard.get(i),newInitialCard.get(i+6));
        }
    }

}