package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * This class represent one component of the left side of the second tab, includes the 2 down and up decks + common objectives
 */
public class CommonTablePanel extends JPanel {
    /**
     * the width of the image
     */
    private static final int  widthImage = 120;
    /**
     * the height of the image
     */
    private static final int  heightImage = 100;
    /**
     * images of the gold front cards
     */
    ArrayList<JLabel> fuGLabel;
    /**
     * images of the resource front cards
     */
    ArrayList<JLabel> fuRLabel;
    /**
     * image of the gold front card
     */
    JLabel fdGLabel;
    /**
     * images of the resource front card
     */
    JLabel fdRLabel;

    /**
     * 2 images of the Common objectives
     */
    ArrayList<JLabel> commonObs;

    GridBagConstraints gbc;

    /**
     * Constructor for the CommonTablePanel.
     *
     * @param fdG Face-down goal card (likely not displayed here).
     * @param fdR Face-down resource card (likely not displayed here).
     * @param fuG Face-up goal cards (likely not displayed here).
     * @param fuR Face-up resource cards (likely not displayed here).
     * @param commonObj The ArrayList of objective cards shared by all players.
     */

    public CommonTablePanel(Card fdG, Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,ArrayList<Objective> commonObj){
        commonObs = new ArrayList<>();

        gbc = new GridBagConstraints();

        setLayout(new GridBagLayout());

        gbc.insets = new Insets(0, 0, 0, 40);
        gbc.anchor = GridBagConstraints.NORTH;
        addCommonObjectives(commonObj);

        addCommonTable(fdG,fdR,fuG,fuR);
    }

    /**
     * Creates the common table
     * @param fdG face down gold
     * @param fdR face down resource
     * @param fuG face up golds
     * @param fuR face up resources
     */
    public void addCommonTable(Card fdG, Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR){
        String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"GoldCardBack"+File.separator+"";

        JLabel cardDescription = new JLabel("Decks: ");
        gbc.insets = new Insets(15, 0, 5, 40);
        gbc.gridy = 1;
        gbc.gridx = 0;

        gbc.anchor = GridBagConstraints.LINE_START;
        add(cardDescription,gbc);




        ImageIcon fdGImage = new ImageIcon(path + String.format("%03d", fdG.getId()) +".png");
        Image img = fdGImage.getImage().getScaledInstance(widthImage,heightImage,Image.SCALE_SMOOTH);
        fdGImage.setImage(img);


        fdGLabel = new JLabel(fdGImage);
        fdGLabel.setPreferredSize(new Dimension(120,100));
        gbc.insets = new Insets(0, 10, 10, 40);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(fdGLabel,gbc);



        path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"GoldCardFront"+File.separator+"";
        gbc.insets = new Insets(0, 10, 10, 10);
        fuGLabel = new ArrayList<>();

        for (Card c:fuG) {
            ImageIcon fuGImage = new ImageIcon(path + String.format("%03d", c.getId())+".png");
            img = fuGImage.getImage().getScaledInstance(widthImage,heightImage,Image.SCALE_SMOOTH);
            fuGImage.setImage(img);
            JLabel label = new JLabel(fuGImage);
            fuGLabel.add(label);
        }


        for(int i=0;i<fuGLabel.size();i++){

            gbc.gridy = 2;
            gbc.gridx = i+1;

            add(fuGLabel.get(i),gbc);
        }


        path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"ResourceCardBack"+File.separator+"";
        ImageIcon fdRImage = new ImageIcon(path + String.format("%03d", fdR.getId()) +".png");
        img = fdRImage.getImage().getScaledInstance(widthImage,heightImage,Image.SCALE_SMOOTH);
        fdRImage.setImage(img);

        fdRLabel = new JLabel(fdRImage);
        fdRLabel.setPreferredSize(new Dimension(120,100));
        gbc.insets = new Insets(10, 10, 10, 40);
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(fdRLabel,gbc);


        fuRLabel = new ArrayList<>();
        gbc.insets = new Insets(10, 10, 10, 10);
        path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"ResourceCardFront"+File.separator+"";

        for (Card c:fuR) {
            ImageIcon fuRImage = new ImageIcon(path + String.format("%03d", c.getId())+".png");
            img = fuRImage.getImage().getScaledInstance(widthImage,heightImage,Image.SCALE_SMOOTH);
            fuRImage.setImage(img);
            JLabel label = new JLabel(fuRImage);
            label.setPreferredSize(new Dimension(120,100));
            fuRLabel.add(label);
        }

        for(int i=0;i<fuRLabel.size();i++){
            gbc.gridy = 3;
            gbc.gridx = i+1;
            add(fuRLabel.get(i),gbc);
        }

        setVisible(true);
        revalidate();
        repaint();
    }


    /**
     * Add the common objectives to the gui
     * @param commonObj the 2 common objectives
     */
    public void addCommonObjectives(ArrayList<Objective> commonObj){
        JLabel objDescription = new JLabel("Common objectives: ");
        String path = "src"+File.separator+"main"+File.separator+"resources"+ File.separator+"graphicalResources"+File.separator+"objectiveCardFront"+File.separator+"";




        Image img;

        for (Objective c:commonObj) {
            ImageIcon commonObjImage = new ImageIcon(path + String.format("%03d", c.getCondition().getId())+".png");
            img = commonObjImage.getImage().getScaledInstance(widthImage,heightImage,Image.SCALE_SMOOTH);
            commonObjImage.setImage(img);

            commonObs.add(new JLabel(commonObjImage));
        }

        gbc.insets = new Insets(10,0,0,0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(objDescription,gbc);


        for(int i=0;i<commonObs.size();i++){
            gbc.gridy = 0;
            gbc.gridx = i+1;

            add(commonObs.get(i),gbc);
        }

    }

    /**
     *
     * @param whichDeck which deck where to remove the card
     * @param position position where to remove the card
     */
    public void removeCardFromFaceUpDeck(int whichDeck, int position) {

        gbc.anchor = GridBagConstraints.CENTER;

        if(whichDeck == 1){
            gbc.insets = new Insets(0, 10, 10, 10);
            for (JLabel label : this.fuGLabel) {

                remove(label);
            }

            fuGLabel.remove(position);


            for(int i=0;i<fuGLabel.size();i++){
                gbc.gridy = 2;
                gbc.gridx = i+1;
                add(fuGLabel.get(i), gbc);
            }
        }else{
            gbc.insets = new Insets(10, 10, 10, 10);
            for (JLabel label : this.fuRLabel) {

                remove(label);
            }

            fuRLabel.remove(position);


            for(int i=0;i<fuRLabel.size();i++){
                gbc.gridy = 3;
                gbc.gridx = i+1;
                add(fuRLabel.get(i), gbc);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Update the common table
     * @param faceDownGold face down gold
     * @param faceDownResource face down resource
     * @param faceupGold face up golds
     * @param faceupResource face up resources
     */
    public void updateCommonTable(Card faceDownGold, Card faceDownResource, ArrayList<Card> faceupGold, ArrayList<Card> faceupResource){

        for (JLabel label : fuGLabel) {
            remove(label);
        }
        for (JLabel label : fuRLabel) {
            remove(label);
        }
        fuRLabel.clear();
        fuGLabel.clear();

        remove(fdGLabel);
        remove(fdRLabel);
        fdGLabel = null;
        fdRLabel = null;

        addCommonTable(faceDownGold,faceDownResource,faceupGold,faceupResource);
    }
}
