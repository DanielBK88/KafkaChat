package volfengaut.chatapp.server.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.server.api.repository.IChatRoomRepository;
import volfengaut.chatapp.server.entity.chat_room.ChatRoom;

@Repository
public class ChatRoomRepository implements IChatRoomRepository {

    private EntityManager entityManager;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public ChatRoom getChatRoom(String name) {
        return entityManager.find(ChatRoom.class, name);
    }

    @Override
    public void addChatRoom(ChatRoom chatRoom) {
        entityManager.persist(chatRoom);
    }

    @Override
    public void deleteChatRoom(ChatRoom chatRoom) {
        entityManager.remove(chatRoom);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
