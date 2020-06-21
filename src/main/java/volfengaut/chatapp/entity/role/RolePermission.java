package volfengaut.chatapp.entity.role;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import static javax.persistence.EnumType.STRING;

/**
 * Defines the mapping of user roles to permissions
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_ROLE_PERMISSION")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RolePermission {
    
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_NAME")
    private UserRole role;

    @Column(name = "PERMISSION")
    @Enumerated(STRING)
    private Permission permission;

    public RolePermission(UserRole role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }

}
