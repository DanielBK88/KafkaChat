package volfengaut.chatapp.message;

import java.time.LocalDateTime;
import lombok.Getter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.user.User;

import static volfengaut.chatapp.entity.message.MessageType.CHAT;

/**
 * An implementation of {@link AbstractMessage} representing a chat message
 **/
@Getter
public class ChatMessage extends AbstractMessage {

    /**
     * The text of the message
     **/
    private String messageText;
    
    /**
     * The name of the recipient of the message.
     * If specified, the message will be delivered as a private message to this recipient only.
     * If null, the message will be delivered to all chatters in the chat room.
     **/
    private String recipient;

    public ChatMessage(String authorName, String chatRoomName, LocalDateTime timeStamp,
            String messageText, String recipient) {
        super(authorName, chatRoomName, timeStamp);
        this.messageText = messageText;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "ChatMessage from " + getAuthorName() + " to " 
                + (recipient == null ? "all" : recipient) + ": " + messageText;
    }

    @Override
    public Message toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new Message(CHAT, author, recipient, room, messageText, null, getTimeStamp());
    }

}
