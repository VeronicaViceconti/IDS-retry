package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Pion;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class that is the first tab of the GUI, includes plateau and chat management
 */
public class PlateauAndChat extends JPanel {
    /**
     * Contains the points, resources and objects of the player
     */
    private PlateauScorePanel ps;
    private ChatPanel chatPanel;

    /**
     * Constructor for the PlateauAndChat panel.
     *
     * @param screenSize The dimension of the screen.
     * @param pions The ArrayList of Pion objects representing the game pieces.
     * @param gui A reference to the GUI object likely used for communication and updates.
     */
    public PlateauAndChat(Dimension screenSize, ArrayList<Pion> pions, GUI gui) {
        setSize(screenSize);
        setLayout(new BorderLayout());

        ps = new PlateauScorePanel(screenSize,pions);
        add(ps,BorderLayout.WEST);

        this.ps.setObjectsAndResources();
        //setVisible(true);


        chatPanel=new ChatPanel(gui);


        add(chatPanel,BorderLayout.CENTER);
        setVisible(true);
    }
    /**
     * Getter method to access the inner PlateauScorePanel object.
     *
     * @return A reference to the PlateauScorePanel object.
     */
    public PlateauScorePanel getPs() {
        return ps;
    }

    /**
     * Getter method to access the inner ChatPanel object.
     *
     * @return A reference to the ChatPanel object.
     */
    public ChatPanel getChatPanel() {
        return chatPanel;
    }
}
