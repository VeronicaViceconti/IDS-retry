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
        //a sx metto obiettivo privato e in centro metto la mano del giocatore
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

        //gestione immagine OBIETTIVO PRIVATO

        //creazione immagine
        ImageIcon objImage = new ImageIcon(path + String.format("%03d", obj.getCondition().getId()) +".png");
        Image img = objImage.getImage().getScaledInstance(widthImage,heightImage,Image.SCALE_SMOOTH);
        objImage.setImage(img);

        //creo label con l'immagine appena creata e la aggiungo nel gridBagLayout di output
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

        //gestione immagini
        Image img;
        String side;
        if(handd.size() == 1) { //inizio del gioco
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

        }else { //qualsiasi hand dopo il primo giro con la initialCard
            for (Card c : handd) {
                //assegno il giusto path
                path = "src/main/resources/graphicalResources/"+c.getClass().getName().substring(19);
                if(c.getSide() == 1) {
                    path += "Front/";
                    side = "1";
                }else {
                    path += "Back/";
                    side = "2";
                }
                //inserisco le immagini
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
        //ho l'array delle carte, le metto nel grid pane
        for(int i=0;i<hand.size();i++){
            gbc.gridy = 1;
            gbc.gridx = i;
            add(hand.get(i),gbc);
        }

        //riaggiorno la view di questo elemento altrimenti non viene visto nulla
        revalidate();
        repaint();

        setVisible(true);
    }

    //quando si apre la finestra di inserimento input, devo visualizzare le immagini, ma devo farne una copia
    //da quelle presenti nella mano del giocatore perchè in GUI ogni elemento ha un solo contenitore
    //non può essere in 2 diversi

    /**
     * Used to clone the hand in order to have 2 equals hand in the gui
     * @return the cloned array
     */
    public ArrayList<JLabel> cloneJLabelArray() {
        ArrayList<JLabel> clonedLabels = new ArrayList<>();
        for (JLabel originalLabel : hand) {
            // Ottieni l'ImageIcon dal JLabel originale
            Icon icon = originalLabel.getIcon();

            // Crea un nuovo JLabel con la stessa ImageIcon
            JLabel clonedLabel = new JLabel(icon);
            clonedLabel.setName(originalLabel.getName());

            // Opzionalmente, copia altre proprietà del JLabel originale
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
            // Rimuovi tutti i componenti della hand dal pannello
            remove(label);
        }

        //rimuovo dalle carte visualizzabili
        hand.remove(index);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Aggiungi nuovamente le carte rimanenti nell'ordine corretto
        for(int i=0;i<hand.size();i++){
            gbc.gridy = 1;
            gbc.gridx = i;
            add(hand.get(i), gbc);
        }

        revalidate();
        repaint();
    }

    //elimino le carte già esistenti e le reinserisco

    /**
     * Update the hand of the player
     * @param cards the hand of the player
     */
    public void updateHand(ArrayList<Card> cards){
        if(!hand.isEmpty()){
            for (JLabel label : hand) {
                // Rimuovi tutti i componenti della hand dal pannello
                remove(label);
            }
            hand.clear();
        }
        //aggiungo le carte nella mano
        addHand(cards);
    }

    //metodo chiamato quando è appena iniziato il turno del giocatore, deve comparire un bottone I'M READY
    //che se cliccato indica lo stop della flip card e l'inizio della selectCardToPut

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
            //elimino dalla visualizzazione
            Container parent = confirmButton.getParent();
            if (parent != null) {
                parent.remove(confirmButton);
                parent.revalidate();
                parent.repaint();
            }
        });

        gbc.insets = new Insets(10, 20, 15, 0); // margini intorno ai componenti

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
        //bottone per flippare una carta
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
