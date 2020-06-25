package volfengaut.chatapp.server.entity.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessage;
import volfengaut.chatapp.message.server_to_client.ServerToClientMessageType;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;

import static javax.persistence.EnumType.STRING;

/**
 * An entity to persist messages, send by the server to clients {@link ServerToClientMessage}
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_SERVER_TO_CLIENT_MSG")
public class ServerToClientMessageEntity extends AbstractMessageEntity implements Serializable {
    
    @Column(name = "MESSAGE_TYPE")
    @Enumerated(STRING)
    private ServerToClientMessageType messageType;
    
    @Column(name = "DATA")
    private String data;
    
    @Column(name = "RECIPIENT_ADDRESS")
    private String recipientAddress;

    public ServerToClientMessageEntity(User recipient, LocalDateTime time, 
            ServerToClientMessageType messageType, String data, String recipientAddress) {
        super(null, recipient, null, time);
        this.messageType = messageType;
        this.data = data;
        this.recipientAddress = recipientAddress;
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ServerToClientMessage(messageType, getRecipient().getLoginName(), getTime(), data, recipientAddress);
    }

}
