package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
/**
 * This class represents a message sent by the client to check if the server is still connected (ping).
 * It inherits from the `SampleClientMessage` class (likely containing common message properties).
 *
 * This message includes the lobby ID and the ID of the player sending the ping.
 */
public class PingClient extends SampleClientMessage {

    /**
     * Constructor for a PingClient message.
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param id_player The ID of the player sending the ping.
     */
    public PingClient(int lobby,int id_player){
        super(TypeMessageClient.PING,lobby,id_player);
    }
    /**
     * This method likely defines the actions to be taken by the server upon receiving a PingClient message.
     * The specific implementation might be missing (commented out).
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this method overrides a method from the parent class (likely `SampleClientMessage`).
     */
    @Override
    public void execute(GameControllerServer gcs) {

    }
}
