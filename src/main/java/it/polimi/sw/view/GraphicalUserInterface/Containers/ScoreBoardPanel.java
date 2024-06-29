package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Pion;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoardPanel extends JPanel {
    /**
     * Image of the score board
     */
    private JLabel boardImage;
    /**
     * map that creates correspondences between points and coordinates in the board game
     */
    private HashMap<Integer, Point> relativePositionMap;
    /**
     * map that includes each player and his points
     */
    private HashMap<Pion, Integer> playerPositions;

    //BLUE -> basso dx, GREEN -> basso sx, YELLOW ->  alto sx , RED -> alto dx
    /**
     * The images of the pions
     */
    private ArrayList<JLabel> pions;
    /**
     * the offset between the different pion in order to visualize them clearly in the board game
     */
    private HashMap<Pion,Point> offset;
    /**
     * Width of the board game
     */
    private int width = 388;
    /**
     * Height of the board game
     */
    private int height = 771;

    /**
     * Constructor for the ScoreBoardPanel.
     *
     * @param pions The ArrayList of Pion objects representing the game pieces.
     */
    public ScoreBoardPanel(ArrayList<Pion> pions) {
        // Carico l'immagine del tabellone
        setPreferredSize(new Dimension(width,height)); //dimensioni del tabellone
        ImageIcon handImage = new ImageIcon("src"+ File.separator+"main"+File.separator+"resources"+ File.separator+"graphicalResources"+ File.separator+"pointBoard"+ File.separator+"PLATEAU-SCORE-IMP.PNG");
        Image img = handImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        handImage.setImage(img);
        boardImage = new JLabel(handImage);
        boardImage.setLayout(null);
        boardImage.setBounds(0, 0, width, height);

        setLayout(null);
        add(boardImage);

        // Inizializzo la mappa delle coordinate relative
        relativePositionMap = new HashMap<>();
        initializeRelativePositionMap();

        // Inizializzo le posizioni dei giocatori
        playerPositions = new HashMap<>();
        this.pions = new ArrayList<>();

        offset = new HashMap<>();
        offset.put(Pion.pion_blue,new Point(0,0));
        offset.put(Pion.pion_green,new Point(-20,-5));
        offset.put(Pion.pion_red, new Point(10,-30));
        offset.put(Pion.pion_yellow, new Point(-15,-30));
    }

    /**
     * Method that creates and adds the pions in the board game
     * @param pionList the pions of the players
     */
    public void initializePions(ArrayList<Pion> pionList){
        //posizione iniziale
        Point relativePoint = relativePositionMap.get(0);
        for (Pion pion:pionList) {
            //carico immagine pion e la aggiungo all'array
            ImageIcon handImages = new ImageIcon("src"+File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"pions"+File.separator+""+pion+".png");
            Image img = handImages.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            handImages.setImage(img);
            pions.add(new JLabel(handImages));

            if (relativePoint != null) {
                //coordinate di dove inserire il pion nel tabellone
                int baseX = relativePoint.x + offset.get(pion).x;
                int baseY = relativePoint.y + offset.get(pion).y;
                // Crea e aggiungi un JLabel per il pion del giocatore
                JLabel playerToken = pions.getLast();
                playerToken.setName(pion.toString());
                playerToken.setSize(20, 20); // Imposta la dimensione del pion
                playerToken.setLocation(baseX, baseY); // Imposta la posizione del pion
                boardImage.add(playerToken); // Aggiunge il pion al JLabel boardImage
            }
        }
        repaint();

    }

    /**
     * Initialize the coordinates of each point in the board game to set the pions when needed
     */
    private void initializeRelativePositionMap() {
        // Aggiungi le coordinate relative per ogni posizione sul tabellone

        relativePositionMap.put(0, new Point(100, 728));
        relativePositionMap.put(1, new Point(180, 728));
        relativePositionMap.put(2, new Point(270, 728));
        relativePositionMap.put(3, new Point(330, 640));
        relativePositionMap.put(4, new Point(240, 640));
        relativePositionMap.put(5, new Point(145, 640));
        relativePositionMap.put(6, new Point(55, 640));
        relativePositionMap.put(7, new Point(55, 560));
        relativePositionMap.put(8, new Point(145, 560));
        relativePositionMap.put(9, new Point(240, 560));
        relativePositionMap.put(10, new Point(330, 560));
        relativePositionMap.put(11, new Point(330, 475));
        relativePositionMap.put(12, new Point(240, 475));
        relativePositionMap.put(13, new Point(145, 475));
        relativePositionMap.put(14, new Point(55, 475));
        relativePositionMap.put(15, new Point(55, 390));
        relativePositionMap.put(16, new Point(145, 390));
        relativePositionMap.put(17, new Point(240, 390));
        relativePositionMap.put(18, new Point(330, 390));
        relativePositionMap.put(19, new Point(330, 310));
        relativePositionMap.put(20, new Point(195, 270));
        relativePositionMap.put(21, new Point(55, 310));
        relativePositionMap.put(22, new Point(55, 225));
        relativePositionMap.put(23, new Point(55, 145));
        relativePositionMap.put(24, new Point(110, 75));
        relativePositionMap.put(25, new Point(200, 60));
        relativePositionMap.put(26, new Point(270, 75));
        relativePositionMap.put(27, new Point(330, 150));
        relativePositionMap.put(28, new Point(330, 225));
        relativePositionMap.put(29, new Point(195, 160));
    }

    /**
     * Update the pion position of the player
     * @param pion the pion of the player
     * @param position the points of the player
     */
    public void updatePlayerPosition(Pion pion, int position) {
        if(position > 29) {//devo tornare all'inizio del tabellone
            position = ( position - 29 ) - 1; //conta anche lo zero!
        }
        playerPositions.put(pion, position);
        addPlayerToken(pion,position);
    }

    /**
     * Add or update the pion on the board game
     * @param pion the pion of the player
     * @param position the points of the player
     */
    private void addPlayerToken(Pion pion, int position) {
        Point relativePoint = relativePositionMap.get(position);
        if (relativePoint != null) {
            int baseX = relativePoint.x + offset.get(pion).x;
            int baseY = relativePoint.y + offset.get(pion).y;
            // Crea e aggiungi un JLabel per il pion del giocatore
            JLabel playerToken;
            for(int i=0;i<pions.size();i++){
                System.out.println(pion.toString());
                if(pions.get(i).getName().equalsIgnoreCase(pion.toString())){
                    playerToken = pions.get(i);
                    playerToken.setSize(20, 20); // Imposta la dimensione del pion
                    playerToken.setLocation(baseX, baseY); // Imposta la posizione del pion
                    boardImage.add(playerToken); // Aggiunge il pion al JLabel boardImage
                }
            }

        }
        revalidate();
        repaint();
    }
}
