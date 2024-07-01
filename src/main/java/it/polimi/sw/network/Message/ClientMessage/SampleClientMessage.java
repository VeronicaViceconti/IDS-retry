package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.network.CommonGameLogicServer;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * abstract class that is extended to specific messages. from client to server
 */
public abstract class SampleClientMessage implements Serializable {
    private final TypeMessageClient type;
    private int clientLobbyReference;

    private int id_player;


    /**
     * constructor.
     * @param type type of the message. taken from enum TypeMessageClient
     */
    public SampleClientMessage(TypeMessageClient type, int lobby, int id_player){
        clientLobbyReference = lobby;
        this.type = type;
        this.id_player= id_player;
    }
    /**
     * Constructor for a SampleClientMessage.
     *
     * @param type The `TypeMessageClient` enum value specifying the message type.
     */
    public SampleClientMessage(TypeMessageClient type){
        clientLobbyReference = -1;
        this.type = type;
    }

    /**
     * Retrieves the message type.
     *
     * @return The `TypeMessageClient` enum value representing the message type.
     */

    public TypeMessageClient getType(){
        return type;
    }
    /**
     * Retrieves the player ID associated with the message (might be set in subclasses).
     *
     * @return The player ID, or 0 if not set.
     */
    public int getId_player() {
        return id_player;
    }
    /**
     * Retrieves the lobby reference ID associated with the message (initialized to -1).
     *
     * @return The lobby reference ID, or -1 if not set.
     */
    public int getClientLobbyReference() {
        return clientLobbyReference;
    }
    /**
     * Abstract method defining the actions to be taken by the server upon receiving this message.
     * Subclasses likely implement this method to handle specific message types.
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this method is abstract and must be implemented by subclasses.
     */
    public abstract void execute(GameControllerServer gcs);
    /**
     * Empty method (might be intended for future implementation related to common game logic on the server).
     *
     * @param cgls The `CommonGameLogicServer` object (not currently used).
     */
    public void execute(CommonGameLogicServer cgls){}
}
