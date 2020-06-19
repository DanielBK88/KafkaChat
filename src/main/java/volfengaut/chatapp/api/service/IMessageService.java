package volfengaut.chatapp.api.service;

import java.util.Collection;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.message.MessageType;
import volfengaut.chatapp.entity.user.User;

/**
 * Used to manage messages.
 * Provides the functionality to fulfill specialized queries for messages.
 **/
public interface IMessageService {

    /**
     * Inserts a new message
     **/
    void addMessage(Message message);

    /**
     * Find messages sent by certain users
     **/
    Collection<Message> findMessagesSendBy(User user);

    /**
     * Find messages of a certain type
     **/
    Collection<Message> findMessagesOfType(MessageType type);

    /**
     * Count, how often this user was banned
     **/
    int countBansOfUser(User user);
    
}
