package it.polimi.sw.network.Message.ViewMessage;
/**
 * This class represents a message sent by the client-side view to the server requesting to resend the nickname.
 * It inherits from the `SampleViewMessage` class.
 *
 * This message is used when the initial nickname chosen by the player might have been rejected by the server (e.g., due to already existing nicknames). The client can then send a new nickname through this message.
 */
public class DisconnectClientGameEnding extends SampleViewMessage{
    /**
     * Constructor for a ResendingNickname message.
     *
     */
    public DisconnectClientGameEnding() {
        super(TypeMessageView.DisconnectClientGameEnding);
    }
}
