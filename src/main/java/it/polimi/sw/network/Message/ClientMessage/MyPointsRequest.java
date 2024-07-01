package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;

/**
 * This class represents a message sent by the client requesting to get point in  the game.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID and the ID of the player who wants to exit.
 */
public class MyPointsRequest extends SampleClientMessage{
    public MyPointsRequest(int lobby, int id_player) {
        super(TypeMessageClient.MYPOINTS_REQUEST,lobby, id_player);
    }


    @Override
    public void execute(GameControllerServer gcs) {

    }
}
