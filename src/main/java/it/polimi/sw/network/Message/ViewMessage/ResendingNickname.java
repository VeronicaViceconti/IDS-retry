package it.polimi.sw.network.Message.ViewMessage;
/**
 * This class represents a message sent by the client-side view to the server requesting to resend the nickname.
 * It inherits from the `SampleViewMessage` class.
 *
 * This message is used when the initial nickname chosen by the player might have been rejected by the server (e.g., due to already existing nicknames). The client can then send a new nickname through this message.
 */
public class ResendingNickname extends SampleViewMessage{
    private String nicknname;

    /**
     * Constructor for a ResendingNickname message.
     *
     * @param nickname The new nickname chosen by the player after a potential rejection during initial setup.
     */
    public ResendingNickname(String nickname) {
        super(TypeMessageView.RESEND_NICKNAME);
        this.nicknname = nickname;
    }
    /**
     * Getter method to access the player's proposed nickname.
     *
     * @return The String containing the nickname the player wants to use.
     */
    public String getNicknname() {
        return nicknname;
    }
}
