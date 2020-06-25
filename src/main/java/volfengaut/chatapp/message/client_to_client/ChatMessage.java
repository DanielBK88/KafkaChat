package volfengaut.chatapp.message.client_to_client;

import java.time.LocalDateTime;
import lombok.Getter;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.message.ChatMessageEntity;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;

/**
 * An implementation of {@link AbstractMessage} representing a chat message
 **/
@Getter
public class ChatMessage extends AbstractMessage {

    /**
     * The text of the message
     **/
    private String messageText;

    public ChatMessage(String authorName, String chatRoomName, LocalDateTime timeStamp,
            String messageText, String recipient) {
        super(authorName, recipient, chatRoomName, timeStamp);
        this.messageText = messageText;
    }
    
    public ChatMessage(String messageText, String recipient) {
        this(null, null, LocalDateTime.now(), messageText, recipient);
    }

    @Override
    public String toString() {
        return "[Chat message: "
                + "Author: " + getAuthorName()
                + ", Recipient: " + getRecipientName()
                + ", message: " + messageText +"]";
    }

    @Override
    public ChatMessageEntity toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new ChatMessageEntity(author, room, getTimeStamp(), recipient, messageText);
    }

}
