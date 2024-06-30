package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Objective;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageSelectionDialog extends JDialog {

    /**
     * Width of the image
     */
    private static final int  widthImage = 150;
    /**
     * Height of the image
     */
    private static final int  heightImage = 130;

    /**
     * The side of the card, 1 = front, 2 = back
     */
    private String side;
    private JLabel cardChosen;
    private static GridBagConstraints gbc;

    /**
     * Mainframe
     */
    private JFrame MF;
    /**
     * card chosen
     */
    private int chosen;

    /**
     * x and y coordinates of the card chosen
     */
    private Integer[] coord = new Integer[2];

    /**
     * Constructor for the ImageSelectionDialog.
     *
     * @param mainFrame The reference to the main game frame.
     * @param one The first Objective card option to be displayed.
     * @param two The second Objective card option to be displayed.
     */
    public ImageSelectionDialog(JFrame mainFrame, Objective one, Objective two) {

        super(mainFrame, "Select your private objective", true);
        this.MF = mainFrame;

        chooseDimension(false);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40); // margini intorno ai componenti

        // Pannello per le immagini
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridBagLayout());
        String path = "src"+ File.separator+"main"+File.separator+"resources"+File.separator+"graphicalResources"+File.separator+"objectiveCardFront"+File.separator+"";

        // Prima immagine
        ImageIcon image1 = new ImageIcon(path+String.format("%03d", one.getCondition().getId()) +".png");
        Image scaledImage1 = image1.getImage().getScaledInstance(widthImage, heightImage, Image.SCALE_SMOOTH);
        JLabel imageLabel1 = new JLabel(new ImageIcon(scaledImage1));

        // Seconda immagine
        ImageIcon image2 = new ImageIcon(path+String.format("%03d", two.getCondition().getId()) +".png");
        Image scaledImage2 = image2.getImage().getScaledInstance(widthImage, heightImage, Image.SCALE_SMOOTH);
        JLabel imageLabel2 = new JLabel(new ImageIcon(scaledImage2));

        //inserimento immagini nella sottogriglia
        gbc.gridx = 0;
        gbc.gridy = 0;
        imagePanel.add(imageLabel1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        imagePanel.add(imageLabel2,gbc);

        //inserimento della griglia delle immagini, all'interno della griglia esterna che contiene gli altri elementi
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(imagePanel, gbc); // Aggiungi il pannello delle immagini

        // Campo di input per la scelta
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        // Bottone di conferma
        JButton confirmButton = new JButton("Send");

        ArrayList<Objective> objs = new ArrayList<>();
        objs.add(one);
        objs.add(two);
        // JPANEL settato per richiedere la carta della mano
        setJPanel(inputPanel,confirmButton,2,objs,null,"Write one or two to select the objective you want: ",null,"Not valid input. Insert 1 or 2.");

    }

    /**
     * Constructor for the ImageSelectionDialog.
     *
     * @param mainFrame The reference to the main game frame.
     * @param hand The ArrayList of JLabels representing the player's hand elements (cards).
     * @param type The type of information needed in addition to the card selection ("Coordinates" or other).
     * @param avPos An ArrayList of Point2D objects (potentially used for coordinate input).
     * @param close Whether the dialog should close after user selection (close = true).
     */
    public ImageSelectionDialog(JFrame mainFrame, ArrayList<JLabel> hand, String type, ArrayList<Point2D> avPos, boolean close) {
        super(mainFrame, "Play card!", true);
        chooseDimension(close);

        this.MF = mainFrame;

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // margini intorno ai componenti

        // Pannello per le immagini
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridBagLayout());

        for(int i=0;i<hand.size();i++){
            gbc.gridy = 0;
            gbc.gridx = i;
            imagePanel.add(hand.get(i),gbc);
        }

        //inserimento della griglia delle immagini, all'interno della griglia esterna che contiene gli altri elementi
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(imagePanel, gbc); // Aggiungi il pannello delle immagini

        // Campo di input per la scelta
        JPanel inputPanel = new JPanel();

        // Bottone di conferma
        JButton confirmButton = new JButton("Play!");

        if(type.equalsIgnoreCase("Coordinates")) {
            setJPanel(inputPanel, confirmButton, hand.size(), null,hand, "Write the card [x,y] coordinates of where to put it: ",avPos, "Not valid input.Retry!");
        }else
            // JPANEL settato per richiedere la carta della mano
            setJPanel(inputPanel,confirmButton,hand.size(),null,hand,"Write a number from 1 to "+hand.size()+" to select the card you want to play: ",null,"Not valid input. Insert a number from 1 to "+hand.size()+".");

    }

    /**
     * Method to set the dimension of the window
     * @param close indicates if the window can be close or not
     */
    private void chooseDimension(boolean close){
        setSize(500, 500);
        setLayout(new GridBagLayout());
        if(!close)
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //pezzo di codice per inserire la nuova finestra al centro dello schermo
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    /**
     * Getter method to access the selected card's side (e.g., "West").
     *
     * @return The String value representing the chosen card's side.
     */
    public String getSide() {
        return side;
    }

    /**
     * Getter method to access the chosen card's index within the hand (1-based).
     *
     * @return The integer value representing the chosen card's index (1-based).
     */
    public int getChosen() {
        return chosen;
    }

    public JLabel getCardChosen() {
        return cardChosen;
    }

    /**
     * Getter method to access the chosen coordinates (if applicable).
     *
     * @return An array of Integers representing the chosen coordinates ([x, y]). Might be null if not applicable.
     */

    public Integer[] getCoord() {
        return coord;
    }

    /**
     * Set the window elements
     * @param inputPanel panel of the elements
     * @param confirmButton confirm button
     * @param cards how many cards
     * @param action what to do
     * @param error what's wrong
     */
    private void setJPanel(JPanel inputPanel, JButton confirmButton, int cards,ArrayList<Objective> objs,ArrayList<JLabel> hand, String action,ArrayList<Point2D> avPos, String error){
        inputPanel.setLayout(new BorderLayout());
        JTextField choiceField = new JTextField();

        //label per fare capire all'utente cosa deve inserire
        JLabel chooseObj = new JLabel();
        if(action.equalsIgnoreCase("Write the card [x,y] coordinates of where to put it: ")){ //append all the available coordinates
            StringBuilder actionWithAvPos = new StringBuilder("<html><body><p>All available coordinates:<br>");
            for (Point2D coords:avPos) {
                actionWithAvPos.append("x: ").append((int)(coords.getX())).append(" y: ").append((int)coords.getY()).append(";");
            }
            actionWithAvPos.append("<br>").append(action).append("</p></body></html>");
            chooseObj.setText(actionWithAvPos.toString());
        }else
            chooseObj.setText(action);
        chooseObj.setSize(new Dimension(50,90));

        inputPanel.add(chooseObj, BorderLayout.NORTH);
        inputPanel.add(choiceField, BorderLayout.CENTER);


        confirmButton.setMargin(new Insets(10,10,10,10));
        confirmButton.setBackground(Color.decode("#d6c45e"));

        if(action.equalsIgnoreCase("Write the card [x,y] coordinates of where to put it: ")){
            confirmButton.addActionListener(e -> {
                String choice = choiceField.getText().trim();
                if (isValidCoordinate(choice) && isInAvPos(choice,avPos)) {
                    // Gestione scelta dell'utente
                    String[] coordinatesString = choice.split(",");
                    coord[0] = Integer.parseInt(coordinatesString[0].trim());
                    coord[1] = Integer.parseInt(coordinatesString[1].trim());
                    JOptionPane.showMessageDialog(MF, "You have chosen: x->" + coord[0] +", y->"+coord[1]);
                    dispose(); // Chiudi la finestra secondaria
                } else {
                    JOptionPane.showMessageDialog(MF, error);
                }
            });
        }else{
            confirmButton.addActionListener(e -> {
                try{
                    int choice = Integer.parseInt(choiceField.getText().trim());
                    if (choice >= 1 && choice <= cards) {
                        // Gestione scelta dell'utente
                        chosen = choice;
                        if(hand != null) {
                            side = hand.get(chosen - 1).getName();

                            Icon icon = hand.get(chosen - 1).getIcon();

                            // new JLabel with same ImageIcon
                            cardChosen = new JLabel(icon);
                            System.out.println("Card chosen! "+cardChosen.getIcon());

                            // Opzionalmente, copia altre proprietà del JLabel originale
                            cardChosen.setText(hand.get(chosen - 1).getText());
                            cardChosen.setHorizontalAlignment(hand.get(chosen - 1).getHorizontalAlignment());
                            cardChosen.setVerticalAlignment(hand.get(chosen - 1).getVerticalAlignment());
                            cardChosen.setBorder(hand.get(chosen - 1).getBorder());

                        }
                        JOptionPane.showMessageDialog(MF, "You have chosen: " + choice);
                        dispose(); // Chiudi la finestra secondaria
                    } else {
                        JOptionPane.showMessageDialog(MF, error);
                    }
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(MF, "Invalid input. Please enter a number.");
                }

            });
        }


        //gestione posizionamento degli elementi nella griglia

        gbc.insets = new Insets(20, 20, 20, 20); // margini intorno ai componenti

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(inputPanel, gbc); // Aggiungi il pannello di input

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(confirmButton, gbc); // Aggiungi il bottone di conferma

        SwingUtilities.invokeLater(() -> {
            Window frame = (Window) SwingUtilities.getWindowAncestor(inputPanel);
            if (frame != null) {
                frame.pack();
            }
        });
    }

    /**
     * Checks if the given choice string represents a valid coordinate within the available positions.
     *
     * @param choice The string representation of the chosen coordinates (e.g., "1,3").
     * @param avPos A list of Point2D objects representing the available positions.
     * @return True if the choice coordinates are present in avPos, False otherwise.
     */
    private boolean isInAvPos(String choice,ArrayList<Point2D> avPos) {
        String[] coords = choice.split(",");
        Point2D pointCoords = new Point2D.Double(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]));
        return avPos.contains(pointCoords);
    }

    /**
     * Control if the input coordinates are good or not
     * @param input the input from the player
     * @return true or false
     */
    private boolean isValidCoordinate(String input) {
        // Regex per il formato x,y dove x e y sono numeri interi
        String regex =  "^-?\\d+,-?\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
