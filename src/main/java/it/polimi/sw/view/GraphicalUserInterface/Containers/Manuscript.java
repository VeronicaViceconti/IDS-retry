package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Card;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Manuscript extends JPanel {

    //private ArrayList<JLabel> cardsPlayed;
    private String nickname;
    private ArrayList<Integer> centerXY;
    private JPanel innerPanel;

    public Manuscript(String nickname) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 500));
        centerXY = new ArrayList<>();

        this.nickname = nickname;
        centerXY.add(1500 / 2);
        centerXY.add(1500 / 2);

        // Create an inner panel with null layout for manual positioning
        innerPanel = new JPanel(null);
        innerPanel.setPreferredSize(new Dimension(1500, 1500));

        // Add the inner panel to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(innerPanel);
        add(scrollPane, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            Rectangle bounds = scrollPane.getViewport().getViewRect();

            JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
            horizontal.setValue( (horizontal.getMaximum() - bounds.width) / 2 );

            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue( (vertical.getMaximum() - bounds.height) / 2 );
        });

        setVisible(true);
    }

    public void updateManuscript(Card card, int x,int y) {
        String path = "src/main/resources/graphicalResources/"+card.getClass().getName().substring(19);
        if(card.getSide() == 1)
            path += "Front/";
        else
            path += "Back/";


        //inserisco le immagini
        ImageIcon handImage = new ImageIcon(path + String.format("%03d", card.getId()) + ".png");
        Image img = handImage.getImage().getScaledInstance(120,100, Image.SCALE_SMOOTH);
        handImage.setImage(img);
        JLabel cardLabel = new JLabel(handImage);

        int posX = centerXY.get(0) + x * (120 / 3 * 2 + 5);
        int posY = centerXY.get(1) - y * (100 / 3 * 2 - 10);

        cardLabel.setBounds(0, 0, 120, 100);

        cardLabel.setLayout(null); // Use null layout for manual positioning
        cardLabel.setOpaque(true);
        cardLabel.setBackground(Color.LIGHT_GRAY);
        cardLabel.setPreferredSize(new Dimension(100, 150));

        // Create the label with coordinates
        JLabel coordinatesLabel = new JLabel("(" + x + ", " + y + ")");
        coordinatesLabel.setOpaque(true);
        coordinatesLabel.setBackground(Color.white);
        coordinatesLabel.setBounds(28, 15, 30, 18); // Position the label at the top of the card

        // Add the coordinates label to the card
        cardLabel.add(coordinatesLabel);

        // Wrapping card in a JPanel with null layout to place it correctly
        JPanel wrapper = new JPanel(null);
        wrapper.setOpaque(false);
        wrapper.setBounds(posX,posY, 120, 100);
        wrapper.add(cardLabel);

        innerPanel.add(wrapper);
        innerPanel.setComponentZOrder(wrapper, 0); // Adjust Z-order for visibility to the first layer

        innerPanel.revalidate();
        innerPanel.repaint();
        innerPanel.setVisible(true);
    }

    public String getNickname() {
        return nickname;
    }
}
