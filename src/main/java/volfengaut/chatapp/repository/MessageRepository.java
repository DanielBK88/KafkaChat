package volfengaut.chatapp.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.IMessageRepository;
import volfengaut.chatapp.entity.WelcomeMessage;
import volfengaut.chatapp.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.entity.message.ChatMessageEntity;
import volfengaut.chatapp.entity.user.User;


@Repository
public class MessageRepository implements IMessageRepository {

    private EntityManager entityManager;
    
    @Override
    public void addMessage(AbstractMessageEntity message) {
        entityManager.persist(message);
    }

    @Override
    public Collection<ChatMessageEntity> findMessagesSendBy(User user) {
        String queryString = 
                "SELECT m from ChatMessageEntity m " 
                        + "JOIN m.author a "
                        + "WHERE a.loginName = :namePlaceHolder";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("namePlaceHolder", user.getLoginName());
        return query.getResultList();
    }

    @Override
    public <T extends AbstractMessageEntity> Collection<T> findMessagesOfType(Class<T> messageClass) {
        String queryString = "SELECT m from " + messageClass.getSimpleName() +  " m";
        
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Override
    public int countBansOfUser(User user) {
        String queryString =
                "SELECT m from BanMessageEntity m "
                        + "JOIN m.bannedChatter r "
                        + "WHERE r.loginName = :namePlaceHolder";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("namePlaceHolder", user.getLoginName());
        return query.getResultList().size();
    }

    @Override
    public WelcomeMessage getWelcomeMessage(String language) {
        return entityManager.find(WelcomeMessage.class, language);
    }

    @Override
    public void setWelcomeMessage(WelcomeMessage message) {
        entityManager.merge(message);
    }

    @Override
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

}
