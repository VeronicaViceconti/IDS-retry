package it.polimi.sw.network.Socket;

import it.polimi.sw.Observer.Observer;
import it.polimi.sw.network.CommonGameLogicServer;
import it.polimi.sw.network.Message.ClientMessage.ConnectionRequest;
import it.polimi.sw.network.Message.ClientMessage.NicknameRequest;
import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;
import it.polimi.sw.network.Message.ClientMessage.TypeMessageClient;
import it.polimi.sw.network.Message.ViewMessage.SampleViewMessage;
import it.polimi.sw.network.Message.serverMessage.SampleServerMessage;
import it.polimi.sw.network.QueueHandlerServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * This class likely implements a handler for client connections on the server-side using sockets.
 * It implements `Runnable` to potentially run in a separate thread, `Serializable` for network transmission, and `Observer` for receiving updates.
 */
public class ClientHandlerSOCKET implements Runnable, Serializable, Observer {

    private final Socket client;
    private boolean online;
    private final CommonGameLogicServer socketServer;
    private QueueHandlerServer queueHandler;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    /**
     * Constructor for ClientHandlerSOCKET.
     *
     * @param client The socket object representing the client connection.
     * @param socketServer A reference to the CommonGameLogicServer object.
     * @param queueHandler A reference to the QueueHandler object.
     */
    public ClientHandlerSOCKET(Socket client, CommonGameLogicServer socketServer, QueueHandlerServer queueHandler) {
        this.client = client;
        this.socketServer = socketServer;
        this.queueHandler = queueHandler;
    }

   /* public ClientHandlerSOCKET(CommonGameLogicServer socketServer, Socket client) {
        this.client = client;
        this.idLobby = -1;
        this.online = true;
        this.socketServer = socketServer;
    }*/

    /**
     * This method overrides the `run` method from the `Runnable` interface.
     * It's likely the entry point for the thread associated with this client handler.
     *
     * @throws IOException This exception is thrown if there is an error during IO operations on the socket.
     */
    @Override
    public void run() {
        try {
            getConnected();
        } catch (IOException e) {
            getDisconnect();
        }
    }
    /**
     * Establishes a connection and handles communication with the client.
     *
     * <p>This method sets up the input and output streams for communication with the client.
     * It then continuously listens for messages from the client, processes specific types of messages
     * such as creating or joining a lobby, and handles other incoming messages.
     * The method runs in an infinite loop until the current thread is interrupted.
     *
     * @throws IOException if an I/O error occurs when creating the input or output streams or when closing them
     */
    public void getConnected() throws IOException{
        try {
                System.out.println("Thread socket client handler partito");
                output = new ObjectOutputStream(client.getOutputStream());
                input = new ObjectInputStream(client.getInputStream());

            //SIAMO GIA' CONNESSI CON CLIENT, CI METTIAMO IN ASCOLTO ALL'INFINITO
                // leggo e scrivo nella connessione finche' non ricevo "quit"
                while (!Thread.currentThread().isInterrupted()) {
                    Object line = input.readObject();
                    receiveMessage(line);
                    output.flush();

                }
        }catch (Exception e) {
            System.err.println("Error receiving a message" + e.getMessage());
        }
        // chiudo gli stream e il socket
        output.close();
        client.close();

    }
    /**
     * Disconnects the client and stops the current thread.
     *
     * <p>This method checks if the client is currently online. If the client is online, it attempts
     * to close the client's socket connection. If the connection is successfully closed or if it was
     * already closed, the method sets the client's online status to {@code false} and interrupts the current thread.
     *
     * @see IOException for exceptions related to input/output operations
     */
    public void getDisconnect() {
        if (online) {
            try {
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            online = false;
            Thread.currentThread().interrupt();

            //notify server
        }
    }
    /**
     * This method likely processes messages received from a client over the socket connection.
     *
     * @param obj The object representing the received message.
     */

    public void receiveMessage(Object obj){

        SampleClientMessage message = (SampleClientMessage) obj;
        if(message.getType() == TypeMessageClient.CONNECTION_REQUEST){
            queueHandler.appendMessage(new ConnectionRequest(((ConnectionRequest)message).getNickname(), this));
        }else {
            if(message.getType() == TypeMessageClient.NICKNAME_REQUEST) {
                queueHandler.appendMessage(new NicknameRequest(((NicknameRequest) message).getName(), this));
            }else{
                queueHandler.appendMessage(message);
            }
        }
    }

    /**
     * This method sends a SampleServerMessage object to the connected client.
     *
     * @param message The SampleServerMessage object containing the data to be sent.
     * @throws IOException This exception is thrown if there is an error during IO operations on the socket.
     */
    public void sendMessageToClient(SampleServerMessage message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException ioException) {
            Thread.currentThread().interrupt();
            ioException.printStackTrace();
        }
    }
    /**
     * This method overrides the default equals method to compare ClientHandlerSOCKET objects.
     * It compares the client socket, output stream, and input stream for equality.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHandlerSOCKET that = (ClientHandlerSOCKET) o;
        return Objects.equals(client, that.client) && Objects.equals(output, that.output) && Objects.equals(input, that.input);
    }
    /**
     * This method overrides the default hashCode method to generate a hash code for ClientHandlerSOCKET objects.
     * It uses the hash codes of the client socket, output stream, and input stream.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(client, output, input);
    }

    /**
     * Receive notify from game and send it via socket to the client socket
     * @param message object to pass
     * @throws RemoteException
     */
    @Override
    public void update(SampleServerMessage message) throws RemoteException {
        this.sendMessageToClient(message);
    }

    //NOT TO USE
    @Override
    public void updateFromView(SampleViewMessage message){

    }

    @Override
    public void requestData() {

    }
}
