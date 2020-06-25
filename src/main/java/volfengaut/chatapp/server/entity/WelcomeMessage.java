package volfengaut.chatapp.server.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The translated welcome message for the user
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_WELCOME_MESSAGE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WelcomeMessage {

    @Id
    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "MESSAGE")
    private String message;
}
