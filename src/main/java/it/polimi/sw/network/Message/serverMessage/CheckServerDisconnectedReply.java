package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;

import java.rmi.RemoteException;

public class CheckServerDisconnectedReply extends SampleServerMessage{
    /**
     * Constructor.
     *
     */
    public CheckServerDisconnectedReply() {
        super(TypeMessageServer.SERVER_DISCONNECTED_REPLY);
    }

    @Override
    public void execute(Client client) throws RemoteException {
        client.getView().isServerAlive();
    }
}
