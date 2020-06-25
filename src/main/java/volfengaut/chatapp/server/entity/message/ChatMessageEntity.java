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
import volfengaut.chatapp.message.client_to_client.ChatMessage;

/**
 * An entity to persist instances of Kafka chat messages {@link ChatMessage}
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_CHAT_MESSAGE")
public class ChatMessageEntity extends AbstractMessageEntity implements Serializable {

    /**
     * The text of the chat message
     **/
    @Column(name = "TEXT")
    private String text;

    public ChatMessageEntity(User author, ChatRoom room, LocalDateTime time,
            User recipient, String text) {
        super(author, recipient, room, time);
        this.text = text;
    }

    @Override
    public AbstractMessage toKafkaMessage() {
        return new ChatMessage(getAuthor().getLoginName(), getRoom().getName(), getTime(),
                text, getRecipient().getLoginName());
    }

}
