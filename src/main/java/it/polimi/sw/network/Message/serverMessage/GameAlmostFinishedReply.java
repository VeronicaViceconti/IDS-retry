package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.network.Client;

public class GameAlmostFinishedReply extends SampleServerMessage{
    public GameAlmostFinishedReply(){
        super(TypeMessageServer.GAME_ALMOST_DONE);
    }

    public void execute(Client client){
        client.getView().gameAlmostFinished();
    }
}
