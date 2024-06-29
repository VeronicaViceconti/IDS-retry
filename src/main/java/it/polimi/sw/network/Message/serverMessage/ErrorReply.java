package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
/**
 * This class represents a message sent by the server to the client containing an error notification.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message is used to inform the client about an error that has occurred on the server-side.
 */
public class ErrorReply extends SampleServerMessage {
    private final ErrorType typeError;
    private final Player errorPlayer;
    /**
     * Constructor for an ErrorReply message.
     *
     * @param type The type of error that occurred.
     * @param player The player associated with the error (optional, can be null if the error is general).
     */

    public ErrorReply(ErrorType type, Player player){
        super(TypeMessageServer.ERROR);
        this.typeError = type;
        this.errorPlayer = player;
    }

    /**
     * This method defines the actions to be taken by the server after sending an ErrorReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) {
        if (client.getMatch().getMe().equals(errorPlayer)){
            client.getView().dealWithError(typeError);
        }
    }
}
