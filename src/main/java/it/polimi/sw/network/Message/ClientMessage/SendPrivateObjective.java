package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.GameState;
import it.polimi.sw.model.Objective;
import it.polimi.sw.network.Message.serverMessage.TurnReply;
/**
 * This class represents a message sent by the client to the server containing a private objective chosen by the client.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID, player ID, and the chosen objective (`Objective` object).
 */
public class SendPrivateObjective extends SampleClientMessage{

    Objective ob;

    /**
     * Constructor for a SendPrivateObjective message.
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param id_player The ID of the player sending the message.
     * @param ob The `Objective` object representing the chosen private objective.
     */
    public SendPrivateObjective(int lobby,int id_player,Objective ob) {
        super(TypeMessageClient.PRIVATE_OBJECTIVE_REQUEST,lobby, id_player);
        this.ob = ob;
    }

    /**
     * This method likely defines the actions to be taken by the server upon receiving a SendPrivateObjective message.
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this method overrides a method from the parent class.
     */
    @Override
    public void execute(GameControllerServer gcs) {
        gcs.currgame.getTotPlayers().get(getId_player()).setObjective(ob);
        gcs.addSecretObjective(); //if everyone has chosen, then this method starts the game loop
    }
}
