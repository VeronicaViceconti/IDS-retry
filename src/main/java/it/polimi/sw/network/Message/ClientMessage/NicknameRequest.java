package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.network.CommonGameLogicServer;
import it.polimi.sw.network.RMI.ClientHandlerRMI;
import it.polimi.sw.network.Socket.ClientHandlerSOCKET;

/**
 * This class represents a message sent by the client to the server requesting to set a nickname.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message is used during the initial game setup phase. It communicates the player's chosen nickname to the server.
 */
public class NicknameRequest extends SampleClientMessage {
    private ClientHandlerRMI clientHandlerRMI;
    private ClientHandlerSOCKET clientHandlerSOCKET;
    private String name;

    /**
     * Constructor for a NicknameRequest message using a socket-based client handler (optional).
     *
     * @param nickname The player's chosen nickname.
     * @param clientHandlerSOCKET The `ClientHandlerSOCKET` object representing the socket-based client handler (might be null if using RMI).
     */
    public NicknameRequest(String nickname, ClientHandlerSOCKET clientHandlerSOCKET){
        super(TypeMessageClient.NICKNAME_REQUEST,-1,-1);
        this.name = nickname;
        this.clientHandlerRMI = null;
        this.clientHandlerSOCKET = clientHandlerSOCKET;
    }
    /**
     * Constructor for a NicknameRequest message using an RMI-based client handler (optional).
     *
     * @param nickname The player's chosen nickname.
     * @param clientHandlerRMI The `ClientHandlerRMI` object representing the RMI-based client handler.
     */
    public NicknameRequest(String nickname, ClientHandlerRMI clientHandlerRMI){
        super(TypeMessageClient.NICKNAME_REQUEST,-1,-1);
        this.name = nickname;
        this.clientHandlerSOCKET = null;
        this.clientHandlerRMI = clientHandlerRMI;
    }

    /**
     * Constructor for a NicknameRequest message without specifying a client handler.
     *
     * @param nickname The player's chosen nickname.
     */
    public NicknameRequest(String nickname){
        super(TypeMessageClient.NICKNAME_REQUEST,-1,-1);
        this.name = nickname;
    }

    /**
     * Getter method to access the player's chosen nickname.
     *
     * @return The String containing the player's chosen nickname.
     */
    public String getName(){
        return name;
    }
    /**
     * Getter method to access the RMI-based client handler (if applicable).
     *
     * @return The `ClientHandlerRMI` object representing the RMI-based client handler (null if not used).
     */
    public ClientHandlerRMI getClientHandlerRMI() {
        return clientHandlerRMI;
    }
    /**
     * Getter method to access the socket-based client handler (if applicable).
     *
     * @return The `ClientHandlerSOCKET` object representing the socket-based client handler (null if not used).
     */
    public ClientHandlerSOCKET getClientHandlerSOCKET() {
        return clientHandlerSOCKET;
    }
    /**
     * This method is likely overridden for server-side processing of the message,
     * but the provided implementations in `execute(CommonGameLogicServer)` and
     * `execute(GameControllerServer)` are empty.
     *
     * @param cgls The `CommonGameLogicServer` object representing the server-side game logic.
     */
    @Override
    public void execute(CommonGameLogicServer cgls) {

    }

    /**
     * This method is likely overridden for server-side processing of the message,
     * but the provided implementation in `execute(GameControllerServer)` is empty.
     *
     * @param gcs The `GameControllerServer` object representing the server-side game controller (might not be used).
     */

    @Override
    public void execute(GameControllerServer gcs) {

    }
}
