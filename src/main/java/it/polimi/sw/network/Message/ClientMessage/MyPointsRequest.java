package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;

/**
 * MESSAGGIO CHE PROBABILMENTE NON VERRA' MAI CHIAMATO QUINDI DA TOGLIERE
 */
public class MyPointsRequest extends SampleClientMessage{
    public MyPointsRequest(int lobby, int id_player) {
        super(TypeMessageClient.MYPOINTS_REQUEST,lobby, id_player);
    }


    @Override
    public void execute(GameControllerServer gcs) {

    }
}
