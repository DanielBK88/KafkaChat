package volfengaut.chatapp.message;

import lombok.Getter;

/**
 * Message send by an admin to ban a chatter from a chat room
 **/
public class ChatterBannedMessage extends AbstractMessage {

    @Getter
    private final String reason;
    
    @Getter
    private final String bannedChatterName;
    
    public ChatterBannedMessage(String authorName, String reason, String bannedChatterName) {
        super(authorName);
        this.reason = reason;
        this.bannedChatterName = bannedChatterName;
    }

}
