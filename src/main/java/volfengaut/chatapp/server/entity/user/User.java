package volfengaut.chatapp.server.entity.user;

import java.time.LocalDate;
import java.util.function.Function;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.role.Permission;
import volfengaut.chatapp.server.entity.role.RolePermission;
import volfengaut.chatapp.server.entity.role.UserRole;

/**
 * A user of the system
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_USER")
public class User {
    
    /**
     * The user name
     **/
    @Id
    @Column(name = "LOGIN_NAME")
    private String loginName;
    
    /**
     * The user's password
     **/
    @Column(name = "PASSWORD")
    private String password;
    
    /**
     * The user role, defining the user's permissions
     **/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE")
    private UserRole role;
    
    /**
     * The date when the user signed up to the system
     **/
    @Column(name = "DATE_JOINED")
    private LocalDate dateJoined;
    
    /**
     * The chat room, which the user is currently participating (if any). Not stored to DB.
     **/
    @Transient
    private ChatRoom currentRoom;
    
    /**
     * The current host name and port of the client. Not stored to DB.
     **/
    @Transient
    private String currentInetAddress;

    public User(String loginName, String password, UserRole role, LocalDate dateJoined) {
        this.loginName = loginName;
        this.password = password;
        this.role = role;
        this.dateJoined = dateJoined;
    }

    @Override
    public int hashCode() {
        return loginName.hashCode() * role.getName().hashCode() + 17;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return user.getRole().getName().equals(role.getName())
                && user.getLoginName().equals(loginName);
    }

    /**
     * Check, whether the user has the specified permission according to his user role
     **/
    public boolean hasPermission(Permission permisson) {
        return role.getPermissons().stream()
                .map(RolePermission::getPermission)
                .anyMatch(p -> p.equals(permisson));
    }
    
}
