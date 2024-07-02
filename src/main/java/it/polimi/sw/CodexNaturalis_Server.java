package it.polimi.sw;

import it.polimi.sw.network.CommonGameLogicServer;
import it.polimi.sw.network.QueueHandlerServer;
import it.polimi.sw.network.RMI.ClientHandlerRMI;
import it.polimi.sw.network.RMI.InterfaceClientHandlerRMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * This class likely represents the entry point for the server-side of the game "Codex Naturalis".
 * It's responsible for the following tasks:
 *  * Prompting the user to enter the server's IP address.
 *  * Setting up the RMI registry and binding the server object to it.
 *  * Creating a server instance (`CommonGameLogicServer`) and starting it in a separate thread.
 *  * Creating and registering an RMI interface implementation (`ClientHandlerRMI`) to handle client communication.
 */
public class CodexNaturalis_Server {
    public static void main(String[] args) {
        final int PORT_NUMBER = 17001;
        try {

            System.out.print("Enter the IP address: ");


            Scanner scanner = new Scanner(System.in);
            String ipAddress = scanner.nextLine().trim();


            while (ipAddress.isEmpty()) {
                System.out.println("IP address cannot be empty.");
                System.out.print("Enter the IP address: ");
                ipAddress = scanner.nextLine().trim();
            }


            System.setProperty("java.rmi.server.hostname", ipAddress);


            int rmiRegistryPort = 1700;
            Registry registry = LocateRegistry.createRegistry(rmiRegistryPort);

            System.out.println("Server is booting....");


            CommonGameLogicServer server = new CommonGameLogicServer(PORT_NUMBER);
            QueueHandlerServer serverQueue = QueueHandlerServer.getInstance();

            if(serverQueue == null)
                System.out.println("QUEUE SERVER NULL");
            InterfaceClientHandlerRMI ClientHandlerRMI = new ClientHandlerRMI(serverQueue);
            registry.rebind("ClientHandlerRMI", ClientHandlerRMI);


            Thread serverThread = new Thread(server, "server");
            serverThread.start();


            System.out.println("Server created successfully.");
        } catch (Exception e) {

            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public static void pingClient(){

    }
}