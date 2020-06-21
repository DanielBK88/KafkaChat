package volfengaut.chatapp.api.repository;

import java.util.Collection;
import volfengaut.chatapp.entity.WelcomeMessage;
import volfengaut.chatapp.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.entity.message.ChatMessageEntity;
import volfengaut.chatapp.entity.user.User;

/**
 * Interface for database operations concerning messages
 **/
public interface IMessageRepository extends EntityManagerDependent {

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
    WelcomeMessage getWelcomeMessage(String language);
    
    /**
     * Set the translated welcome message
     **/
    void setWelcomeMessage(WelcomeMessage message);
}
