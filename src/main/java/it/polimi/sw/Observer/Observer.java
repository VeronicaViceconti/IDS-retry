package it.polimi.sw.Observer;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;
import it.polimi.sw.network.Message.ViewMessage.SampleViewMessage;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;

import java.io.IOException;
import java.rmi.RemoteException;
/**
 * This interface defines the contract for observer objects in an observer pattern implementation.
 * Observer objects are notified of changes in an observable object.
 *
 * @param <T> The type of object that the observer expects to receive updates about.
 */
public interface Observer <T> {
    /**
     * notify the observer of the change
     * @param message object to pass
     */
    void update(SampleServerMessage message) throws RemoteException;
    /**
     * The observer receives a `SampleViewMessage` object containing the view-related information.
     *
     *  Without further context, the purpose and usage of this method are unclear.
     *
     * @param message The `SampleViewMessage` object containing the view update information.
     * @throws RemoteException This method declaration indicates that the update notification
     *  might involve remote communication, potentially throwing a `RemoteException` if there are issues.
     */

    void updateFromView(SampleViewMessage message) throws RemoteException;
    /**
     * This method is likely called by the observer to request data from the observable object.
     * The specific implementation of how data is requested depends on the specific use case.
     */
    void requestData();
}
