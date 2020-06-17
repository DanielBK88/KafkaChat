package volfengaut.chatapp.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.IChatRoomRepository;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.user.User;

@Repository
public class ChatRoomRepository implements IChatRoomRepository {
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public ChatRoom getChatRoom(String name) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        ChatRoom room = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            room = em.find(ChatRoom.class, name);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return room;
    }

    @Override
    public ChatRoom addChatRoom(String name, User creator) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        ChatRoom room = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            room = new ChatRoom(name, creator);
            em.persist(room);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return room;
    }

    @Override
    public boolean deleteChatRoom(ChatRoom chatRoom) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            em.remove(chatRoom);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
        return true;
    }

}
