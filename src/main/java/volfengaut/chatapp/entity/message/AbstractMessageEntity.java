package volfengaut.chatapp.entity.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.message.AbstractMessage;

/**
 * Base class of message entities
 **/
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class AbstractMessageEntity implements Serializable {
    
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
