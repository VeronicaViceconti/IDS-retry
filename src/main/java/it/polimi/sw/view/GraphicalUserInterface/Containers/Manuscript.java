package it.polimi.sw.view.GraphicalUserInterface.Containers;

import it.polimi.sw.model.Card;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Manuscript extends JPanel {

    private ArrayList<JLabel> cardsPlayed;
    private String nickname;
    private ArrayList<Integer> centerXY;
    private ArrayList<Integer> offsetXY;

    public Manuscript(String nickname) {
        cardsPlayed = new ArrayList<>();
        setLayout(null);
        centerXY = new ArrayList<>();
        offsetXY = new ArrayList<>();

        this.nickname = nickname;
        centerXY.add(this.getWidth()/2);
        centerXY.add(this.getHeight()/2);
    }

    public void updateManuscript(JLabel card, int x, int y) {
        card.setLocation(centerXY.get(0),centerXY.get(1));

        add(card);
        revalidate();
        repaint();
    }

    public String getNickname() {
        return nickname;
    }
}
