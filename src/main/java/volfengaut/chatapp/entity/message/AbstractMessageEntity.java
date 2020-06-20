package volfengaut.chatapp.entity.message;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
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
 * Base class of message entities
 **/
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractMessageEntity {
    
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "AUTHOR")
    private User author;

    @ManyToOne
    @JoinColumn(name = "ROOM")
    private ChatRoom room;
    
    @Column(name = "TIME")
    private LocalDateTime time;

    public AbstractMessageEntity(User author, ChatRoom room, LocalDateTime time) {
        this.author = author;
        this.room = room;
        this.time = time;
    }

    public abstract AbstractMessage toKafkaMessage();
    
}
