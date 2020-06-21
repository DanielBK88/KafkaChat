package volfengaut.chatapp.entity.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.ChatMessage;

/**
 * An entity to persist instances of Kafka chat messages {@link ChatMessage}
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChatMessageEntity extends AbstractMessageEntity implements Serializable {

    /**
     * The recipient of the chat message.
     * Null, if it is a public message
     **/
    @ManyToOne
    @JoinColumn(name = "RECIPIENT")
    private User recipient;

    /**
     * The text of the chat message
     **/
    @Column(name = "TEXT")
    private String text;

    public ChatMessageEntity(User author, ChatRoom room, LocalDateTime time,
            User recipient, String text) {
        super(author, room, time);
        this.recipient = recipient;
        this.text = text;
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ChatMessage(getAuthor().getLoginName(), getRoom().getName(), getTime(),
                text, getRecipient().getLoginName());
    }

}
