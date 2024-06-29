package it.polimi.sw.network.Socket;
import java.lang.Object;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

import it.polimi.sw.Observer.Observer;
import it.polimi.sw.network.Client;

import it.polimi.sw.network.ClientModel.Match;
import it.polimi.sw.network.Message.ClientMessage.*;

import it.polimi.sw.model.*;
import it.polimi.sw.network.Message.ViewMessage.*;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;
import it.polimi.sw.network.QueueHandlerClient;
import it.polimi.sw.view.View;
import java.io.*;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The ClientSocket class is the class in which there are the methods that the client invoke to play,
 * it takes the input from the view and send them, using socket, to the server.
 */
public class ClientSocket extends Client implements Observer, Runnable, Serializable {
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private View view;

    /**
     * the constructor of the ClientSocket class.
     * @param serveraddress the address to connect to the server.
     * @param serverport the address to connect to the server.
     * @param view the player's view.
     * @throws IOException
     */
    public ClientSocket(String serveraddress, int serverport, View view,String nickname) {
        super(view,nickname);
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(serveraddress, serverport));
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            output.flush();

            //start thread that reads from the socket stream
            new Thread(new SocketInputReader(input,this.queue)).start();
            System.out.println("Thread partito lato client per lettura input");
        } catch (IOException e ) {
            throw new RuntimeException(e);
        }
        System.out.println("Connessione socket avvenuta con server!");
        this.view = view;

        //mi connetto come osservatore della view
        this.view.addObserversView(this);

        //send connection message on socket
        connectToServer();
    }

    /**
     * This method likely establishes a connection with the server by sending a ConnectionRequest message.
     *
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    private void connectToServer(){
        sendMessage(new ConnectionRequest(nickName));
    }

    /**
     * This method sends a request to the server to place a card on the game board.
     *
     * @param card The Card object representing the card to be played.
     * @param coordinatesCard An array of integers representing the x and y coordinates of the placement.
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    public void placeCard (Card card,Integer[] coordinatesCard){
        sendMessage(new PlaceCardRequest(getIdlobby(), card,coordinatesCard[0], coordinatesCard[1],getMatch().getMe().getId()));
    }
    /**
     * This method sends a request to the server to resend the client's nickname.
     *
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    @Override
    public void resendNickname(){
        sendMessage(new NicknameRequest(this.nickName));
    }
    /**
     * This method sends a request to the server to draw a card from a specific deck (Gold or Resource).
     *
     * @param whichDeck An integer representing the deck to draw from (1 for Gold, other value for Resource).
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    @Override
    public void drawCardFromDeck(int whichDeck){
        if(whichDeck == 1) {//selezionato gold deck
            sendMessage(new DrawCardRequest(getIdlobby(), "Gold", getMatch().getMe().getId()));
        }else {
            sendMessage(new DrawCardRequest(getIdlobby(), "Resource", getMatch().getMe().getId()));
        }


    }
    /**
     * This method sends a request to the server to draw a card from a specific deck (Gold or Resource) at a specific face-up position on the table.
     *
     * @param whichDeck An integer representing the deck to draw from (1 for Gold, other value for Resource).
     * @param whichPose An integer representing the face-up position of the card on the table.
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */

    @Override
    public void drawCardFromTable(int whichDeck, int whichPose) {
        if(whichDeck == 1)
            //System.out.println("Arrivato al client la carta da pescare dal deck gold face up carta: "+whichPose);
            sendMessage(new DrawCardRequest(getIdlobby(), "Gold", whichPose, getMatch().getMe().getId()));
        else
            //System.out.println("Arrivato al client la carta da pescare dal deck resource face up carta: "+whichPose);
            sendMessage(new DrawCardRequest(getIdlobby(), "Resource", whichPose, getMatch().getMe().getId()));

    }


    /**
     * This method sends a request to the server to get the client's current points.
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    public void getMyPoints() {
        sendMessage(new MyPointsRequest(this.getIdlobby(), getMatch().getMe().getId()));
    }

    /**
     * This method sends the client's chosen secret objective to the server.
     *
     * @param obj The Objective object representing the chosen secret objective.
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    public void setMySecretObjective(Objective obj) {
        sendMessage(new SendPrivateObjective(getIdlobby(), getMatch().getMe().getId(),obj));
    }
    /**
     * This method sends a private chat message to another player in the lobby.
     *
     * @param message A SendingChatMessage object containing information about the message (player message, recipient nickname, sender ID).
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */

    @Override
    public void privateChatMessage(SendingChatMessage message) {
        sendMessage(new PrivateChatRequest(getIdlobby(), message.getPlayerMessage(), message.getToOne(), getMatch().getMe().getId()));


    }
    /**
     * This method sends a public chat message to all players in the lobby.
     *
     * @param message A SendingChatMessage object containing information about the message (player message, sender ID).
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    @Override
    public void publicChatMessage(SendingChatMessage message) {
        sendMessage(new PublicChatRequest(getIdlobby(),message.getPlayerMessage(), getMatch().getMe().getId()));

    }

    /**
     * Client send a message asking for his manuscript.
     */
    @Override
    public void getMyTable() {

        sendMessage(new BoardDataRequest(getIdlobby(), getMatch().getMe().getId()));
    }

    /**
     * Client send a message asking for his resource and object
     */
    @Override
    public void getMyNumOfResourceAndObject() {
        sendMessage(new NumOfResourceRequest(getIdlobby(), getMatch().getMe().getId()));
    }
    /**
     * This method sends a request to the server specifying the chosen pawn (piece) and the number of players.
     *
     * @param pion The Pion (game piece) chosen by the player.
     * @param numOfPlayers The total number of players in the lobby (0 for creating a game, otherwise number of players to join).
     * @throws IOException This exception might be thrown if there is an error during communication with the server.
     */
    @Override
    public void sendNumPlayersAndPion(Pion pion, int numOfPlayers) {
        if(numOfPlayers == 0){ //join game
            sendMessage(new PlayerNumberAndPionRequest(getIdlobby(),nickName,0,pion));
        }else{ //create game
            sendMessage(new PlayerNumberAndPionRequest(getIdlobby(),nickName,numOfPlayers,pion));
        }
    }

    @Override
    public void disconnection() {

    }

    @Override
    public void pingClient() {

    }
    /**
     * This method handles updates received from the View object (presumably representing user actions).
     * It uses a switch statement to handle different message types from the `SampleViewMessage` class.
     *
     * @param message A SampleViewMessage object containing information about the update.
     * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
     */
    @Override
    public void updateFromView(SampleViewMessage message) {
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
                resendNickname();
        }
    }

    /**
     * the method send the various types of messages to the ClientHandler.
     * @param message specifies the type, id_lobby, and the player that send message.
     */
    public void sendMessage(SampleClientMessage message)  {
        try{
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("server out");
        }

    }

