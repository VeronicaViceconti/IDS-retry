package it.polimi.sw.view.GraphicalUserInterface.Containers;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import it.polimi.sw.model.Pion;

public class PlateauScorePanel extends JPanel {

    /**
     * The plateau rapresenting the points of the players
     */
    ScoreBoardPanel plateau;
    /**
     * The 4 resources + 3 objects in the game
     */
    ArrayList<JLabel> resObj;
    /**
     * Map of The 4 resources + 3 objects in the game and their JLabel
     */
    HashMap<String,JLabel> resAndObjMap;
    /**
     * Container for the quantity arraylist
     */
    JPanel containerQuantity;
    /**
     * Constraint used to manage GridBag layout
     */
    private GridBagConstraints gbc;

    /**
     * This class likely represents a panel that displays the score information for a game board (plateau) along with
     * objective cards (possibly used for tracking points or other game objectives).
     *
     * @param screenSize The dimension (width and height) of the panel.
     * @param pions An ArrayList containing information about the pions (playing pieces) on the board.
     */

    public PlateauScorePanel(Dimension screenSize,ArrayList<Pion> pions) {

        screenSize.width = (int) (screenSize.width - screenSize.width*0.7);
        screenSize.height = (int) (screenSize.height - screenSize.height*0.2);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),"Plateau Score", TitledBorder.CENTER,TitledBorder.TOP));

        resAndObjMap = new HashMap<>();
        resObj = new ArrayList<>();

        plateau = new ScoreBoardPanel(pions);

        add(plateau,BorderLayout.CENTER);
        setVisible(true);

    }

    /**
     * Method invocated at the beginning of the game, to initialise and create all the possible objects and resources
     * that he can obtain
     */
    public void setObjectsAndResources(){
        gbc = new GridBagConstraints();
        containerQuantity = new JPanel();
        containerQuantity.setLayout(new GridBagLayout());
        gbc.insets = new Insets(10, 7, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        String[] nomi = new String[]{"ANIMAL","FUNGI","PLANT","INSECT","QUILL","MANUSCRIPT","INKWELL"};

        String path = "src"+File.separator+"main"+File.separator+"resources"+ File.separator+"graphicalResources"+File.separator+"ResourcesAndObjects"+File.separator+"";
        for(int i=1;i<=7;i++){
            ImageIcon ic = new ImageIcon(path+String.format("%02d",i) +".PNG");
            Image img = ic.getImage().getScaledInstance(35,40,Image.SCALE_AREA_AVERAGING);
            ic.setImage(img);
            JLabel neew = new JLabel(ic);
            neew.setName(nomi[i-1]);
            resObj.add(neew);
            resAndObjMap.put(nomi[i-1],new JLabel("0"));
        }

        for(int i=0;i<7;i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridheight = 1;
            containerQuantity.add(resObj.get(i), gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.gridheight = 1;
            containerQuantity.add(resAndObjMap.get(nomi[i]), gbc);
        }

        add(containerQuantity,BorderLayout.EAST);
    }

    /**
     * Method invocated to update one or more resources/objects
     * @param numOfResourceAndObject key is the name of resource or object and the value is its quantity
     */
    public void updateObjectsAndResources(HashMap<String, Integer> numOfResourceAndObject){

        for (Map.Entry<String, Integer> entry:numOfResourceAndObject.entrySet()) {

            Integer points = entry.getValue();
            resAndObjMap.get(entry.getKey()).setText( points.toString() );
        }
    }

    /**
     *
     * @return scoreboardpanel
     */
    public ScoreBoardPanel getPlateau() {
        return plateau;
    }
}
