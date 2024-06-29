package it.polimi.sw.network.Message.serverMessage;


import it.polimi.sw.network.Client;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * This abstract class represents a base class for server-side messages sent to clients.
 * It's likely part of a larger communication framework between the game server and clients.
 */
public abstract class SampleServerMessage implements Serializable {
       private final TypeMessageServer type;

       /**
        * Constructor.
        * @param type type of the message. taken from enum TypeMessageClient
        */
       public SampleServerMessage(TypeMessageServer type){
           this.type = type;
       }
    /**
     * Getter method for the message type.
     *
     * @return The type of the message (e.g., CHAT_REPLY, DRAW_REPLY, etc.).
     */
       public TypeMessageServer getType(){
           return type;
       }

    /**
     * Abstract method that defines the actions to be taken by the server after sending a message to a client.
     * This method should be implemented by concrete subclasses to specify the specific behavior for each message type.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @throws RemoteException Indicates a potential exception if remote communication fails.
     */

       public abstract void execute(Client client) throws RemoteException;

    }
