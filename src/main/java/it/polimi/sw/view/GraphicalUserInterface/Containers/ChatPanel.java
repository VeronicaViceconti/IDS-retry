package it.polimi.sw.view.GraphicalUserInterface.Containers;
import it.polimi.sw.network.Message.ViewMessage.SendingChatMessage;
import it.polimi.sw.view.GraphicalUserInterface.GUI;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * This class likely represents a JPanel component for displaying and interacting with a chat functionality within a user interface.
 * It provides features for:
 *  * Displaying received messages in a JTextArea.
 *  * Composing and sending messages through a JTextArea and a JButton.
 *  * Selecting a recipient for the message using a JComboBox.
 */
public class ChatPanel extends JPanel{
    private JPanel chat;

    private JTextArea receiveMessage;
    private JTextArea sendMessage;
    private JScrollPane inputMessage,outputMessage;

    private JComboBox menuSendTo;
    private JButton send;


    /**
     * Constructor for the ChatPanel.
     *
     * @param gui A reference to the GUI object likely used for communication and updates.
     */


    public ChatPanel(GUI gui,Dimension screenSize){

        chat=new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());


        c.insets=new Insets(30,30,0,30);
        c.gridy = 0;
        c.gridx = 0;


        receiveMessage = new JTextArea();
        receiveMessage.setEditable(false);
        Border border=BorderFactory.createTitledBorder("CHAT");
        Border emptyBorder = BorderFactory.createEmptyBorder();
        Border combinedBorder = BorderFactory.createCompoundBorder(border,emptyBorder);
        receiveMessage.setBorder(combinedBorder);


        outputMessage = new JScrollPane(receiveMessage);
        outputMessage.setPreferredSize(new Dimension(580, 520));
        add(outputMessage, c);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.VERTICAL;

        sendMessage = new JTextArea("Type a message...");
        sendMessage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendMessage.setText("");
            }
        });

        sendMessage.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent e) {
                if(sendMessage.getText().isEmpty()){
                    sendMessage.setText("Type a message...");
                }
            }
        });

        inputMessage = new JScrollPane(sendMessage);
        c.insets = new Insets(10, 10, 0, 10);
        inputMessage.setPreferredSize(new Dimension(580, 100));
        add(inputMessage, c);

        send=new JButton("send");
        send.setPreferredSize(new Dimension(100,50));
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 1;


        add(send,buttonConstraints);



        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.gridx = 2;
        menuConstraints.gridy = 1;
        String[] opzioniSend={"All","Player1","Player2","Player3","Player4"};
        menuSendTo=new JComboBox(opzioniSend);
        menuSendTo.setPreferredSize(new Dimension(50,20));
        add(menuSendTo,menuConstraints);

        send.addActionListener(e -> {
            if(menuSendTo.getSelectedItem().equals("All")){
                String messageText = sendMessage.getText();
                gui.notify(new SendingChatMessage(messageText));
                gui.showGameChat(messageText);
            }else{
                String messageText = sendMessage.getText();
                gui.notify(new SendingChatMessage(messageText));
                gui.showGameChat(messageText);
            }
            sendMessage.setText("");
        });


        validate();

        setVisible(true);
    }

    /**
     * Appends a new message to the chat history displayed in the chat panel.
     *
     * @param message The message to be displayed.
     */
    public void showGameChat(String message){

        receiveMessage.append(message + "\n");
    }


}
