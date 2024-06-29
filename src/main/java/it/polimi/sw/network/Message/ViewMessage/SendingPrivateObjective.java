package it.polimi.sw.network.Message.ViewMessage;
import it.polimi.sw.model.*;

/**
 * This class represents a message sent by the client-side view to the server indicating the player's chosen private objective.
 * It inherits from the `SampleViewMessage` class.
 *
 * This message is used during the initial game setup to communicate the player's chosen private objective to the server.
 */

public class SendingPrivateObjective extends SampleViewMessage {

    private Objective ob;
    /**
     * Constructor for a SendingPrivateObjective message.
     *
     * @param obj The `Objective` object chosen by the player (private objective).
     */

    public SendingPrivateObjective(Objective obj) {
        super(TypeMessageView.SEND_OBJECTIVE_CHOSEN);
        this.ob = obj;
    }
    /**
     * Getter method to access the chosen objective.
     *
     * @return The `Objective` object chosen by the player.
     */

    public Objective getOb() {
        return ob;
    }
}
