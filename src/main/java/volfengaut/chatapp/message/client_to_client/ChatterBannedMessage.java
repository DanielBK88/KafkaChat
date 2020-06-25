package volfengaut.chatapp.message.client_to_client;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.message.BanMessageEntity;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;

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
     * Filled by the server before resending the ban message to the banned user. Used only to inform him
     * about available other rooms.
     * Represents a list of room names, separated by CommonConstants.DATA_DELIMITER
     **/
    @Setter
    private String availableOtherChatRooms;

    public ChatterBannedMessage(String authorName, String chatRoomName, LocalDateTime timeStamp,
            String banReason, String bannedChatterName) {
        super(authorName, bannedChatterName, chatRoomName, timeStamp);
        this.banReason = banReason;
    }
    
    public ChatterBannedMessage(String banReason, String recipient) {
        this(null, null, LocalDateTime.now(), banReason, recipient);
    }
    
    @Override
    public BanMessageEntity toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new BanMessageEntity(author, room, getTimeStamp(), recipient, banReason);
    }

    @Override
    public String toString() {
        return "[Ban message: "
                + "Author: " + getAuthorName()
                + ", Recipient: " + getRecipientName()
                + ", Reason: " + banReason +"]";
    }
}
