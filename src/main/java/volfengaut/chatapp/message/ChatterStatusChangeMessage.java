package volfengaut.chatapp.message;

import java.time.LocalDateTime;
import lombok.Getter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.user.User;

import static volfengaut.chatapp.entity.message.MessageType.STATUS_CHANGE;

/**
 * A message, which informs about a status change, which happened to it's author
 * (he entered or left the chat room)
 **/
@Getter
public class ChatterStatusChangeMessage extends AbstractMessage {

    public ChatterStatusChangeMessage(String authorName, String chatRoomName, LocalDateTime timeStamp,
            ChatterStatusChange statusChange) {
        super(authorName, chatRoomName, timeStamp);
        this.statusChange = statusChange;
    }

    /**
     * The author's status change to inform other chatters about
     **/
    private ChatterStatusChange statusChange;

    @Override
    public String toString() {
        return "Status change message: " + getAuthorName() + " " + statusChange;
    }

    @Override
    public Message toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new Message(STATUS_CHANGE, author, recipient, room, null, null, getTimeStamp());
    }
}
