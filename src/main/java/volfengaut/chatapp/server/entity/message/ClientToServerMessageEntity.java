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
import volfengaut.chatapp.message.client_to_server.ClientToServerMessage;
import volfengaut.chatapp.message.client_to_server.ClientToServerMessageType;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;

import static javax.persistence.EnumType.STRING;

/**
 * An entity to persist messages, send by a client to the server {@link ClientToServerMessage}
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_CLIENT_TO_SERVER_MSG")
public class ClientToServerMessageEntity extends AbstractMessageEntity implements Serializable {

    @Column(name = "MESSAGE_TYPE")
    @Enumerated(STRING)
    private ClientToServerMessageType messageType;

    @Column(name = "DATA")
    private String data;
    
    @Column(name = "AUTHOR_ADDRESS")
    private String authorAddress;

    public ClientToServerMessageEntity(User author, LocalDateTime time,
            ClientToServerMessageType messageType, String data, String authorAddress) {
        super(author, null, null, time);
        this.messageType = messageType;
        this.data = data;
        this.authorAddress = authorAddress;
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ClientToServerMessage(getAuthor().getLoginName(), getTime(), data, messageType, authorAddress);
    }

}
