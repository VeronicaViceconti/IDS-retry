package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;

public class GameAlmostFinishedReply extends SampleServerMessage{

    private Player player;
    public GameAlmostFinishedReply(Player finishedPLayer){
        super(TypeMessageServer.GAME_ALMOST_DONE);
        player = finishedPLayer;
    }

    public void execute(Client client){
        if(player.equals(client.getMatch().getMe()))
        client.getView().gameAlmostFinished();
    }
}
