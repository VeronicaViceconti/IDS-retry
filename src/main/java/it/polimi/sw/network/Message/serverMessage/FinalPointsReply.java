package it.polimi.sw.network.Message.serverMessage;

import it.polimi.sw.model.Player;
import it.polimi.sw.network.Client;
/**
 * This class represents a message sent by the server to the client containing a player's final points at the end of the game.
 * It inherits from the `SampleServerMessage` class (likely containing common server message properties).
 *
 * This message informs the client about a specific player's final points and potentially updates the client-side view.
 */
public class FinalPointsReply extends SampleServerMessage{
        private final Player player;
        private final int points;
    /**
     * Constructor for a FinalPointsReply message.
     *
     * @param player1 The player whose final points are being sent.
     * @param point The final points earned by the player.
     */

        public FinalPointsReply(Player player1, int point){
            super(TypeMessageServer.FINAL_POINTS_REPLY);
            this.player = player1;
            this.points = point;
        }

    /**
     * This method defines the actions to be taken by the server after sending a FinalPointsReply message to a client.
     *
     * @param client The `Client` object representing the client that received the message.
     *
     * @Override
     * Specifies that this method overrides a method from a superclass (likely `SampleServerMessage`).
     */
    @Override
    public void execute(Client client) {
        if(client.getMatch().getMe().getNickName().equals(player.getNickName())){
            client.getMatch().getMe().addPoints(points);
            client.getView().chatWait();
            client.getView().addPoints(client.getMatch().getMe(), points);
            client.getView().chatUnblockWait();
        }
    }
}
