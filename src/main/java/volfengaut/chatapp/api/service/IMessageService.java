package volfengaut.chatapp.api.service;

import java.util.Collection;
import volfengaut.chatapp.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.entity.message.ChatMessageEntity;
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
    
}
