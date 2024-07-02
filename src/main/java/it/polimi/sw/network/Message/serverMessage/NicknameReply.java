package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.network.Client;

/**
 * This class represents a message sent by the server to the client in response to a nickname selection.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message is used to inform the client whether the chosen nickname is available or not. The client can then take appropriate actions based on the server's response (e.g., requesting a new nickname if unavailable).
 */
public class NicknameReply extends SampleServerMessage {
    private final boolean isavailableNickname;

    /**
     * Constructor for a NicknameReply message.
     *
     * @param avaliable A boolean indicating if the chosen nickname is available (true) or not (false).
     */
    public NicknameReply(boolean avaliable){
        super(TypeMessageServer.NICKNAME_REPLY);
        this.isavailableNickname = avaliable;
    }

    /**
     * This method is likely overridden for client-side processing of the message.
     *
     * @param client The `Client` object representing the client that received the message.
     */
    @Override
    public void execute(Client client) {

        client.getView().isNicknameAvailable(isavailableNickname);
    }
}
