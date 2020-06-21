package volfengaut.chatapp.entity.settings;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entity holding developer settings
 **/
@Entity
@NoArgsConstructor
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeveloperSettings extends Settings implements Serializable {
    
    /**
     * The developer's full name
     **/
    @Column(name = "FULL_NAME")
    private String fullName;
    
    /**
     * The developer's mail address
     **/
    @Column
    private String mailAddress;
    
}
