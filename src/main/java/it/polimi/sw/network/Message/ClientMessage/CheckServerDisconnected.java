package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.network.Message.serverMessage.ChatReply;
import it.polimi.sw.network.Message.serverMessage.CheckServerDisconnectedReply;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;

/**
 * This class represents a message sent by the client requesting to send keep alive for server.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID and the ID of the player who wants to exit.
 */
public class CheckServerDisconnected extends SampleClientMessage{
    public CheckServerDisconnected(int lobby, int id_player) {
        super(TypeMessageClient.SERVER_DISCONNECTED,lobby, id_player);
    }


    @Override
    public void execute(GameControllerServer gcs) {
        gcs.currgame.notify(new CheckServerDisconnectedReply());
    }
}
