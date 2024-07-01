package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Pion;
import it.polimi.sw.network.Client;

import java.util.ArrayList;

/**
 * This class represents a message sent by the server to a client indicating that have made wrong selection pion.
 * It inherits from the `SampleServerMessage` class.
 */
public class SendingPionErrorReply extends SampleServerMessage {
    private ArrayList<Pion> availablePions;
    public SendingPionErrorReply(ArrayList<Pion> availablePions){
        super(TypeMessageServer.SENDING_PION_ERROR);
        this.availablePions = availablePions;
    }
    /**
     * This method defines the actions to be taken by the server after sending a PionErrorReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) {
        System.out.println("Pion selection gone wrong, rechoose:\n");
        client.getView().selectNumberOfplayerInMatch();
    }
}