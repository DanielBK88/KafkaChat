package volfengaut.chatapp.message;

import java.time.LocalDateTime;
import lombok.Getter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.user.User;

import static volfengaut.chatapp.entity.message.MessageType.BAN;

/**
 * Message send by an admin to ban a chatter from a chat room
 **/
@Getter
public class ChatterBannedMessage extends AbstractMessage {

    /**
     * The reason of banning the chatter
     **/
    private String banReason;
    
    /**
     * The name of the banned chatter
     **/
    private String bannedChatterName;

    public ChatterBannedMessage(String authorName, String chatRoomName, LocalDateTime timeStamp,
            String banReason, String bannedChatterName) {
        super(authorName, chatRoomName, timeStamp);
        this.banReason = banReason;
        this.bannedChatterName = bannedChatterName;
    }

    @Override
    public Message toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new Message(BAN, author, recipient, room, null, null, getTimeStamp());
    }
}
