package volfengaut.chatapp.entity.message;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.ExistingChatterInfoMessage;

/**
 * An entity to persist instances of Kafka chatter info messages {@link ExistingChatterInfoMessage},
 * informing new entering chatters about already existing chatters
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ExistingChatterInfoMessageEntity extends AbstractMessageEntity {

    public ExistingChatterInfoMessageEntity(User author,
            ChatRoom room, LocalDateTime time) {
        super(author, room, time);
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ExistingChatterInfoMessage(getAuthor().getLoginName(), getRoom().getName(), getTime());
    }

}
