package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.network.Client;
/**
 * This class represents a message sent by the server to a client indicating that they are currently in a lobby waiting for other players to join the game.
 * It inherits from the `SampleServerMessage` class.
 */
public class WaitingPlayerReply extends SampleServerMessage {
    public WaitingPlayerReply(){
        super(TypeMessageServer.WAITING_FOR_OTHER_PLAYERS);
    }
    /**
     * This method defines the actions to be taken by the server after sending a WaitingPlayerReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) {
        System.out.println("You are in lobby, more players have to join the game...");
    }
}
