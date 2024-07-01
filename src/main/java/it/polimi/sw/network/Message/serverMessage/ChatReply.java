package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;

import java.rmi.RemoteException;
/**
 * This class represents a message sent by the server to the client containing a chat message.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message can be used to send chat messages to a specific player or to the entire game chat.
 *
 * @throws RemoteException Indicates a potential exception if remote communication fails.
 */
public class ChatReply extends SampleServerMessage{
    private final String message;
    private final String destination;
    private final String fromWhom;

    /**
     * Constructor for a ChatReply message to be broadcast to the entire game chat.
     *
     * @param mex The chat message content.
     */
    public ChatReply (String mex){
        super(TypeMessageServer.CHAT_REPLY);
        this.message = mex;
        this.destination = null;
        this.fromWhom = null;
    }
    /**
     * Constructor for a ChatReply message to be sent to a specific player.
     *
     * @param name The nickname of the player to receive the message.
     * @param mex The chat message content.
     */
    public ChatReply (String fromWhom, String name, String mex){
        super(TypeMessageServer.CHAT_REPLY);
        this.message = mex;
        this.destination = name;
        this.fromWhom = fromWhom;
    }

    public String getMessage() {
        return message;
    }

    public String getDestination() {
        return destination;
    }

    /**
     * This method defines the actions to be taken by the server after sending a ChatReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) throws RemoteException {
        if(destination == null){
            System.out.println("Dovrebbe aggiornare il mex con->"+message);
            client.getView().showGameChat(message);
        }
        else{
            System.out.println("IN EXECUTE CHATREPLY ELSE->dest: "+destination+",mex "+message+" "+",me "+client.getMatch().getMe().getNickName());
            for (Player p : client.getMatch().getTotPlayers()) {

                if(destination.trim().equalsIgnoreCase(p.getNickName())){ //it is for you
                    System.out.println("IN EXECUTE CHATREPLY ELSE TROVATO TIZIO destinatario ->"+destination);
                    client.getView().showGameChat("Private message from "+fromWhom+ ": " +message);
                }
            }
        }
    }
}
