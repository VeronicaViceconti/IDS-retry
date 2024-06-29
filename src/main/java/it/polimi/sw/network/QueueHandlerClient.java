package it.polimi.sw.network;

import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;
import it.polimi.sw.network.RMI.RemoteQueueInterface;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class implements the `RemoteQueueInterface` and extends `UnicastRemoteObject` to enable remote access to its methods.
 * It manages a queue of messages received from the server.
 */
public class QueueHandlerClient extends UnicastRemoteObject implements RemoteQueueInterface,Serializable {

    private LinkedBlockingDeque<SampleServerMessage> queue ;
    /**
     * Constructor for QueueHandlerClient.
     * Creates a new instance with an empty LinkedBlockingDeque for message storage.
     *
     * @throws RemoteException This exception is thrown if there is a problem with remote object creation.
     */
    public QueueHandlerClient() throws RemoteException {
        super();
        queue = new LinkedBlockingDeque<>();
    }

    /**
     * This method adds a SampleServerMessage object to the message queue.
     * It's synchronized to ensure thread-safe access to the queue.
     *
     * @param message The SampleServerMessage object to be added.
     * @throws RemoteException This exception is thrown if there is a problem during remote method invocation.
     */

    @Override
    public synchronized void appendMessage(SampleServerMessage message) throws RemoteException{
        try {
            queue.add(message);
        } catch (Exception e){
            System.out.println("Error deserializing message: "+e.getMessage());
        }
    }
    /**
     * This method retrieves and removes the next message from the queue.
     * It's synchronized to ensure thread-safe access to the queue.
     *
     * @return The next SampleServerMessage object from the queue, or null if the queue is empty.
     */
    public synchronized SampleServerMessage getNextMessage(){
        return queue.poll();
    }

    /**
     * This method checks if the message queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
