package volfengaut.chatapp.server.entity.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.client_to_client.ChatterBannedMessage;

/**
 * An entity to persist instances of Kafka chat messages {@link ChatterBannedMessage}
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_BAN_MESSAGE")
public class BanMessageEntity extends AbstractMessageEntity implements Serializable {
    
    /**
     * The reason of banning
     **/
    @Column(name = "REASON")
    private String reason;

    public BanMessageEntity(User author, ChatRoom room, LocalDateTime time,
            User bannedChatter, String reason) {
        super(author, bannedChatter, room, time);
        this.reason = reason;
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ChatterBannedMessage(getAuthor().getLoginName(), getRoom().getName(), getTime(),
                reason, getRecipient().getLoginName());
    }

}
