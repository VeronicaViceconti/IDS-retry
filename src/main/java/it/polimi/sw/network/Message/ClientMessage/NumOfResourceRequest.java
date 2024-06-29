package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;

/**
 * MESSAGGIO CHE PROBABILMENTE NON VERRA' MAI CHIAMATO QUINDI DA TOGLIERE
 */
public class NumOfResourceRequest extends SampleClientMessage{

    public NumOfResourceRequest(int lobby,int id_player) {
        super(TypeMessageClient.NUM_OF_RESOURCE_REQUEST,lobby, id_player);
    }


    @Override
    public void execute(GameControllerServer gcs) {

    }
}
