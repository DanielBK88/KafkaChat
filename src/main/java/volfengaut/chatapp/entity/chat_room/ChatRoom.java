package volfengaut.chatapp.entity.chat_room;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.entity.user.User;

/**
 * A chat room, permitting a public chat to it's participants
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_ROOM")
public class ChatRoom implements Serializable {

    /**
     * The name of the chat room (and of the corresponding Kafka topic)
     **/
    @Id
    @Column(name = "ROOM_NAME")
    private String name;
    
    /**
     * The name of the user, who created this chat room
     **/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CREATOR_NAME")
    private User creator;

    public ChatRoom(String name, User creator) {
        this.name = name;
        this.creator = creator;
    }

}
