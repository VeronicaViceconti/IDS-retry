package it.polimi.sw;

import it.polimi.sw.network.CommonGameLogicServer;
import it.polimi.sw.network.QueueHandler;
import it.polimi.sw.network.RMI.ClientHandlerRMI;
import it.polimi.sw.network.RMI.InterfaceClientHandlerRMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
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
            // Prompt the user to enter the IP address
            System.out.print("Enter the IP address: ");

            // Read the IP address from the user
            Scanner scanner = new Scanner(System.in);
            String ipAddress = scanner.nextLine().trim();

            // Keep prompting until a non-empty IP address is entered
            while (ipAddress.isEmpty()) {
                System.out.println("IP address cannot be empty.");
                System.out.print("Enter the IP address: ");
                ipAddress = scanner.nextLine().trim();
            }

            // Set the system property for RMI server hostname
            System.setProperty("java.rmi.server.hostname", ipAddress);

            // Create RMI registry
            int rmiRegistryPort = 1700;
            Registry registry = LocateRegistry.createRegistry(rmiRegistryPort);
            // Notify user about server booting
            System.out.println("Server is booting....");

            // Set up server socket port
            CommonGameLogicServer server = new CommonGameLogicServer(PORT_NUMBER);
            QueueHandler serverQueue = QueueHandler.getInstance();
            //2 righe da togliere
            if(serverQueue == null)
                System.out.println("QUEUE SERVER NULL");
            InterfaceClientHandlerRMI ClientHandlerRMI = new ClientHandlerRMI(serverQueue);
            registry.rebind("ClientHandlerRMI", ClientHandlerRMI);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    registry.unbind("ClientHandlerRMI");
                    UnicastRemoteObject.unexportObject(ClientHandlerRMI, true);
                } catch (Exception e) {
                    System.err.println("Errore durante l'arresto del server RMI: " + e.getMessage());
                }
            }));

            // Start the server in a separate thread
            Thread serverThread = new Thread(server, "server");
            serverThread.start();

            // Notify user about successful server creation
            System.out.println("Server created successfully.");
        } catch (Exception e) {
            // Notify user if an exception occurs
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public static void pingClient(){

    }
}