package it.polimi.sw;

import it.polimi.sw.network.Client;
import it.polimi.sw.network.RMI.ClientRMI;
import it.polimi.sw.network.Socket.ClientSocket;
import it.polimi.sw.view.GraphicalUserInterface.GUI;
import it.polimi.sw.view.TUI;
import it.polimi.sw.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class likely represents the entry point for the client-side of the game "Codex Naturalis".
 * It's responsible for establishing a connection with the server, choosing the user interface type (GUI or TUI),
 * handling user input for nickname selection, and potentially creating client objects based on the chosen connection type (sockets or RMI).
 */
public class CodexNaturalis_Client {
    /*static final String SERVER_ADDRESS = "localhost";*/
    static final int PORT_NUMBER = 17001;


    public static void main(String[] args) throws NotBoundException, IOException {

        String serverIpaddress = "";
        String BLUE = "\033[0;34m";

        int connection = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Please enter an IP address (IPv4 format xxx.xxx.xxx.xxx): ");
            serverIpaddress = reader.readLine().trim();

            if (!serverIpaddress.isEmpty()) {
                break;
            } else {
                System.out.println("IP address cannot be empty. Please try again.");
            }
        }
        System.out.println("You entered an IP address: " + serverIpaddress);

        do {
            System.out.println(BLUE+"Which type of connection do you want?\nWrite 1 for socket, 2 for RMI.");
            try {
                connection = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.err.println("You must choose a number, Either 1 or 2");
                reader.readLine();
            }
        } while (connection != 1 && connection != 2);

        View view;
        int interfaceType = 0;
        while(interfaceType < 1 || interfaceType > 2){
            try{
                System.out.println(BLUE + "Enter 1 for Graphical User Interface or 2 for Textual User Interface");
                interfaceType = Integer.parseInt(reader.readLine());
            }catch(IOException | NumberFormatException e ){
                System.err.println("You must choose a number, Either 1 or 2");
                reader.readLine();
            }
        }
        if (interfaceType == 1) {
            view = new GUI();
        } else {
            view = new TUI();
        }

        Matcher matcher;
        String nickname = "";
        do {
            System.out.println("Choose your nickname: ");
            nickname = reader.readLine();
            Pattern pattern = Pattern.compile("[A-Za-z]");
            matcher = pattern.matcher(nickname);
        } while (!matcher.find());

        Client client;

        if (connection == 1) {
            client = new ClientSocket(serverIpaddress, PORT_NUMBER, view,nickname); //singleton si crea da solo
            new Thread(client).start();
        }else{
            new ClientRMI(view, nickname); //THREAD FATTO PARTIRE NEL COSTRUTTORE
        }

    }
}