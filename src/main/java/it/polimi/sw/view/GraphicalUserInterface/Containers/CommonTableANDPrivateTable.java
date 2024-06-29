package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class rapresent the left and right side of the second tab, includes private things and common table/objectives
 */
public class CommonTableANDPrivateTable extends JPanel {

    /**
     * Contans hand, objectives and common table
     */
    private HandObjectivesAndCommonTable myHandAndObjectivesAndCommonTable;

    private ManuscriptsMainPanel manuscript;
    //manca il manoscritto sulla dx

    /**
     * Constructor for the CommonTableANDPrivateTable.
     *
     * @param fdG The player's face-down goal card.
     * @param fdR The player's face-down resource card.
     * @param fuG The player's face-up goal cards.
     * @param fuR The player's face-up resource cards.
     * @param commonObj The ArrayList of objective cards shared by all players.
     */
    public CommonTableANDPrivateTable(Card fdG,Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,ArrayList<Objective> commonObj) {

        setLayout(new BorderLayout());
        //oggetto che contiene tutta la parte sx della finestra fissa
        myHandAndObjectivesAndCommonTable = new HandObjectivesAndCommonTable(fdG,fdR,fuG,fuR,commonObj);
        manuscript = new ManuscriptsMainPanel();

        add(myHandAndObjectivesAndCommonTable,BorderLayout.WEST);
        add(manuscript, BorderLayout.CENTER);
        setVisible(true);
    }
    /**
     * Getter method to access the inner HandObjectivesAndCommonTable panel.
     *
     * @return A reference to the HandObjectivesAndCommonTable object.
     */
    public HandObjectivesAndCommonTable getMyHandAndObjectivesAndCommonTable() {
        return myHandAndObjectivesAndCommonTable;
    }

    public ManuscriptsMainPanel getManuscript() {
        return manuscript;
    }
}
