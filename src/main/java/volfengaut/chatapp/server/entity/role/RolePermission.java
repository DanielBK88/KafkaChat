package volfengaut.chatapp.server.entity.role;

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

import static javax.persistence.EnumType.STRING;

/**
 * Defines the mapping of user roles to permissions
 **/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_ROLE_PERMISSION")
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
