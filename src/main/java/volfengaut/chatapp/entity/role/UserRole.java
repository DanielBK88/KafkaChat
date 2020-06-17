package volfengaut.chatapp.entity.role;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import volfengaut.chatapp.entity.user.User;

/**
 * A user role within the system
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_ROLE")
public class UserRole {
    
    /**
     * The name of the role
     **/
    @Id
    @Column(name = "ROLE_NAME")
    private String name;
    
    /**
     * Permissions, which are included in this role
     **/
    @ElementCollection(targetClass = Permisson.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "TB_ROLE_PERMISSION",
            joinColumns = @JoinColumn(name = "ROLE"))
    @Column(name = "PERMISSION_ID")
    private Set<Permisson> permissons;
    
    /**
     * Users, who have this role
     **/
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private Set<User> users;

}
