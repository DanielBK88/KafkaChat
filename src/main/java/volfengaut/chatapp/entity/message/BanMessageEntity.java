package volfengaut.chatapp.entity.message;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.ChatterBannedMessage;

/**
 * An entity to persist instances of Kafka chat messages {@link ChatterBannedMessage}
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BanMessageEntity extends AbstractMessageEntity {

    /**
     * The chatter banned by this message
     **/
    @ManyToOne
    @JoinColumn(name = "BANNED_CHATTER")
    private User bannedChatter;

    /**
     * The reason of banning
     **/
    @Column(name = "REASON")
    private String reason;

    public BanMessageEntity(User author, ChatRoom room, LocalDateTime time,
            User bannedChatter, String reason) {
        super(author, room, time);
        this.bannedChatter = bannedChatter;
        this.reason = reason;
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ChatterBannedMessage(getAuthor().getLoginName(), getRoom().getName(), getTime(),
                reason, bannedChatter.getLoginName());
    }

}
