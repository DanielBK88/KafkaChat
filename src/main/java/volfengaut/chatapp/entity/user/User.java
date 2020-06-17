package volfengaut.chatapp.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.entity.role.Permisson;
import volfengaut.chatapp.entity.role.UserRole;

/**
 * A user of the system
 **/
@NoArgsConstructor
@AllArgsConstructor
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
     * The user role, defining the user's permissions
     **/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE")
    private UserRole role;
    
    /**
     * Check, whether the user has the specified permission according to his user role
     **/
    public boolean hasPermission(Permisson permisson) {
        return role.getPermissons().contains(permisson);
    }
    
}
