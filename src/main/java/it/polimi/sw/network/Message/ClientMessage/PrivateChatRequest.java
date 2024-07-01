package it.polimi.sw.network.Message.ClientMessage;

import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Message.serverMessage.ChatReply;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;
import it.polimi.sw.network.RMI.ClientHandlerRMI;
import it.polimi.sw.network.Socket.ClientHandlerSOCKET;

import java.rmi.RemoteException;

/**
 * This class represents a message sent by the client requesting to send a private chat message to another player.
 * It inherits from the `SampleClientMessage` class.
 *
 * This message includes the lobby ID, the player's ID, the message content, and the recipient's nickname.
 */
public class PrivateChatRequest extends SampleClientMessage {
    private final String mess;
    private final String toWhom;

    private final int fromWhom;
    private ClientHandlerSOCKET clientHandlerSOCKET;
    private ClientHandlerRMI clientHandlerRMI;

    /**
     * Constructor for a PrivateChatRequest message.
     *
     * @param lobby The ID of the lobby the message is associated with.
     * @param message The text content of the private chat message.
     * @param Nickname The nickname of the player intended to receive the message.
     * @param id_player The ID of the player sending the message.
     */
    public PrivateChatRequest(int lobby,String message, String Nickname,int id_player, ClientHandlerRMI clientHandlerRMI){
        super(TypeMessageClient.PRIVATE_MESSAGE_REQUEST,lobby,id_player);
        this.mess = message;
        this.toWhom = Nickname;
        this.fromWhom = id_player;
        this.clientHandlerRMI=clientHandlerRMI;
        this.clientHandlerSOCKET=null;
    }

    public PrivateChatRequest(int lobby,String message, String Nickname,int id_player, ClientHandlerSOCKET clientHandlerSOCKET){
        super(TypeMessageClient.PRIVATE_MESSAGE_REQUEST,lobby,id_player);
        this.mess = message;
        this.toWhom = Nickname;
        this.fromWhom = id_player;
        this.clientHandlerSOCKET=clientHandlerSOCKET;
        this.clientHandlerRMI=null;
    }

    public PrivateChatRequest(int lobby,String message, String Nickname,int id_player){
        super(TypeMessageClient.PRIVATE_MESSAGE_REQUEST,lobby,id_player);
        this.mess = message;
        this.toWhom = Nickname;
        this.fromWhom = id_player;

    }


    public int getFromWhom() {
        return fromWhom;
    }

    /**
     * Retrieves the text content of the private chat message.
     *
     * @return The message string.
     */
    public String getMessage(){
        return mess;
    }

    /**
     * Retrieves the nickname of the player intended to receive the message.
     *
     * @return The recipient's nickname.
     */
    public String getToWhom(){
        return toWhom;
    }
    /**
     * This method defines the actions to be taken by the server upon receiving a PrivateChatRequest message.
     *
     * @param gcs The `GameControllerServer` object representing the server instance.
     *
     * @Override
     * Specifies that this method overrides a method from the parent class.
     */
    @Override
    public void execute(GameControllerServer gcs) {
        String nickname = gcs.currgame.getTotPlayers().stream()
                .filter(player -> player.getId() == fromWhom)
                .findFirst().get().getNickName();
        SampleServerMessage toOne = new ChatReply(toWhom,mess);

        if(clientHandlerRMI!= null){
            gcs.currgame.singleNotify(toOne, clientHandlerRMI);
        }else{
            gcs.currgame.singleNotify(toOne, clientHandlerSOCKET);
        }

    }
}
