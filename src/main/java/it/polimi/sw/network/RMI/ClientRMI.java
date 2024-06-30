package it.polimi.sw.network.RMI;
import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.network.ClientModel.Match;
import it.polimi.sw.network.CommonGameLogicServer;
import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;
import it.polimi.sw.network.Message.ClientMessage.SendPrivateObjective;
import it.polimi.sw.network.Message.ViewMessage.*;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;
import it.polimi.sw.network.QueueHandlerClient;
import it.polimi.sw.view.View;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class likely implements a client-side functionality for a remote game using RMI.
 * It inherits from the base class `Client` and implements the `Serializable` and `Runnable` interfaces.
 */
public class ClientRMI extends Client implements Serializable,Runnable {
    private InterfaceClientHandlerRMI clientHandler;

    /**
     * This constructor likely initializes a client-side object for a remote game using RMI.
     *
     * @param view A reference to a view object (likely for handling UI elements)
     * @param nickName The chosen nickname for the client.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     * @throws NotBoundException This exception is thrown if the remote interface object (clientHandler) cannot be found in the RMI registry.
     */
    public ClientRMI(View view, String nickName) throws RemoteException, NotBoundException {
        super(view,nickName);
        view.setNickname(nickName);

        new Thread(this).start();
        //mi connetto come osservatore della view
        this.view.addObserversView(this);

        connectToServer();
    }

    /**
     *  Connecting to the server...
     * @throws RemoteException
     * @throws NotBoundException
     */

    private void connectToServer() throws RemoteException, NotBoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Matcher matcher;
        Registry registry = LocateRegistry.getRegistry("localhost", 1700);
        String remoteObjectName = "ClientHandlerRMI";
        this.clientHandler = (InterfaceClientHandlerRMI) registry.lookup(remoteObjectName);

        while(!clientHandler.duplicateHandler(nickName)) {
            do {
                try {
                    System.out.print("\nI'm sorry, the nickname is not available. \nChoose another nickname:");
                    this.nickName =  reader.readLine();
                    Pattern pattern = Pattern.compile("[A-Za-z]");
                    matcher = pattern.matcher(nickName);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid input!");
                    throw new IllegalArgumentException();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }while (!matcher.find());
        }

        System.out.println("CONNESSO CON CLIENT HANDLER");

        clientHandler = (InterfaceClientHandlerRMI) registry.lookup("ClientHandlerRMI_"+nickName);

        //invio queue al server

        // UNesporta l'oggetto queue come oggetto remoto
        UnicastRemoteObject.unexportObject(this.queue, true);
            // Se non è già esportato, esporta l'oggetto
        RemoteQueueInterface stub = (RemoteQueueInterface) UnicastRemoteObject.exportObject(this.queue, 0);

        // Registrati presso il server RMI
        clientHandler.register(stub, this.nickName);
        //chiamo metodo RMI passando me stesso per connettermi
        clientHandler.connectToServer(nickName);
    }

