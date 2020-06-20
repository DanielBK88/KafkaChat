package volfengaut.chatapp.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.entity.user.User;

/**
 * An abstract chat message to be send via Kafka within a chat room.
 **/
@Getter
public abstract class AbstractMessage implements Serializable {
    
    /**
     * The name of the author of this message
     **/
    private String authorName;
    
    /**
     * The name of the chat room, in which the message was sent
     **/
    private String chatRoomName;
    
    /**
     * The date and time, when the message was send
     **/
    private LocalDateTime timeStamp;

    public AbstractMessage(String authorName, String chatRoomName, LocalDateTime timeStamp) {
        this.authorName = authorName;
        this.chatRoomName = chatRoomName;
        this.timeStamp = timeStamp;
    }
    
    /**
     * Convert to an instance of {@link AbstractMessageEntity}, which can be persisted to the database
     **/
    public abstract AbstractMessageEntity toPersistableMessage(User author, User recipient, ChatRoom room);

}