/*    @Override
    public void updateChat(SampleViewMessage message) throws RemoteException {
        SampleClientMessage messageToSend = message.transformClientMex( );
        sendMessage(messageToSend);
    }*/


    /**
     * This protected static inner class likely implements a thread that reads messages from the server socket.
     */
        protected static class SocketInputReader implements Runnable{
        private ObjectInputStream in;
        private QueueHandlerClient queue;

        /**
         * Constructor for SocketInputReader.
         *
         * @param in The ObjectInputStream used for reading objects.
         * @param queue The QueueHandlerClient object for message queuing.
         */
        public SocketInputReader(ObjectInputStream in,QueueHandlerClient queue) {
            this.in = in;
            this.queue = queue;
        }

        /**
         * This method is called when the thread starts.
         * It reads objects from the socket in a loop and adds them to the queue as SampleServerMessage objects.
         * The loop continues until the end of the stream is reached (null message) or an IOException occurs.
         *
         * @throws IOException This exception is thrown if there is an error during IO operations on the socket.
         * @throws ClassNotFoundException This exception is thrown if a received object's class cannot be found.
         */
        @Override
        public void run() {

            try {
                Object message;
                while ((message = in.readObject()) != null) { //read message from socket and add it to queue
                    queue.appendMessage((SampleServerMessage) (message));

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
