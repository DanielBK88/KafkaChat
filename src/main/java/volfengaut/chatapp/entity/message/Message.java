package volfengaut.chatapp.entity.message;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.message.ChatMessage;
import volfengaut.chatapp.message.ChatterBannedMessage;
import volfengaut.chatapp.message.ChatterStatusChange;
import volfengaut.chatapp.message.ChatterStatusChangeMessage;
import volfengaut.chatapp.message.ExistingChatterInfoMessage;

import static javax.persistence.EnumType.STRING;

/**
 * An entity representing messages persisted in the database.
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_MESSAGE")
public class Message {
    
    @Id
    @GeneratedValue
    private long id;
    
    @Column(name = "TYPE")
    @Enumerated(STRING)
    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "AUTHOR")
    private User author;

    @ManyToOne
    @JoinColumn(name = "RECIPIENT")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "ROOM")
    private ChatRoom room;
    
    @Column(name = "TEXT")
    private String text;
    
    @Column(name = "AUTHOR_STATUS_CHANGE")
    @Enumerated(STRING)
    private ChatterStatusChange statusChange;
    
    @Column(name = "TIME")
    private LocalDateTime time;

    public Message(MessageType type, User author, User recipient, ChatRoom room, String text,
            ChatterStatusChange statusChange, LocalDateTime time) {
        this.type = type;
        this.author = author;
        this.recipient = recipient;
        this.room = room;
        this.text = text;
        this.statusChange = statusChange;
        this.time = time;
    }

    public AbstractMessage toKafkaMessage(){
        switch (type) {
            case CHAT:
                return new ChatMessage(author.getLoginName(), room.getName(), time, text, recipient.getLoginName());
            case STATUS_CHANGE:
                return new ChatterStatusChangeMessage(author.getLoginName(), room.getName(), time, statusChange);
            case EXISTING_CHATTER_INFO:
                return new ExistingChatterInfoMessage(author.getLoginName(), room.getName(), time);
            case BAN:
                return new ChatterBannedMessage(
                        author.getLoginName(), room.getName(), time, text, recipient.getLoginName());
            default:
                throw new IllegalStateException("Unsupported message type: " + type + "!");
        }
    }
}
