package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
/**
 * This class represents a message sent by the client to inform the server about a disconnection.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID and the ID of the player who is disconnecting.
 */
public class DisconnectionRequest extends SampleClientMessage {
    /**
     * Constructor for a DisconnectionRequest message.
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param id_player The ID of the player who is disconnecting.
     */
    public DisconnectionRequest(int lobby, int id_player){
        super(TypeMessageClient.DISCONNECTION,lobby,id_player);
    }
    /**
     * This method likely defines the actions to be taken by the server upon receiving a DisconnectionRequest message.
     * The specific implementation for handling the disconnection might be missing (empty).
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this code overrides a method from the parent class.
     */
    @Override
    public void execute(GameControllerServer gcs) {

    }
}