    /**
     * This method sends a request to the server to place a card on the game board.
     *
     * @param card The Card object representing the card to be placed.
     * @param coordin An array of integers likely representing the x and y coordinates for placement.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    public void placeCard(Card card,Integer[] coordin) throws RemoteException{
        clientHandler.placeCard(getIdlobby(),card,coordin[0],coordin[1],getMatch().getMe().getId());
    }
    /**
     * This method overrides the `resendNickname` method from an unknown interface (likely related to UI updates).
     * It sends a request to the server to resend the client's nickname.
     *
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void resendNickname() throws RemoteException {
        clientHandler.resendNickname(this.nickName);
    }

    /**
     * This method sends a request to the server to draw a card from the deck.
     *
     * @param selected An integer representing the deck selection (1 for Gold, else for Resource).
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    public void drawCardFromDeck(int selected) throws RemoteException {
        if(selected == 1) {//selezionato gold deck
            clientHandler.drawCardFromDeck(getIdlobby(), "Gold", getMatch().getMe().getId());
        }else {
            clientHandler.drawCardFromDeck(getIdlobby(), "Resource", getMatch().getMe().getId());
        }
    }
    /**
     * This method sends a request to the server to draw a card from a face-up deck on the game board.
     *
     * @param deck An integer representing the deck selection (1 for Gold, else for Resource).
     * @param pose The index of the specific card to draw from the chosen deck.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    public void drawCardFromTable(int deck,int pose) throws RemoteException {

        if(deck == 1) {
            clientHandler.drawCardFromTable(getIdlobby(),"Gold",pose, getMatch().getMe().getId());
        }else {
            clientHandler.drawCardFromTable(getIdlobby(),"Resource",pose, getMatch().getMe().getId());
        }
    }
    /**
     * This method sends a request to the server to get the player's points in the current lobby.
     *
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    public void getMyPoints () throws RemoteException {
        clientHandler.getMyPoints(getIdlobby(), getMatch().getMe().getId());
    }
    /**
     * This method sends a request to the server to set the player's secret objective in the current lobby.
     *
     * @param obj The Objective object representing the player's secret objective.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    public void setMySecretObjective (Objective obj) throws RemoteException {
        clientHandler.setMySecretObjective(getIdlobby(), getMatch().getMe().getId(),obj);
    }

    /**
     * This method sends a private chat message to another player in the lobby.
     *
     * @param message A SendingChatMessage object containing information about the message.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     * @throws RuntimeException This exception is re-thrown from the caught RemoteException for convenience.
     */
    @Override
    public void privateChatMessage(SendingChatMessage message) {
        try {
            clientHandler.privateChatMessage(getIdlobby(),message.getPlayerMessage(),message.getToOne() , getMatch().getMe().getId());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * This method sends a public chat message to all players in the lobby.
     *
     * @param message A SendingChatMessage object containing information about the message.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     * @throws RuntimeException This exception is re-thrown from the caught RemoteException for convenience.
     */
    @Override
    public void publicChatMessage(SendingChatMessage message) {
        try {
            clientHandler.publicChatMessage(getIdlobby(),message.getPlayerMessage(),getMatch().getMe().getId());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a request to the server to get the player's game board data in the current lobby.
     *
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */

    public void getMyTable() throws RemoteException {
        clientHandler.getMyTable(getIdlobby(), getMatch().getMe().getId());
    }
    /**
     * This method sends a request to the server to get the player's resource and object counts in the current lobby.
     *
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    public void getMyNumOfResourceAndObject() throws RemoteException {
        clientHandler.getMyNumOfResourceAndObject(getIdlobby(), getMatch().getMe().getId());
    }
    /**
     * This method sends information about the player (nickname, chosen pawn, number of players) to the server upon joining or creating a lobby.
     *
     * @param pion The Pawn (game piece) chosen by the player.
     * @param numOfPlayers The total number of players in the lobby (0 for creating a game).
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void sendNumPlayersAndPion(Pion pion, int numOfPlayers) throws RemoteException {
        if(numOfPlayers == 0) { //create game request
            clientHandler.sendNumPlayersAndPion(getIdlobby(),nickName,pion,0);
        }else{
            clientHandler.sendNumPlayersAndPion(getIdlobby(),nickName,pion,numOfPlayers);
        }
    }
    /**
     * This method sends a disconnection request to the server for the player in the current lobby.
     *
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void disconnection() throws RemoteException {
        clientHandler.disconnection(getIdlobby(), getMatch().getMe().getId());
    }
    /**
     * This method sends a ping request to the server to check the client's connection status in the current lobby.
     *
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void pingClient() throws RemoteException {
        clientHandler.pingClient(getIdlobby(),getMatch().getMe().getId());
    }
    /**
     * This method signature is likely intended for future implementation of handling updates
     * received directly from the server (without going through the View object).
     *
     * @param message A SampleServerMessage object (implementation not provided).
     */
    @Override
    public void update( SampleServerMessage message) {

    }
    /**
     * This method handles updates received from the View object.
     * It uses a switch statement to handle different message types from the `SampleViewMessage` class.
     *
     * @param message A SampleViewMessage object containing information about the update.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void updateFromView(SampleViewMessage message) throws RemoteException {
        switch(message.getType()){
            case SEND_OBJECTIVE_CHOSEN:
                //modifico il model + mando al server la scelta
                Objective ob = ((SendingPrivateObjective) message).getOb();
                match.getMe().setObjective(ob);

                setMySecretObjective(ob);
                break;
            case CARD_PLAYED:
                //il giocatore ha appena deciso di giocare una carta, invio al server la richiesta
                placeCard(((SendingCardPlayed) message).getCard(),((SendingCardPlayed) message).getCoords());
                break;
            case CARD_TO_DRAW_FACEDOWN:
                drawCardFromDeck(((SendingCardToDraw) message).getDeck());
                break;
            case CARD_TO_DRAW_FACEUP:
                drawCardFromTable(((SendingCardToDraw) message).getDeck(),((SendingCardToDraw) message).getPose());
                break;
            case CHAT_REQUEST:
                chatMessage((SendingChatMessage) message);
                break;
            case NUM_PLAYERS_CHOSEN:
                sendNumPlayersAndPion(((SendingNumPlayersAndPion)message).getPion(),((SendingNumPlayersAndPion)message).getNum());
                break;
            case RESEND_NICKNAME:
                nickName = ((ResendingNickname)message).getNicknname();
                view.setNickname(nickName);
                resendNickname();
                break;
        }
    }
}
