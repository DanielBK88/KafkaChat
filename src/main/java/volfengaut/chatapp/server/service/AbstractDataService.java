package volfengaut.chatapp.server.service;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import volfengaut.chatapp.server.api.repository.EntityManagerDependent;
import volfengaut.chatapp.server.entity.WelcomeMessage;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;
import volfengaut.chatapp.server.entity.message.AbstractMessageEntity;
import volfengaut.chatapp.server.entity.role.UserRole;
import volfengaut.chatapp.server.entity.user.User;

public abstract class AbstractDataService {

    @Setter(onMethod=@__({@Autowired}))
    private EntityManagerFactory entityManagerFactory;
    
    protected <I, O> O doInTransaction(Function<I,O> action, I input, EntityManagerDependent repository) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        O result = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            
            repository.setEntityManager(em);
            result = action.apply(input);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
    
    protected <I> void doInTransaction(Consumer<I> action, I input, EntityManagerDependent repository) {
        doInTransaction(i -> {
            action.accept(input);
            return null;
        }, input, repository);
    }
    
    protected <O> O doInTransaction(Supplier<O> action, EntityManagerDependent repository) {
        return doInTransaction(i -> {
            return action.get();
        }, null, repository);
    }

    protected void checkRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("The role should not be null!");
        } else if (StringUtils.isEmpty(role.getName())) {
            throw new IllegalArgumentException("The role's name should not be null or empty!");
        } else if (CollectionUtils.isEmpty(role.getPermissons())) {
            throw new IllegalArgumentException("The role should have at least 1 permission!");
        }
    }

    protected void checkUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("The user should not be null!");
        } else if (org.springframework.util.StringUtils.isEmpty(user.getLoginName())) {
            throw new IllegalArgumentException("The user's name should not be null or empty!");
        } 
        checkRole(user.getRole());
    }
    
    protected void checkRoom(ChatRoom room) {
        if (StringUtils.isEmpty(room.getName())) {
            throw new IllegalArgumentException("The room name should not be null!");
        }
        checkUser(room.getCreator());
    }
    
    protected void checkMessage(AbstractMessageEntity message) {
        if (message == null) {
            throw new IllegalArgumentException("The message should not be null!");
        } 
    }

    protected void checkRoleName(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            throw new IllegalArgumentException("The role's name should not be null or empty!");
        }
    }

    protected void checkUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new IllegalArgumentException("The user's name should not be null or empty!");
        }
    }

    protected void checkRoomName(String roomName) {
        if (StringUtils.isEmpty(roomName)) {
            throw new IllegalArgumentException("The room name should not be null or empty!");
        }
    }
    
    protected void checkText(String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("The text should not not be null or empty!");
        }
    }
    
    protected void chekWelcomeMessage(WelcomeMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("The message not not be null!");
        } else if (StringUtils.isEmpty(message.getMessage())) {
            throw new IllegalArgumentException("The text of the message should not not be null or empty!");
        } else if (StringUtils.isEmpty(message.getLanguage())) {
            throw new IllegalArgumentException("The language should not not be null or empty!");
        }
    }

    protected void checkDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("The date should not be null!");
        }
    }
}
