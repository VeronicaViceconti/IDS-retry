package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;

import java.util.ArrayList;
import java.util.List;
//is it even used? delete?
public class HandReply extends SampleServerMessage {
    private final ArrayList<Card> hand;
    private final Player player;

    public HandReply(ArrayList<Card> cards, Player pl) {
        super(TypeMessageServer.HAND_REPLY);
        this.hand = cards;
        this.player = pl;
    }


    @Override
    public void execute(Client client) {
        for (Player p : client.getMatch().getTotPlayers()) {
            if (player.equals(p)) {
                p.setHand(hand);
            }
        }
        if (player.equals(client.getMatch().getMe())) {
            client.getView().showPlayerHand(hand);
        }
    }
}
