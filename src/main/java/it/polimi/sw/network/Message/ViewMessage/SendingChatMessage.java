package it.polimi.sw.network.Message.ViewMessage;
/**
 * This class represents a message sent by the client-side view to the server containing a chat message from the player.
 * It inherits from the `SampleViewMessage` class.
 *
 * This message is used to send chat messages to the server, which might then be broadcast to all players (public chat) or sent to a specific recipient (private chat).
 */
public class SendingChatMessage extends SampleViewMessage{
    private final String playerMessage;

    private String toOne;
    /**
     * Constructor for a SendingChatMessage message (public chat).
     *
     * @param mex The message content sent by the player.
     */
    public  SendingChatMessage (String mex){
        super(TypeMessageView.CHAT_REQUEST);
        this.playerMessage = mex;
    }
    /**
     * Constructor for a SendingChatMessage message (private chat).
     *
     * @param mex The message content sent by the player.
     * @param destination The username of the recipient (optional for private messaging).
     */
    public  SendingChatMessage (String mex, String destination){
        super(TypeMessageView.CHAT_REQUEST);
        this.playerMessage = mex;
        this.toOne = destination;
    }
    /**
     * Getter method to access the player's chat message.
     *
     * @return The String containing the chat message content.
     */

    public String getPlayerMessage(){
        return playerMessage;
    }
    /**
     * Getter method to access the recipient username (private chat only).
     *
     * @return The String containing the username of the intended recipient (null for public chat).
     */
    public String getToOne(){
        return toOne;
    }
    /**
     * Method to determine if the message is public or private.
     *
     * @return True if the 'toOne' field is null (public chat), False otherwise (private chat).
     */
    public boolean isPublic(){return toOne == null;}

}
