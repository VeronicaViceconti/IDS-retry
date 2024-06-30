package it.polimi.sw.Observer;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;
import it.polimi.sw.network.Message.ViewMessage.SampleViewMessage;
import it.polimi.sw.network.Message.ViewMessage.SendingChatMessage;
import it.polimi.sw.network.Message.ViewMessage.TypeMessageView;
import it.polimi.sw.network.Message.serverMessage.ErrorType;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
/**
 * This abstract class defines the base functionality for observable objects in an observer pattern implementation.
 * Observable objects manage a list of registered observers and notify them of changes.
 *
 * @param <T> The type of object that the observable provides updates about.
 */
public abstract class Observable <T> implements Serializable {
 private final ArrayList<Observer<T>> observers = new ArrayList<>();
 private final ArrayList<Observer<T>> observersView = new ArrayList<>();

 /**
  * Adds an observer to the list of observers for server-side updates.
  *
  * @param observer The observer object to be added.
  */
 public void addObservers(Observer<T> observer){
    if(!observers.contains(observer))
        observers.add(observer);

 }
 /**
  * Adds an observer to the list of observers for view-related updates.
  *
  * @param observer The observer object to be added.
  */
 public void addObserversView(Observer<T> observer){
     if(!observersView.contains(observer))
            observersView.add(observer);

 }
    /**
     * Removes an observer from the list of observers for server-side updates.
     * This method is synchronized to ensure thread-safety when modifying the observers list.
     *
     * @param obs The observer object to be removed.
     */
 public void removeObservers(Observer<T> obs){
     synchronized (observers){
         observers.remove(obs);
     }
 }
    /**
     * Removes an observer from the list of observers for view-related updates.
     * This method is synchronized to ensure thread-safety when modifying the observers list.
     *
     * @param observer The observer object to be removed.
     */
 public void removeObserversView(Observer<T> observer){
     synchronized (observersView){
         observersView.remove(observer);
     }
 }
    /**
     * Notifies all registered observers for server-side updates about a change.
     * Iterates through the list of observers and calls their `update` method with the provided `SampleServerMessage` object.
     * Catches potential `RemoteException` exceptions and re-throws them as runtime exceptions.
     *
     * @param message The `SampleServerMessage` object containing the update information.
     */
 public void notify(SampleServerMessage message)  {
     for(Observer observer: observers){
         try {
             observer.update(message);
         } catch (RemoteException e) {
             throw new RuntimeException(e);
         }
     }
 }
    /**
     * Returns a copy of the list of observers for server-side updates.
     *
     * @return A copy of the `observers` list.
     */
 public List<Observer<T>> getObservers() {
        return observers;
    }
    /**
     * Notifies all registered observers for view-related updates about a change.
     * Iterates through the list of observers and calls their `updateFromView` method with the provided `SampleViewMessage` object.
     * Catches potential `RemoteException` exceptions and re-throws them as runtime exceptions.
     *
     * @param message The `SampleViewMessage` object containing the view update information.
     */
    public void notify(SampleViewMessage message){
        for(Observer observer: observersView){
            try {
                if(message.getType().equals(TypeMessageView.CHAT_REQUEST)){
                    SendingChatMessage control = (SendingChatMessage) message; //per riferimento
                    Client cl = (Client) observer;
                    if(!control.isPublic()){
                        for(Player p: cl.getMatch().getTotPlayers()){
                            if(control.getToOne().equalsIgnoreCase(p.getNickName())){
                                observer.updateFromView(message);
                                return;
                            }
                        }
                        cl.getView().dealWithError(ErrorType.NO_DESTINATION);
                    }else{
                        observer.updateFromView(message);
                    }
                }else{
                    observer.updateFromView(message);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Notifies all registered observers for view-related updates to request data about other players.
     * Iterates through the list of observers and calls their `requestData` method.
     */
    public void requestOtherData(){
           for(Observer observer: observersView){
               observer.requestData(); //inside there should be control, who asked data
           }
    }


}
