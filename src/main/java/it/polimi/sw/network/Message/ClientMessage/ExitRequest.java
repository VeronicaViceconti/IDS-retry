package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
/**
 * This class represents a message sent by the client requesting to exit the game.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID and the ID of the player who wants to exit.
 */
public class ExitRequest extends SampleClientMessage {
    /**
     * Constructor for an ExitRequest message.
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param id_player The ID of the player who wants to exit.
     */
    public ExitRequest(int lobby, int id_player){
        super(TypeMessageClient.EXIT_REQUEST,lobby,id_player);
    }

    /**
     * This method likely defines the actions to be taken by the server upon receiving an ExitRequest message.
     * The specific implementation for handling the exit might be missing (empty).
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
