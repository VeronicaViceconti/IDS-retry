package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class represent the container of the 2 components CommonTable and private things
 */
public class HandObjectivesAndCommonTable extends JPanel {
    /**
     * Contains the hand and objectives of the player
     */
    private HandAndObjectivesPanel ho;
    /**
     * common table of the game
     */
    private CommonTablePanel ct;

    private GridBagConstraints gbc;

    /**
     * Constructor for the HandObjectivesAndCommonTable.
     *
     * @param fdG Face-down goal card.
     * @param fdR Face-down resource card.
     * @param fuG Face-up goal cards.
     * @param fuR Face-up resource cards.
     * @param commonObj The ArrayList of objective cards shared by all players.
     */

    public HandObjectivesAndCommonTable(Card fdG,Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,ArrayList<Objective> commonObj) {

        setLayout(new GridBagLayout());

        ct = new CommonTablePanel(fdG,fdR,fuG,fuR,commonObj);
        ct.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,2),"COMMON TABLE", TitledBorder.CENTER,TitledBorder.TOP));


        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(ct,gbc);

        ho = new HandAndObjectivesPanel();
        setVisible(true);
    }

    /**
     * Creating the up left side of the main frame
     */
    public void addClientThings(){

        ho.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY,2),"YOUR THINGS", TitledBorder.CENTER,TitledBorder.TOP));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        add(ho,gbc);

        setVisible(true);
    }
    /**
     * Getter method to access the inner HandAndObjectivesPanel object.
     *
     * @return A reference to the HandAndObjectivesPanel object.
     */
    public HandAndObjectivesPanel getHo() {
        return ho;
    }

    /**
     * Getter method to access the inner CommonTablePanel object (likely null in this class).
     *
     * @return A reference to the CommonTablePanel object (might be null).
     */
    public CommonTablePanel getCt() {
        return ct;
    }
}
