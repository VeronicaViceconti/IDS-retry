package it.polimi.sw.view.GraphicalUserInterface.main;

import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Pion;
import it.polimi.sw.model.Player;
import java.util.List;
import it.polimi.sw.network.Message.serverMessage.ErrorType;
import it.polimi.sw.view.GraphicalUserInterface.Containers.CommonTableANDPrivateTable;
import it.polimi.sw.view.GraphicalUserInterface.Containers.ImageSelectionDialog;
import it.polimi.sw.view.GraphicalUserInterface.Containers.Manuscript;
import it.polimi.sw.view.GraphicalUserInterface.Containers.PlateauAndChat;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class Framework extends JFrame {
    private PlateauAndChat ps;
    private CommonTableANDPrivateTable ctpt;
    private JTabbedPane tp;
    private ArrayList<String> names;
    private boolean ok;

    private int chosen;
    private Integer[] coord;
    private JLabel cardChosen;
    private String nickname;

    /**
     * Constructs a new Framework.
     * Initializes Codex Naturalis Game.
     */
    public Framework(GUI gui) {

        super("Codex Naturalis");
        ok = true;
        this.names = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        screenSize.height = (int) (screenSize.height - screenSize.height*0.1);
        screenSize.width = (int)(screenSize.width-screenSize.width*0.1);
        setSize( screenSize.width , screenSize.height );

        setResizable(false);
        setLocationRelativeTo(null);
        setAlwaysOnTop(false);

        ArrayList<Pion> pion = new ArrayList<>();

        ps = new PlateauAndChat(screenSize,pion,gui);

        tp = new JTabbedPane(JTabbedPane.TOP);

        add(new JScrollPane(tp));
        setVisible(true);
    }

    /**
     * Method called when client receive a setBoardData message
     * @param fdG the gold back card of the common table
     * @param fdR the resource back card of the common table
     * @param fuG the two gold front cards of the common table
     * @param fuR the two resource front cards of the common table
     */
    public void initializeCommonTable(Card fdG, Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,ArrayList<Objective> commonObj){
        ctpt = new CommonTableANDPrivateTable(fdG,fdR,fuG,fuR,commonObj);

        SwingUtilities.invokeLater(()->{
            tp.add("Main Game",ctpt);
            tp.add("Plateau&Chat",new JScrollPane(ps));
        });

    }

    /**
     * Update the resources and objects
     * @param numOfResourceAndObject contains all the resources and objects of the player
     */
    public void updatePlayerResAndObj(HashMap<String, Integer> numOfResourceAndObject){
        this.ps.getPs().updateObjectsAndResources(numOfResourceAndObject);
    }

    /**
     * Adds the private objective to the player gui
     * @param obj objective to be added
     */
    public void addPrivateObjective(Objective obj,GUI gui){
        tp.setSelectedIndex(0);

        ctpt.getMyHandAndObjectivesAndCommonTable().getHo().addPrivObj(obj,this);
    }

    /**
     * Create the hand of the player
     * @param hand the hand of the player right now
     */
    public void addHand(ArrayList<Card> hand){
        ctpt.getMyHandAndObjectivesAndCommonTable().addClientThings();
        ctpt.getMyHandAndObjectivesAndCommonTable().getHo().addHand(hand);
    }

    /**
     * Method invocated to create and add the pions in 0 points on the score board
     * @param pions, the pions of the players
     */
    public void initializePions(int indexMe,ArrayList<Pion> pions){
        this.ps.getPs().getPlateau().initializePions(indexMe,pions);
    }

    /**
     * Remove and update the hand of the player
     * @param cards the hand of the player
     */
    public void updateHand(ArrayList<Card> cards){
        ctpt.getMyHandAndObjectivesAndCommonTable().getHo().updateHand(cards);
    }

    /**
     *
     * @return the card, 1-2-3, to play
     */
    public Integer[] chooseCardToPut(){
        tp.setSelectedIndex(0);
        ImageSelectionDialog selection = new ImageSelectionDialog(this, ctpt.getMyHandAndObjectivesAndCommonTable().getHo().cloneJLabelArray(),"NotCoordinates",null,false);
        selection.setVisible(true);
        Integer[] positionAndSide = new Integer[2];
        positionAndSide[0] = selection.getChosen();
        positionAndSide[1] = Integer.parseInt(selection.getSide());
        cardChosen = selection.getCardChosen();
        return positionAndSide;
    }

    /**
     *
     * @return the x and y coordinates of the card to play
     */
    public Integer[] chooseCoordinates(ArrayList<Point2D> avaiableCoords,String nickname){
        tp.setSelectedIndex(0);
        coord = new Integer[2];
        ImageSelectionDialog selection = new ImageSelectionDialog(this, ctpt.getMyHandAndObjectivesAndCommonTable().getHo().cloneJLabelArray(),"Coordinates",avaiableCoords,false);
        selection.setVisible(true);
        return selection.getCoord();
    }

    /**
     *
     * @return boolean, if hand is empty or not
     */
    public boolean alreadyCreatedHand(){
        return !ctpt.getMyHandAndObjectivesAndCommonTable().getHo().getHand().isEmpty();
    }

    /**
     *
     * @param one the first objective
     * @param two the second objective
     * @return
     */
    public int choosePrivateObjective(Objective one, Objective two){
        tp.setSelectedIndex(0);
        ImageSelectionDialog selection = new ImageSelectionDialog(this, one, two);
        selection.setVisible(true);
        return chosen;
    }

    /**
     * Used after have chosen the coordinates of where to put the card, so we have to remove the visibility in the hand
     * @param i the card index to be removed
     */
    public void removeCardFromHand(int i) {
        ctpt.getMyHandAndObjectivesAndCommonTable().getHo().removeCardFromHand(i);
    }

    /**
     *
     * @param gui refers to the gui in order to call a specific method when 'Im ready' button is clicked
     */
    public void turnNotification(GUI gui,String nickname){
        tp.setSelectedIndex(0);
        YourTurnDialog nytd = new YourTurnDialog(this,"<p>It's your turn! Flip the cards and, when you are ready, click the 'I'm ready' button! Or:<ul><li>Send a message!</li><li>Visit other player's manuscript!</li></ul></p>",500,200);
        nytd.setVisible(true);

        this.nickname = nickname;
        //set the tab pane to the player's turn
        for (int i=0;i<this.ctpt.getManuscript().getAllManuscripts().size();i++) {
            if(this.ctpt.getManuscript().getAllManuscripts().get(i).getNickname().equals(nickname)) {
                ctpt.getManuscript().getTp().setSelectedIndex(i);
            }
        }

        ctpt.getMyHandAndObjectivesAndCommonTable().getHo().addReadyButton(gui);
        if(ok)
            ps.getChatPanel().setChatPlayers(nickname,names);
        ok = false;
    }

    /**
     * Window to indicates to the user it's not his turn
     */
    public void notYourTurn() {
        YourTurnDialog nytd = new YourTurnDialog(this,"I'm sorry, it's not your turn!But you can:<ul><li>Send a message!</li><li>Visit other player's manuscript!</li></ul>",300,150);
        nytd.setVisible(true);
    }

    /**
     *
     * @return which deck and which card to draw
     */
    public ArrayList<Integer> drawCard(){
        tp.setSelectedIndex(0);

        YourTurnDialog ytd = new YourTurnDialog(this,"<p>It's time to draw a card!</p>",300,150);
        ytd.setVisible(true);

        ArrayList<Integer> infoCard = new ArrayList<>();
        OptionDialog od = new OptionDialog(this,"Facedown decks","Faceup decks","Select the deck from which draw a card!");
        od.setVisible(true);

        if(od.selectedOption == 1){

            OptionDialog od2 = new OptionDialog(this,"Gold deck","Resource deck","Select the deck from which draw a card!");
            od2.setVisible(true);
            infoCard.add(od2.selectedOption);
            return infoCard;
        }else{
            OptionDialog od3 = new OptionDialog(this,"Pick from gold cards","Pick from resource cards","Now, select from where to pick the card!");
            od3.setVisible(true);
            infoCard.add(od3.selectedOption);

            OptionDialog od4 = new OptionDialog(this,"Card 1","Card 2","And finally, select which card 1, or 2, you want to pick up!");
            od4.setVisible(true);
            infoCard.add(od4.selectedOption);
            this.ctpt.getMyHandAndObjectivesAndCommonTable().getCt().removeCardFromFaceUpDeck(infoCard.get(0),infoCard.get(1)-1);
            return infoCard;
        }
    }

    /**
     * Method invocated to update the common table
     * @param faceDownGold the gold back card of the common table
     * @param faceDownResource the resource back card of the common table
     * @param faceupGold the two gold front cards of the common table
     * @param faceupResource the two resource front cards of the common table
     */
    public void updateCommoneTable(Card faceDownGold, Card faceDownResource, ArrayList<Card> faceupGold, ArrayList<Card> faceupResource) {
        this.ctpt.getMyHandAndObjectivesAndCommonTable().getCt().updateCommonTable(faceDownGold,faceDownResource,faceupGold,faceupResource);
    }

    /**
     * Add the button to the gui in order to let the user flip the card whenever he wants
     * @param gui refers to gui in order to call a specific method when 'Flip a Card' button is clicked
     */
    public void addFlipButton(GUI gui) {
        this.ctpt.getMyHandAndObjectivesAndCommonTable().getHo().addFlipButton(this,gui);
    }

    /**
     * Update the points of the player
     * @param pion the pion of the player
     * @param points points of the player
     */
    public void updatePoints(Pion pion, int points) {
        this.ps.getPs().getPlateau().updatePlayerPosition(pion,points);
    }

    public void showError(ErrorType error,GUI gui){
        YourTurnDialog err = new YourTurnDialog(this, error.name().replace("_"," ")+", RETRY!",350,150 );
        err.setVisible(true);
        switch (error){
            case INCORRECT_PARAMETRES_PLACECARD,NO_CORNERS_COVERED, NOT_ENOUGH_RESOURCES, CARD_NOT_IN_HAND, COVERED_TWO_CORNERS, HIDDEN_CORNER_COVERED:
                YourTurnDialog back_To_PLACECARD = new YourTurnDialog(this, "Flip the card and when you are ready, click the 'I'm ready' button!",450,150 );
                back_To_PLACECARD.setVisible(true);
                this.ctpt.getMyHandAndObjectivesAndCommonTable().getHo().addReadyButton(gui);
                break;
            case INCORRECT_PARAMETRES_DRAWCARD:
            case EMPTY_DECK:
                this.drawCard();
                break;

        }
    }

    /**
     * This method likely delegates the functionality of displaying a game chat message to a ChatPanel object.
     *
     * @param message The message string to be displayed in the game chat.
     */

    public void showGameChat(String message) {

        ps.getChatPanel().showGameChat(message);

    }

    public void updateManuscript(String nickname, Card card,Integer x, Integer y) {

        for (int i=0;i<this.ctpt.getManuscript().getAllManuscripts().size();i++) {
            if(this.ctpt.getManuscript().getAllManuscripts().get(i).getNickname().equals(nickname)) {
                this.ctpt.getManuscript().getAllManuscripts().get(i).updateManuscript(card, x, y);
            }
            if(this.ctpt.getManuscript().getAllManuscripts().get(i).getNickname().equals(this.nickname)) {
                ctpt.getManuscript().getTp().setSelectedIndex(i);
            }
        }
    }

    public void announceWinners(List<Player> winners){
        StringBuilder s = new StringBuilder(" ");
        for (Player p: winners) {
            s.append(p.getNickName()).append(", ");
        }
        YourTurnDialog finalDialog = new YourTurnDialog(this, "The winners are :"+s,450,350);
        finalDialog.setVisible(true);
    }

    public void createTabbedManuscripts(ArrayList<String> names) {
        ctpt.getManuscript().createTabbedManuscripts(names);
        this.names = names;
    }

    /**
     * Class used to show if it is, or it is not the turn of the player
     */
    public static class YourTurnDialog extends JDialog {
        Timer timer = null;

        /**
         * Constructor for the YourTurnDialog.
         *
         * @param mainFrame The reference to the main game frame.
         * @param notification The message to be displayed in the dialog.
         * @param width The desired width of the dialog window.
         * @param height The desired height of the dialog window.
         */

        public YourTurnDialog(JFrame mainFrame,String notification,int width,int height) {
            super(mainFrame, "Notification", true);
            setSize(width,height);
            setLayout(new GridBagLayout());
            if(notification.startsWith("The winner")) {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                System.exit(0);
            }else
                setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - getWidth()) / 2;
            int y = (screenSize.height - getHeight()) / 2;
            setLocation(x, y);


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);



            JLabel messageLabel = new JLabel();
            messageLabel.setPreferredSize(new Dimension(width,height));
            messageLabel.setText("<html><body style='width: " + (width - 0.25*width) + "px;'>" + notification + "</body></html>");
            messageLabel.setVerticalAlignment(SwingConstants.TOP);
            messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            add(messageLabel, gbc);


            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> dispose());
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            add(okButton, gbc);


            int timeoutInSeconds = 10;
            timer = new Timer(timeoutInSeconds * 1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    if(notification.startsWith("The winner"))
                        System.exit(0);
                    if(timer != null)
                        timer.stop();
                }
            });
            timer.setRepeats(false);
            timer.start();

        }
    }

    /**
     * Class used to show a window to comunicate with the user to obtain some inputs
     */
    public class OptionDialog extends JDialog {
        /**
         * First option
         */
        private JRadioButton option1;
        /**
         * Second option
         */
        private JRadioButton option2;
        /**
         * Final button
         */
        private JButton okButton;

        /**
         * The selected option
         */
        private int selectedOption = -1;

        /**
         * Constructor for the OptionDialog.
         *
         * @param mainFrame The reference to the main game frame.
         * @param opt1 The text for the first option.
         * @param opt2 The text for the second option.
         * @param action An action description displayed above the options.
         */

        public OptionDialog(JFrame mainFrame,String opt1, String opt2,String action) {
            super(mainFrame, "Select the deck", true);
            setSize(300, 200);
            setLayout(new GridBagLayout());
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - getWidth()) / 2;
            int y = (screenSize.height - getHeight()) / 2;
            setLocation(x, y);


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;

            JLabel descriptionLabel = new JLabel(action);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            add(descriptionLabel, gbc);


            option1 = new JRadioButton(opt1);
            option2 = new JRadioButton(opt2);
            ButtonGroup group = new ButtonGroup();
            group.add(option1);
            group.add(option2);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            add(option1, gbc);

            gbc.gridy = 2;
            add(option2, gbc);


            okButton = new JButton("SELECT");
            okButton.addActionListener(e -> {
                if (option1.isSelected()) {
                    selectedOption = 1;
                } else if (option2.isSelected()) {
                    selectedOption = 2;
                }
                if(action.equalsIgnoreCase("And finally, select which card 1, or 2, you want to pick up!") )
                    JOptionPane.showMessageDialog(mainFrame,"Good, you have selected the card!");
                dispose();
            });

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            add(okButton, gbc);
        }
    }



}

