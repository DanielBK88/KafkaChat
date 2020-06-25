package volfengaut.chatapp.message.server_to_client;

import java.time.LocalDateTime;
import lombok.Getter;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.server.entity.message.ServerToClientMessageEntity;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;

/**
 * A message send by the server to a client to request a certain information as answer.
 **/
@Getter
public class ServerToClientMessage extends AbstractMessage {
    
    /**
     * The type of this message
     **/
    private ServerToClientMessageType messageType;
    
    /**
     * The data, provided by the server to the client with this message (if any)
     **/
    private String providedData;
    
    /**
     * The host name and port of the recipient client
     **/
    private String recipientAddress;

    public ServerToClientMessage(ServerToClientMessageType messageType, String recipientName, LocalDateTime timeStamp,
            String providedData, String recipientAddress) {
        super(null, recipientName, null, timeStamp);
        this.messageType = messageType;
        this.providedData = providedData;
        this.recipientAddress = recipientAddress;
    }

    @Override
    public AbstractMessageEntity toPersistableMessage(User author, User recipient, ChatRoom room) {
        return new ServerToClientMessageEntity(recipient, getTimeStamp(), messageType, providedData, recipientAddress);
    }

    @Override
    public String toString() {
        return "[Message from server to client: " 
                + "Type: " + messageType 
                + ", Recipient: " + getRecipientName() 
                + ", Data: " + providedData +"]";
    }

}
