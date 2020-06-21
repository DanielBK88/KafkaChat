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
 * Entity holding application settings
 **/
@Entity
@NoArgsConstructor
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationSettings extends Settings implements Serializable {

    /**
     * Name of the operation system
     **/
    @Column(name = "OS_NAME")
    private String osName;
    
    /**
     * Memory required for the application in megabytes
     **/
    @Column(name = "MEMORY_MB")
    private int memory;
    
}
