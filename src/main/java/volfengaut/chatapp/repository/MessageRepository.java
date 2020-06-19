package volfengaut.chatapp.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.IMessageRepository;
import volfengaut.chatapp.entity.message.Message;
import volfengaut.chatapp.entity.message.MessageType;
import volfengaut.chatapp.entity.user.User;

import static volfengaut.chatapp.entity.message.MessageType.BAN;

@Repository
public class MessageRepository implements IMessageRepository {

    private EntityManager entityManager;
    
    @Override
    public void addMessage(Message message) {
        entityManager.persist(message);
    }

    @Override
    public Collection<Message> findMessagesSendBy(User user) {
        String queryString = 
                "SELECT m from Message m " 
                        + "JOIN m.author a "
                        + "WHERE a.loginName = :namePlaceHolder";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("namePlaceHolder", user.getLoginName());
        return query.getResultList();
    }

    @Override
    public Collection<Message> findMessagesOfType(MessageType type) {
        String queryString =
                "SELECT m from Message m " 
                        + "WHERE m.type = :typePlaceHolder";
        
        Query query = entityManager.createQuery(queryString);
        query.setParameter("typePlaceHolder", type);
        return query.getResultList();
    }

    @Override
    public int countBansOfUser(User user) {
        String queryString =
                "SELECT m from Message m "
                        + "JOIN m.recipient r "
                        + "WHERE r.loginName = :namePlaceHolder AND m.type = :typePlaceHolder";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("typePlaceHolder", BAN);
        query.setParameter("namePlaceHolder", user.getLoginName());
        return query.getResultList().size();
    }

    @Override
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

}
