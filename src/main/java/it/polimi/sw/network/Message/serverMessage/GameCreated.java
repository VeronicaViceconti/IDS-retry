package it.polimi.sw.network.Message.serverMessage;


import it.polimi.sw.controller.GameControllerServer;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
import it.polimi.sw.network.CommonGameLogicServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class represents a message sent by the server to the client informing them that a new game has been created.
 * It inherits from the `SampleServerMessage` class.
 *
 * This message provides information about the newly created game and potentially prompts the client for additional input.
 */

public class GameCreated extends SampleServerMessage {
    private int askOrNotNumPlayers;
    private ArrayList<Pion> availablePions;
    private int idlobby;
    private CommonGameLogicServer gameControllerServer;
    /**
     * Constructor for a GameCreated message.
     *
     * @param availablePions An ArrayList containing the available Pion (game piece) options for the client to choose from.
     * @param askOrNotNumPlayers An integer indicating whether to ask the client for the number of players (1) or not (0).
     * @param idlobby The ID of the lobby associated with the newly created game.
     */
    public GameCreated(ArrayList<Pion> availablePions,int askOrNotNumPlayers,int idlobby){

        super(TypeMessageServer.CREATED_NEW_GAME);
        this.availablePions = availablePions;
        this.askOrNotNumPlayers = askOrNotNumPlayers;
        this.idlobby = idlobby;
    }
    /**
     * This method defines the actions to be taken by the server after sending a GameCreated message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) {
        //set lobby for the client
        client.setIdlobby(idlobby);
        if (askOrNotNumPlayers == 1) {
            client.getView().selectNumberOfplayerInMatch();
        }
        client.getView().selectPion(this.availablePions);
    }


}

