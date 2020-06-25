package volfengaut.chatapp.server.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.message.AbstractMessage;
import volfengaut.chatapp.server.api.repository.IMessageRepository;
import volfengaut.chatapp.server.entity.WelcomeMessage;
import volfengaut.chatapp.server.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.server.entity.message.ChatMessageEntity;
import volfengaut.chatapp.server.entity.message.ClientToServerMessageEntity;
import volfengaut.chatapp.server.entity.message.ServerToClientMessageEntity;
import volfengaut.chatapp.server.entity.user.User;
import java.util.List;


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
    public void updateServerMessagesForUser(User user) {
        String queryMsgsToUserString = "SELECT m from ServerToClientMessageEntity m "
                + "WHERE m.recipientAddress = :addrPlaceHolder";
        
        Query queryMsgsToUser = entityManager.createQuery(queryMsgsToUserString);
        queryMsgsToUser.setParameter("addrPlaceHolder", user.getCurrentInetAddress());
        
        List msgsToUser = queryMsgsToUser.getResultList();

        String queryMsgsFromUserString = "SELECT m from ClientToServerMessageEntity m "
                + "WHERE m.authorAddress = :addrPlaceHolder2";

        Query queryMsgsFromUser = entityManager.createQuery(queryMsgsFromUserString);
        queryMsgsFromUser.setParameter("addrPlaceHolder2", user.getCurrentInetAddress());

        List msgsFromUser = queryMsgsFromUser.getResultList();
        
        for (Object o : msgsToUser) {
            ServerToClientMessageEntity msg = (ServerToClientMessageEntity) o;
            msg.setRecipient(user);
            entityManager.merge(msg);
        }

        for (Object o : msgsFromUser) {
            ClientToServerMessageEntity msg = (ClientToServerMessageEntity) o;
            msg.setAuthor(user);
            entityManager.merge(msg);
        }
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
