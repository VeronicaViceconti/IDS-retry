package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Game;
import it.polimi.sw.network.Message.serverMessage.BoardDataReply;

  /**
   * MESSAGGIO CHE PROBABILMENTE NON VERRA' MAI CHIAMATO QUINDI DA TOGLIERE
   */
public class BoardDataRequest extends SampleClientMessage {


    public BoardDataRequest(int lobby, int id_player){
        super(TypeMessageClient.BOARD_DATA_REQUEST, lobby, id_player);
    }

    /**
     * this execute just has to send data from game model to client
     * @param gcs, the controller of the correct match
     */
    @Override
    public void execute(GameControllerServer gcs) {
    }
}
