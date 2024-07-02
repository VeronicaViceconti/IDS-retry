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

        add(new JScrollPane(tp),BorderLayout.CENTER);

        setVisible(true);
    }

    public void createTabbedManuscripts(ArrayList<String> names) {

        SwingUtilities.invokeLater(() -> {

            for (String name: names) {
                Manuscript manuscript = new Manuscript(name);
                JScrollPane scrollPane = new JScrollPane(manuscript);

                tp.add(name, scrollPane);
                allManuscripts.add(manuscript);
            }
        });

    }

    public JTabbedPane getTp() {
        return tp;
    }

    public ArrayList<Manuscript> getAllManuscripts() {
        return allManuscripts;
    }
}