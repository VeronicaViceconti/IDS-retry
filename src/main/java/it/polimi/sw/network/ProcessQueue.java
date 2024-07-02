package it.polimi.sw.network;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Message.ClientMessage.ConnectionRequest;
import it.polimi.sw.network.Message.ClientMessage.NicknameRequest;
import it.polimi.sw.network.Message.ClientMessage.PlayerNumberAndPionRequest;
import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;
import it.polimi.sw.network.Message.serverMessage.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.sw.network.Message.ClientMessage.TypeMessageClient.SERVER_DISCONNECTED;

/**
 * This class likely implements a thread that processes messages received from clients through the provided QueueHandler.
 * It interacts with a CommonGameLogicServer object to handle the game logic based on the messages.
 */
public class ProcessQueue implements Runnable {
    private final QueueHandlerServer queueHandler;
    private final CommonGameLogicServer server;
    /**
     * Constructor for ProcessQueue.
     *
     * @param queueHandler The QueueHandler object for accessing client messages.
     * @param server The CommonGameLogicServer object for game logic handling.
     */
    public ProcessQueue(QueueHandlerServer queueHandler, CommonGameLogicServer server) {
        this.queueHandler = queueHandler;
        this.server = server;
    }
    /**
     * This method is called when the thread starts.
     * It likely retrieves messages from the queueHandler in a loop and calls appropriate methods on the server object to process the game logic based on the message content.
     */
    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            SampleClientMessage message = queueHandler.getNextMessage();
            if (message != null) {
                try {
                    processMessage(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    /**
     * Processes the received message from the client and takes appropriate action
     * based on the message type.
     * <p>
     * For a {@code CONNECTION_REQUEST} message, it checks the type of connection
     * (RMI or SOCKET) and adds the appropriate handler for the client. It also manages
     * the player pion selection and game creation.
     * </p>
     * <p>
     * For a {@code NICKNAME_REQUEST} message, it checks if the nickname already exists
     * and notifies the client of the result.
     * </p>
     *
     * @param message the message received from the client
     */
    private void processMessage(SampleClientMessage message) throws RemoteException {

        switch(message.getType()){
            case CONNECTION_REQUEST -> {
                if(((ConnectionRequest)message).getClientHandlerRMI() == null){ //socket
                    if(server.getHandlerForClient_Map().containsKey(((ConnectionRequest)message).getNickname()) ||
                            server.getHandlerForClient_Map_RMI().containsKey(((ConnectionRequest)message).getNickname())){

                        try {
                            ((ConnectionRequest)message).getClientHandlerSOCKET().update(new NicknameReply(false));
                            System.out.println("NICKNAME ALREADY EXISTS SOCKET");
                            return;
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        server.addSocketHandler(((ConnectionRequest)message).getNickname(),((ConnectionRequest)message).getClientHandlerSOCKET());
                        System.out.println("Totale handler socket attivi: "+server.getHandlerForClient_Map().size());
                    }
                }else {
                    if(server.getHandlerForClient_Map_RMI().containsKey(((ConnectionRequest)message).getNickname()) ||
                            server.getHandlerForClient_Map().containsKey(((ConnectionRequest)message).getNickname())){

                        try {
                            ((ConnectionRequest)message).getClientHandlerRMI().update(new NicknameReply(false));
                            System.out.println("NICKNAME ALREADY EXISTS RMI");
                            return;
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        server.addRMIHandler(((ConnectionRequest)message).getNickname(),((ConnectionRequest)message).getClientHandlerRMI());
                    }
                }
                Pion []allPions = new Pion[]{Pion.pion_blue,Pion.pion_yellow,Pion.pion_red,Pion.pion_green};
                ArrayList<Pion> pions = new ArrayList<>(Arrays.asList(allPions));
                System.out.println("numero di controller: "+server.getLobbyReference().size());
                for (GameControllerServer lobby : server.getLobbyReference()) {
                    System.out.println("prima di !lobby.isfull " +lobby.toString());
                    if(!lobby.isItFull()){

                        for (Player p : lobby.getPlayersList()) {

                            pions.remove(p.getPion());
                        }

                        if(((ConnectionRequest)message).getClientHandlerRMI() == null) {
                            lobby.currgame.addObservers(((ConnectionRequest) message).getClientHandlerSOCKET());
                            ((ConnectionRequest) message).getClientHandlerSOCKET().sendMessageToClient(new GameCreated(pions,0,lobby.lobbyreference));
                        }else {
                            lobby.currgame.addObservers(((ConnectionRequest) message).getClientHandlerRMI());
                            try {
                                ((ConnectionRequest) message).getClientHandlerRMI().update(new GameCreated(pions,0,lobby.lobbyreference));
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }
                            return;
                    }
                }
                GameControllerServer gameControllerServer = new GameControllerServer(server.getLobbyReference().size(), server);
                gameControllerServer.currgame.setMaxPlayersNumbers(0);

                if(((ConnectionRequest)message).getClientHandlerRMI() == null) {
                    gameControllerServer.currgame.addObservers(((ConnectionRequest) message).getClientHandlerSOCKET());
                }else {
                    gameControllerServer.currgame.addObservers(((ConnectionRequest) message).getClientHandlerRMI());
                }
                server.getLobbyReference().add(gameControllerServer);
                gameControllerServer.getCurrgame().notify(new GameCreated(pions,1, gameControllerServer.lobbyreference));

            }
            case NICKNAME_REQUEST -> {
                String name = ((NicknameRequest)message).getName();
                if((server.getHandlerForClient_Map().containsKey(name) || server.getHandlerForClient_Map_RMI().containsKey(name))){

                    try {
                        if(((NicknameRequest)message).getClientHandlerRMI() == null){
                            ((NicknameRequest)message).getClientHandlerSOCKET().update(new NicknameReply(false));
                        }else{
                            ((NicknameRequest)message).getClientHandlerRMI().update(new NicknameReply(false));
                        }
                        System.out.println("NICKNAME ALREADY EXISTS MORE TIMES RMI");

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Pion []allPions = new Pion[]{Pion.pion_blue,Pion.pion_yellow,Pion.pion_red,Pion.pion_green};
                    ArrayList<Pion> pions = new ArrayList<>(Arrays.asList(allPions));
                    for (GameControllerServer lobby : server.getLobbyReference()) {
                        if(!lobby.isItFull()){

                            for (Player p : lobby.getPlayersList()) {
                                pions.remove(p.getPion());
                            }

                            if(((NicknameRequest)message).getClientHandlerRMI() == null) {
                                lobby.currgame.addObservers(((NicknameRequest) message).getClientHandlerSOCKET());
                                try {
                                    ((NicknameRequest) message).getClientHandlerSOCKET().update(new GameCreated(pions,0,lobby.lobbyreference));
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            }else {
                                lobby.currgame.addObservers(((NicknameRequest) message).getClientHandlerRMI());
                                try {
                                    ((NicknameRequest) message).getClientHandlerRMI().update(new GameCreated(pions,0,lobby.lobbyreference));
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            return;
                        }
                    }
                    GameControllerServer gameControllerServer = new GameControllerServer(server.getLobbyReference().size(), server);
                    gameControllerServer.currgame.setMaxPlayersNumbers(0);

                    if(((NicknameRequest)message).getClientHandlerRMI() == null)
                        gameControllerServer.currgame.addObservers(((NicknameRequest)message).getClientHandlerSOCKET());
                    else
                        gameControllerServer.currgame.addObservers(((NicknameRequest)message).getClientHandlerRMI());

                    server.getLobbyReference().add(gameControllerServer);
                    gameControllerServer.getCurrgame().notify(new GameCreated(pions,1, gameControllerServer.lobbyreference));

                }
            }
            case PLAYER_NUMBER_REQUEST -> {
                int lobbyid = (message).getClientLobbyReference();
                Boolean flag = false;
                Pion pion = ((PlayerNumberAndPionRequest)message).getPion();
                Pion [] allPions = new Pion[]{Pion.pion_blue,Pion.pion_yellow,Pion.pion_red,Pion.pion_green};
                ArrayList<Pion> pions = new ArrayList<>(Arrays.asList(allPions));
                for (Player p : server.getLobbyReference().get(lobbyid).getCurrgame().getTotPlayers()) {
                    if(p.getPion().equals(pion))
                        flag = true;
                    pions.remove(p.getPion());
                }
                if(flag){

                    if(server.getHandlerForClient_Map_RMI().get(((PlayerNumberAndPionRequest)message).getNickname()) == null){
                        server.getHandlerForClient_Map().get(((PlayerNumberAndPionRequest)message).getNickname()).update(new SendingPionErrorReply(pions));
                    }else{
                        server.getHandlerForClient_Map_RMI().get(((PlayerNumberAndPionRequest)message).getNickname()).update(new SendingPionErrorReply(pions));
                    }
                }else {
                    int numberofPlayer = ((PlayerNumberAndPionRequest)message).getNum();
                    Player player = new Player(((PlayerNumberAndPionRequest)message).getNickname(), pion);
                    player.setId(server.getLobbyReference().get(lobbyid).getPlayersList().size());
                    if(numberofPlayer == 0)
                    {

                        server.getLobbyReference().get(lobbyid).addPlayertoGame(player);
                        if(!server.getLobbyReference().get(lobbyid).isItFull())
                        {
                            server.getLobbyReference().get(lobbyid).getCurrgame().notify(new WaitingPlayerReply());
                        }
                    }else{
                        server.getLobbyReference().get(lobbyid).getCurrgame().setMaxPlayersNumbers(numberofPlayer);
                        server.getLobbyReference().get(lobbyid).addPlayertoGame(player);
                        server.getLobbyReference().get(lobbyid).getCurrgame().notify(new WaitingPlayerReply());
                    }
                }
            }
            case DRAW_DECK_REQUEST, PLACE_REQUEST, PRIVATE_OBJECTIVE_REQUEST, DRAW_TABLE_REQUEST -> message.execute(server.getLobbyReference().get((message).getClientLobbyReference()));
            case PING -> {}
            case PRIVATE_MESSAGE_REQUEST -> message.execute(server.getLobbyReference().get((message).getClientLobbyReference()));
            case PUBLIC_MESSAGE_REQUEST -> message.execute(server.getLobbyReference().get((message).getClientLobbyReference()));
            case SERVER_DISCONNECTED -> {
                for(GameControllerServer gc : server.getLobbyReference()){
                    gc.getCurrgame().notify(new CheckServerDisconnectedReply());
                }
            }
            case DisconnectionRequestGameEnding -> {
                int lobby = message.getClientLobbyReference();
                GameControllerServer gameControllerServer = server.getLobbyReference().get(lobby);
                for(Player p : gameControllerServer.getPlayersList()){
                    if(server.getHandlerForClient_Map().containsKey(p.getNickName()) || server.getHandlerForClient_Map_RMI().containsKey(p.getNickName())){
                        if(server.getHandlerForClient_Map_RMI().get(p.getNickName()) == null){
                            server.getHandlerForClient_Map().remove(p.getNickName());
                        }else{
                            server.getHandlerForClient_Map_RMI().remove(p.getNickName());
                        }
                    }
                }
            }
        }
    }

}
