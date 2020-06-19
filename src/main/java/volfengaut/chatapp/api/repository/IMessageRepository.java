package volfengaut.chatapp.api.repository;

import java.util.Collection;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.message.MessageType;
import volfengaut.chatapp.entity.user.User;

/**
 * Interface for database operations concerning messages
 **/
public interface IMessageRepository extends EntityManagerDependent {

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
