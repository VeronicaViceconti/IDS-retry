package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.network.Message.serverMessage.ChatReply;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;

import java.rmi.RemoteException;
/**
 * This class represents a message sent by the client requesting to broadcast a public chat message.
 * It inherits from the `SampleClientMessage` class (likely containing common message properties).
 *
 * This message includes the lobby ID, the player's ID, and the message content.
 */
public class PublicChatRequest extends SampleClientMessage {
    private final String mess;

    private final int fromWhom;

    private final int lobby;
    /**
     * Constructor for a PublicChatRequest message.
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param message The text content of the public chat message.
     * @param id_player The ID of the player sending the message.
     */
    public PublicChatRequest(int lobby, String message, int id_player){
            super(TypeMessageClient.PUBLIC_MESSAGE_REQUEST,lobby,id_player);
            this.mess = message;
            this.fromWhom = id_player;
            this.lobby = lobby;
    }
    /**
     * Retrieves the text content of the public chat message.
     *
     * @return The message string.
     */
    public String getMessage(){
            return mess;
    }
    /**
     * Composes a formatted message string combining the player's nickname and the message content (private method, not part of the public API).
     *
     * @param name The player's nickname.
     * @param message The message content.
     * @return The formatted message string (nickname + ": " + message).
     */
    private String composeMes(String name, String message){
            return name + ": " + message;
    }
    /**
     * This method defines the actions to be taken by the server upon receiving a PublicChatRequest message.
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this method overrides a method from the parent class (likely `SampleClientMessage`).
     */
    @Override
    public void execute(GameControllerServer gcs) {
        String nickname = gcs.currgame.getTotPlayers().stream()
                .filter(player -> player.getId() == fromWhom)
                .map(Player::getNickName)
                .findFirst()
                .orElse("Unknown");

        String combinedMessage = new String("Public message from "+nickname + ": "+mess);

        SampleServerMessage toEveryOne = new ChatReply(combinedMessage);

        gcs.currgame.notify(toEveryOne);
    }

}
