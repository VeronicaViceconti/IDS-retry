package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.network.Client;
/**
 * This class represents a message sent by the server to the client to check for client liveness.
 * It inherits from the `SampleServerMessage` class (likely containing common server message properties).
 *
 * The purpose of this message type (PING) is currently not implemented but likely intended to be part of a mechanism
 * to monitor client connections and detect inactive clients.
 */
public class PingServer extends SampleServerMessage {
    public PingServer(){
        super(TypeMessageServer.PING);
    }
    /**
     * This method defines the actions to be taken by the server after sending a PingServer message to a client.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) {

    }
}
