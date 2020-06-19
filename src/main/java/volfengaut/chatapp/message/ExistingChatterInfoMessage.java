package volfengaut.chatapp.message;

import java.time.LocalDateTime;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.user.User;

import static volfengaut.chatapp.entity.message.MessageType.EXISTING_CHATTER_INFO;

/**
 * A message, informing a new entering chatter about chatters, which already exist in the given chat room.
 **/
public class ExistingChatterInfoMessage extends AbstractMessage {

    public ExistingChatterInfoMessage(String authorName, String chatRoomName, LocalDateTime timeStamp) {
        super(authorName, chatRoomName, timeStamp);
    }

    @Override
    public String toString() {
        return "Existing chatter Info: " + getAuthorName();
    }

    @Override
    public Message toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new Message(EXISTING_CHATTER_INFO, author, recipient, room, null, null, getTimeStamp());
    }

}
