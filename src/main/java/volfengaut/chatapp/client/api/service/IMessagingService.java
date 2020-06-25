package volfengaut.chatapp.client.api.service;

import volfengaut.chatapp.message.AbstractMessage;

/**
 * Handles the messaging on client side
 **/
public interface IMessagingService {

    /**
     * Send a message to the server
     **/
    void send(AbstractMessage message);
}
