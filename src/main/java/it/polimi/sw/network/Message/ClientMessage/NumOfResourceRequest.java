package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;

/**
 * A class representing a request from a client to the server for the number of resources.
 * This class extends the {@link SampleClientMessage} class.
 */

public class NumOfResourceRequest extends SampleClientMessage{
    /**
     * Constructor for the NumOfResourceRequest class.
     *
     * @param lobby The lobby ID of the game the request is for.
     * @param id_player The ID of the player making the request.
     */
    public NumOfResourceRequest(int lobby,int id_player) {
        super(TypeMessageClient.NUM_OF_RESOURCE_REQUEST,lobby, id_player);
    }
    /**
     * This method defines the action to be taken when the server receives this request.
     * It is expected to be overridden by a subclass to implement the specific logic for handling the request.
     *
     * @param gcs The {@link GameControllerServer} instance that handles the request.
     */

    @Override
    public void execute(GameControllerServer gcs) {

    }
}
