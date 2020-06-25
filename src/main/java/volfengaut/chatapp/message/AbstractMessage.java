package volfengaut.chatapp.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.server.entity.user.User;

/**
 * An abstract chat message to be send via Kafka within a chat room.
 **/
@Getter
@Setter
public abstract class AbstractMessage implements Serializable {
    
    /**
     * The name of the author of this message
     * Is null, if it is a message from the server to a client
     **/
    private String authorName;
    
    /**
     * The name of the recipient of the message
     * Is null, if it is a message from a client to the server or a public chat message
     **/
    private String recipientName;
    
    /**
     * The name of the chat room, in which the message was sent
     * May be null.
     **/
    private String chatRoomName;
    
    /**
     * The date and time, when the message was send
     **/
    private LocalDateTime timeStamp;

    public AbstractMessage(String authorName, String recipientName, String chatRoomName, LocalDateTime timeStamp) {
        this.authorName = authorName;
        this.recipientName = recipientName;
        this.chatRoomName = chatRoomName;
        this.timeStamp = timeStamp;
    }
    
    /**
     * Convert to an instance of {@link AbstractMessageEntity}, which can be persisted to the database
     **/
    public abstract AbstractMessageEntity toPersistableMessage(User author, User recipient, ChatRoom room);

}
