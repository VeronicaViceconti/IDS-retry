package it.polimi.sw.network.Message.ViewMessage;

import it.polimi.sw.network.Message.ClientMessage.TypeMessageClient;

import java.io.Serializable;
import java.lang.reflect.Type;
/**
 * This abstract class represents a base class for messages sent from the client-side view to the server.
 * It likely defines common properties and functionalities for these messages.
 *
 * Clients using this framework likely inherit from this class to create specific message types
 * for different communication needs within the game.
 */
public abstract class SampleViewMessage  implements Serializable {

    private final TypeMessageView type;
    /**
     * Constructor for a SampleViewMessage.
     *
     * @param type The `TypeMessageView` enumeration value specifying the type of message.
     */
    public SampleViewMessage(TypeMessageView type) {
        this.type = type;
    }
    /**
     * Getter method to access the message type.
     *
     * @return The `TypeMessageView` enumeration value representing the message type.
     */
    public TypeMessageView getType(){
        return type;
    }
}
