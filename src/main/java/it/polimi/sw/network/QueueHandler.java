package it.polimi.sw.network;

import it.polimi.sw.network.Message.ClientMessage.SampleClientMessage;

import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * This class provides a singleton instance for managing a queue of messages to be sent to the server.
 * It uses a ConcurrentLinkedQueue for thread-safe access.
 */

public class QueueHandler {

    private static QueueHandler instance;
    private ConcurrentLinkedQueue<SampleClientMessage> queue = new ConcurrentLinkedQueue<>();
    /**
     * This method retrieves the singleton instance of QueueHandler.
     * It's synchronized to ensure thread-safe access to the instance creation.
     *
     * @return The singleton instance of QueueHandler.
     */
    public static synchronized QueueHandler getInstance(){
        if(instance == null)
            instance = new QueueHandler();
        return instance;
    }

    /**
     * This method adds a SampleClientMessage object to the message queue.
     * It's synchronized to ensure thread-safe access to the queue.
     *
     * @param message The SampleClientMessage object to be added.
     */
    public synchronized void appendMessage(SampleClientMessage message){
        queue.add(message);
    }

    /**
     * This method retrieves and removes the next message from the queue.
     * It's synchronized to ensure thread-safe access to the queue.
     *
     * @return The next SampleClientMessage object from the queue, or null if the queue is empty.
     */
    public synchronized SampleClientMessage getNextMessage(){
        return queue.poll();
    }

    /**
     * This method checks if the message queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
