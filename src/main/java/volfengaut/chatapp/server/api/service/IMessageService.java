package volfengaut.chatapp.server.api.service;

import java.util.Collection;
import volfengaut.chatapp.server.entity.WelcomeMessage;
import volfengaut.chatapp.server.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.server.entity.message.ChatMessageEntity;
import volfengaut.chatapp.server.entity.user.User;

/**
 * Used to manage messages.
 * Provides the functionality to fulfill specialized queries for messages.
 **/
public interface IMessageService {

    /**
     * Inserts a new message
     **/
    void addMessage(AbstractMessageEntity message);

    /**
     * Find messages sent by certain users
     **/
    Collection<ChatMessageEntity> findMessagesSendBy(User user);

    /**
     * Find messages of a certain type
     **/
    <T extends AbstractMessageEntity> Collection<T> findMessagesOfType(Class<T> messageClass);

    /**
     * Count, how often this user was banned
     **/
    int countBansOfUser(User user);

    /**
     * Get the translated welcome message
     **/
    String getWelcomeMessage(String language);
    
    /**
     * Set the translated welcome message
     **/
    void setWelcomeMessage(WelcomeMessage message);
    
    /**
     * Find all messages, which were send to this user by the server and all messages, which this user has send
     * to the server (not client-to-client messages) and update their user column.
     * The messages will be retrieved by the user's current internet address.
     * 
     * This is needed, because server-to-client messages are stored to the db with the user field being null
     * in the case, when the user did not yet sign up or log in successfully.
     **/
    void updateServerMessagesForUser(User user);
    
}
