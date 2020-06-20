package volfengaut.chatapp.entity.message;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.ChatterStatusChange;
import volfengaut.chatapp.message.ChatterStatusChangeMessage;

import static javax.persistence.EnumType.STRING;

/**
 * An entity to persist instances of Kafka chat messages {@link ChatterStatusChangeMessage}
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
public class StatusChangeMessageEntity extends AbstractMessageEntity {

    /**
     * The status change that happened to the author (entering / leaving a chat room)
     **/
    @Column(name = "AUTHOR_STATUS_CHANGE")
    @Enumerated(STRING)
    private ChatterStatusChange statusChange;

    public StatusChangeMessageEntity(User author, ChatRoom room, LocalDateTime time, ChatterStatusChange statusChange) {
        super(author, room, time);
        this.statusChange = statusChange;
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ChatterStatusChangeMessage(getAuthor().getLoginName(), getRoom().getName(), getTime(), 
                getStatusChange());
    }

}
