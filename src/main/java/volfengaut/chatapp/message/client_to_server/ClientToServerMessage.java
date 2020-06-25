package volfengaut.chatapp.message.client_to_server;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.server.entity.message.ClientToServerMessageEntity;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;

/**
 * A message send by a client to the server
 **/
@Getter
public class ClientToServerMessage extends AbstractMessage {
    
    /**
     * The data sent to the server
     **/
    private String data;
    
    /**
     * The type of the sent message
     **/
    private ClientToServerMessageType messageType;
    
    /**
     * The host name and port of the author client
     **/
    private String authorAddress;

    public ClientToServerMessage(String authorName, LocalDateTime timeStamp, String data,
            ClientToServerMessageType messageType, String authorAddress) {
        super(authorName, null, null, timeStamp);
        this.data = data;
        this.messageType = messageType;
        this.authorAddress = authorAddress;
    }
    
    public ClientToServerMessage(ClientToServerMessageType messageType, String data) {
        this(null, LocalDateTime.now(), data, messageType, null);
    }

    @Override
    public AbstractMessageEntity toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new ClientToServerMessageEntity(author, getTimeStamp(), messageType, data, authorAddress);
    }

    @Override
    public String toString() {
        return "[Message from client to server: "
                + "Type: " + messageType
                + ", Author: " + getAuthorName()
                + ", Data: " + data +"]";
    }
}
