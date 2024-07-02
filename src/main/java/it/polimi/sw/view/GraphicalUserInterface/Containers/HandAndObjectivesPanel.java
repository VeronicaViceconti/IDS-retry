package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

//import static sun.tools.jconsole.inspector.XDataViewer.dispose;

/**
 * This class represent one component of the left side of the second tab, includes hand and private objectivs
 */
public class HandAndObjectivesPanel extends JPanel {
    /**
     * Width of the image
     */
    private static final int  widthImage = 120;
    /**
     * Height of the image
     */
    private static final int  heightImage = 100;
    /**
     * Label with the image of the private objective
     */
    private JLabel privObj;
    /**
     * Labels with the images of the hand's card
     */
    private ArrayList<JLabel> hand;
    private GridBagConstraints gbc;

    /**
     * Constructor for the HandAndObjectivesPanel.
     */
    public HandAndObjectivesPanel(){
        setLayout(new GridBagLayout());

        hand = new ArrayList<>();
        gbc = new GridBagConstraints();
        setVisible(true);

    }

    /**
     *
     * @param obj the objective to add to the player
     * @param mainFrame the main frame of the gui
     */
    public void addPrivObj(Objective obj, JFrame mainFrame){
        JLabel objDescription = new JLabel("Private objective: ");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(objDescription,gbc);

        String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"objectiveCardFront"+File.separator+"";


        ImageIcon objImage = new ImageIcon(path + String.format("%03d", obj.getCondition().getId()) +".png");
        Image img = objImage.getImage().getScaledInstance(widthImage,heightImage,Image.SCALE_SMOOTH);
        objImage.setImage(img);


        privObj = new JLabel(objImage);
        gbc.insets = new Insets(30, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(privObj,gbc);

        revalidate();
        repaint();
        setVisible(true);
    }

    /**
     * Add the hand of the player to the gui
     * @param handd the hand of the player
     */
    public void addHand(ArrayList<Card> handd) {

        JLabel handDescription = new JLabel("Your hand:");

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        add(handDescription,gbc);

        String path;


        Image img;
        String side;
        if(handd.size() == 1) {
            if (handd.getFirst().getClass().getName().substring(19).equalsIgnoreCase("InitialCard")) {
                if(handd.getFirst().getSide() == 1){
                    path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"InitialCardFront"+File.separator+"";
                    side = "1";
                }else{ path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"InitialCardBack"+File.separator+"";
                    side = "2";
                }
                ImageIcon handImage = new ImageIcon(path + String.format("%03d", handd.getFirst().getId()) + ".png");
                img = handImage.getImage().getScaledInstance(widthImage, heightImage, Image.SCALE_SMOOTH);
                handImage.setImage(img);
                JLabel jl = new JLabel(handImage);
                jl.setPreferredSize(new Dimension(120,100));
                jl.setName(side);
                hand.add(jl);
            }

        }else {
            for (Card c : handd) {

                path = "src/main/resources/graphicalResources/"+c.getClass().getName().substring(19);
                if(c.getSide() == 1) {
                    path += "Front/";
                    side = "1";
                }else {
                    path += "Back/";
                    side = "2";
                }

                ImageIcon handImage = new ImageIcon(path + String.format("%03d", c.getId()) + ".png");
                img = handImage.getImage().getScaledInstance(widthImage, heightImage, Image.SCALE_SMOOTH);
                handImage.setImage(img);
                JLabel jl = new JLabel(handImage);
                jl.setName(side);
                hand.add(jl);
            }
        }
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        for(int i=0;i<hand.size();i++){
            gbc.gridy = 1;
            gbc.gridx = i;
            add(hand.get(i),gbc);
        }


        revalidate();
        repaint();

        setVisible(true);
    }



    /**
     * Used to clone the hand in order to have 2 equals hand in the gui
     * @return the cloned array
     */
    public ArrayList<JLabel> cloneJLabelArray() {
        ArrayList<JLabel> clonedLabels = new ArrayList<>();
        for (JLabel originalLabel : hand) {

            Icon icon = originalLabel.getIcon();


            JLabel clonedLabel = new JLabel(icon);
            clonedLabel.setName(originalLabel.getName());


            clonedLabel.setText(originalLabel.getText());
            clonedLabel.setHorizontalAlignment(originalLabel.getHorizontalAlignment());
            clonedLabel.setVerticalAlignment(originalLabel.getVerticalAlignment());
            clonedLabel.setBorder(originalLabel.getBorder());

            clonedLabels.add(clonedLabel);
        }
        return clonedLabels;
    }

    /**
     * Getter method to access the list of JLabels representing the player's hand in the user interface.
     *
     * @return An ArrayList containing the JLabels for the hand elements.
     */
    public ArrayList<JLabel> getHand() {
        return hand;
    }

    /**
     * Remove one specific card from the hand of the player and re-order the hand
     * @param index which card to remove
     */
    public void removeCardFromHand(int index) {

        for (JLabel label : hand) {

            remove(label);
        }


        hand.remove(index);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;


        for(int i=0;i<hand.size();i++){
            gbc.gridy = 1;
            gbc.gridx = i;
            add(hand.get(i), gbc);
        }

        revalidate();
        repaint();
    }



    /**
     * Update the hand of the player
     * @param cards the hand of the player
     */
    public void updateHand(ArrayList<Card> cards){
        if(!hand.isEmpty()){
            for (JLabel label : hand) {

                remove(label);
            }
            hand.clear();
        }

        addHand(cards);
    }



    /**
     *
     * @param gui refers to the gui in order to invocate a specific method when 'I'm ready button' is clicked
     */
    public void addReadyButton(GUI gui) {

        JButton confirmButton = new JButton("I'm ready!");
        confirmButton.setMargin(new Insets(10,10,10,10));
        confirmButton.setBackground(Color.decode("#d6c45e"));

        confirmButton.addActionListener(e -> {
            try {
                gui.readyButtonClicked(hand.size());

            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            Container parent = confirmButton.getParent();
            if (parent != null) {
                parent.remove(confirmButton);
                parent.revalidate();
                parent.repaint();
            }
        });

        gbc.insets = new Insets(10, 20, 15, 0);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(confirmButton, gbc);

        revalidate();
        repaint();
    }

    /**
     * Method that add a button to allow the player to flip the cards whenever he wants
     * @param mainframe the main frame of the gui
     * @param gui refers to the gui in order to invocate a specific method when 'Flip a card' button is clicked
     */
    public void addFlipButton(JFrame mainframe, GUI gui) {

        JButton confirmButton = new JButton("Flip cards");
        confirmButton.setMargin(new Insets(10,10,10,10));
        confirmButton.setBackground(Color.decode("#d6c45e"));

        confirmButton.addActionListener(e -> {
            try {
                gui.cardToFlip(hand.get(0).getName());
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        gbc.insets = new Insets(10, 10, 15, 0); // margini intorno ai componenti

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(confirmButton, gbc);
    }
}
