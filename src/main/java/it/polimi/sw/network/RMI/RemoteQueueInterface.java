package it.polimi.sw.network.RMI;

import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * This interface defines a remote method for adding messages to a queue.
 */
public interface RemoteQueueInterface extends Remote {
     /**
      * This method adds a message object to a remote queue.
      *
      * @param message The message object to be appended to the queue. This message object likely extends
      *                 a base class named `SampleServerMessage`.
      * @throws RemoteException This exception is thrown if there is a problem communicating with the server remotely.
      */
     void appendMessage(SampleServerMessage message) throws RemoteException;

}
