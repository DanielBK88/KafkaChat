package volfengaut.chatapp.entity.settings;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Base entity of settings. To be subclassed by specialized setting entities.
 **/
@Entity
@NoArgsConstructor
@Table(name = "TB_SETTINGS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Settings implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    
}
