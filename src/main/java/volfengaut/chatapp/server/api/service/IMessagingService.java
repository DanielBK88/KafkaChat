package volfengaut.chatapp.server.api.service;

import java.net.Socket;
import volfengaut.chatapp.message.AbstractMessage;

/**
 * Handles the sending and receiving of messages and stores them into the database.
 **/
public interface IMessagingService {

    /**
     * Sends the given message to the given destination socket
     **/
    void send(Socket destination, AbstractMessage message);
    
    /**
     * Waits for a message from the given source socket and returns it
     **/
    AbstractMessage receive(Socket source);
}
