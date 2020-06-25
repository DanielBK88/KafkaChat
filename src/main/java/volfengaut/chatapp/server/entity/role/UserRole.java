package volfengaut.chatapp.server.entity.role;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import volfengaut.chatapp.server.entity.user.User;

/**
 * A user role within the system
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_ROLE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role", cascade = CascadeType.ALL)
    private Set<RolePermission> permissons;
    
    /**
     * Users, who have this role
     **/
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private transient Set<User> users;

    public UserRole(String name, Permission... permissions) {
        this.name = name;
        this.permissons = Arrays.stream(permissions)
                .map(p -> new RolePermission(this, p))
                .collect(Collectors.toSet());
    }
}
