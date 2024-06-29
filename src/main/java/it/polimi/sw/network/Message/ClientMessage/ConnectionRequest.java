package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.network.RMI.ClientHandlerRMI;
import it.polimi.sw.network.Socket.ClientHandlerSOCKET;
/**
 * This class represents a message sent by the client to the server requesting a connection.
 * It inherits from the `SampleClientMessage` class (likely containing common message properties).
 *
 * This message specifies the client's chosen nickname and the type of connection handler used
 * (either RMI or SOCKET).
 */
public class ConnectionRequest extends SampleClientMessage{

    private String nickname;
    private ClientHandlerRMI clientHandlerRMI;
    private ClientHandlerSOCKET clientHandlerSOCKET;

    /**
     * Constructor for a ConnectionRequest message using RMI for communication.
     *
     * @param nickname The chosen nickname for the client.
     * @param clientHandlerRMI The `ClientHandlerRMI` object used for communication.
     */
    public ConnectionRequest(String nickname, ClientHandlerRMI clientHandlerRMI) {
        super(TypeMessageClient.CONNECTION_REQUEST);
        this.nickname = nickname;
        this.clientHandlerRMI = clientHandlerRMI;
        this.clientHandlerSOCKET = null;
    }
    /**
     * Constructor for a ConnectionRequest message using SOCKET for communication.
     *
     * @param nickname The chosen nickname for the client.
     * @param clientHandlerSOCKET The `ClientHandlerSOCKET` object used for communication (likely implements socket communication).
     */
    public ConnectionRequest(String nickname, ClientHandlerSOCKET clientHandlerSOCKET) {
        super(TypeMessageClient.CONNECTION_REQUEST);
        this.nickname = nickname;
        this.clientHandlerRMI = null;
        this.clientHandlerSOCKET = clientHandlerSOCKET;
    }
    /**
     * Constructor for a ConnectionRequest message with nickname but without specifying the connection handler.
     * The connection handler type (RMI or SOCKET) might be set later.
     *
     * @param nickname The chosen nickname for the client.
     */
    public ConnectionRequest(String nickname) {
        super(TypeMessageClient.CONNECTION_REQUEST);
        this.nickname = nickname;
        this.clientHandlerRMI = null;
        this.clientHandlerSOCKET = null;
    }
    /**
     * Retrieves the chosen nickname for the client.
     *
     * @return The nickname string.
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * Retrieves the `ClientHandlerRMI` object used for communication.
     *
     * @return The `ClientHandlerRMI` object or null if not set.
     */
    public ClientHandlerRMI getClientHandlerRMI() {
        return clientHandlerRMI;
    }
    /**
     * Retrieves the `ClientHandlerSOCKET` object used for communication (if set, otherwise null).
     *
     * @return The `ClientHandlerSOCKET` object or null if not set.
     */
    public ClientHandlerSOCKET getClientHandlerSOCKET() {
        return clientHandlerSOCKET;
    }

    /**
     * This method likely defines the actions to be taken by the server upon receiving a ConnectionRequest message.
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this method overrides a method from the parent class.
     */
    @Override
    public void execute(GameControllerServer gcs) {

    }
}
