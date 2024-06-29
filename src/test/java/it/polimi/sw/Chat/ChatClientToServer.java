package it.polimi.sw.Chat;
import it.polimi.sw.model.*;
import it.polimi.sw.network.Message.ViewMessage.SendingChatMessage;
import it.polimi.sw.network.Socket.ClientSocket;
import it.polimi.sw.view.TUI;
import it.polimi.sw.view.View;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatClientToServer {
    static View tui=new TUI();

    static String serveraddress = "server";

    static String name = "Giuseppe";
    static ClientSocket cs = new ClientSocket(serveraddress, 8080, tui, name);

    //static SendingChatMessage mx = new SendingChatMessage();
    public static void main (String[] args) throws RemoteException {
       // cs.publicChatMessage(mx);

        tui.chatUnblockWait();
        tui.showGameChat(new String("Ghost says boooo.showGameChat")); //works with unblock, not without

        System.out.println("dopo tui");
    }
}
