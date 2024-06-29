package it.polimi.sw.view.GraphicalUserInterface.Containers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ManuscriptsMainPanel extends JPanel {
    private JTabbedPane tp;
    private ArrayList<Manuscript> allManuscripts;

    public ManuscriptsMainPanel() {
        setLayout(new BorderLayout());

        tp = new JTabbedPane();
        tp = new JTabbedPane(JTabbedPane.TOP);
        allManuscripts = new ArrayList<>();

        add(new JScrollPane(tp));

    }

    public void createTabbedManuscripts(ArrayList<String> names) {
        //for each player, create its tab manuscript
        for (String name: names) {
            allManuscripts.add(new Manuscript(name));
            tp.add(name, new JScrollPane(allManuscripts.getLast()));
        }
    }

    public ArrayList<Manuscript> getAllManuscripts() {
        return allManuscripts;
    }
}