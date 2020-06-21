package volfengaut.chatapp.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.IUserRepository;
import volfengaut.chatapp.entity.chat_room.ChatRoom;
import volfengaut.chatapp.entity.chat_room.ChatRoom_;
import volfengaut.chatapp.entity.message.BanMessageEntity;
import volfengaut.chatapp.entity.message.BanMessageEntity_;
import volfengaut.chatapp.entity.message.ChatMessageEntity;
import volfengaut.chatapp.entity.message.ChatMessageEntity_;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.role.UserRole_;
import volfengaut.chatapp.entity.user.User;
import volfengaut.chatapp.entity.user.User_;

@Repository
public class UserRepository implements IUserRepository {

    private EntityManager entityManager;
    
    @Override
    public User getUserByName(String loginName) {
        return entityManager.find(User.class, loginName);
    }

    @Override
    public void deleteUser(User user) {
        entityManager.remove(user);
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public Collection<User> getUsersByRole(UserRole role) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Join<User, UserRole> joinToRoles = root.join(User_.ROLE);
        
        criteriaQuery.multiselect(root.get(User_.LOGIN_NAME), root.get(User_.ROLE), root.get(User_.DATE_JOINED))
                .where(criteriaBuilder.equal(joinToRoles.get(UserRole_.NAME), role.getName()));

        return new HashSet<>(entityManager.createQuery(criteriaQuery).getResultList());
    }

    @Override
    public Collection<User> getAuthorsOfMessagesContaining(String text) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<ChatMessageEntity> root = criteriaQuery.from(ChatMessageEntity.class);
        Join<ChatMessageEntity, User> joinToUsers = root.join(ChatMessageEntity_.AUTHOR);

        criteriaQuery.multiselect(joinToUsers.get(User_.LOGIN_NAME), joinToUsers.get(User_.ROLE), joinToUsers.get(User_.DATE_JOINED))
                .where(criteriaBuilder.like(root.get(ChatMessageEntity_.TEXT), "%" + text + "%"));

        return new HashSet<>(entityManager.createQuery(criteriaQuery).getResultList());
    }

    @Override
    public Collection<User> getUsersJoinedAfter(LocalDate time) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        Path<LocalDate> dateJoinedPath = root.get("dateJoined");
        criteriaQuery.multiselect(root.get("loginName"), root.get("role"), root.get("dateJoined"))
                .where(criteriaBuilder.greaterThanOrEqualTo(dateJoinedPath, time));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Collection<User> getChattersBannedFrom(ChatRoom room) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<BanMessageEntity> root = criteriaQuery.from(BanMessageEntity.class);
        Join<BanMessageEntity, User> joinToUsers = root.join(BanMessageEntity_.BANNED_CHATTER);
        Join<BanMessageEntity, ChatRoom> joinToRooms = root.join(BanMessageEntity_.ROOM);
        
        criteriaQuery.multiselect(joinToUsers.get(User_.LOGIN_NAME), joinToUsers.get(User_.ROLE), 
                joinToUsers.get(User_.DATE_JOINED))
                
                .where(criteriaBuilder.equal(joinToRooms.get(ChatRoom_.NAME), room.getName()));

        return new HashSet<>(entityManager.createQuery(criteriaQuery).getResultList());
    }

    @Override
    public Collection<User> getChattersBannedBy(User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<BanMessageEntity> root = criteriaQuery.from(BanMessageEntity.class);
        Join<BanMessageEntity, User> joinToRecipients = root.join(BanMessageEntity_.BANNED_CHATTER);
        Join<BanMessageEntity, User> joinToAuthors = root.join(BanMessageEntity_.AUTHOR);

        criteriaQuery.multiselect(joinToRecipients.get(User_.LOGIN_NAME), joinToRecipients.get(User_.ROLE), 
                joinToRecipients.get(User_.DATE_JOINED))
                
                .where(criteriaBuilder.equal(joinToAuthors.get(User_.LOGIN_NAME), user.getLoginName()));

        return new HashSet<>(entityManager.createQuery(criteriaQuery).getResultList());
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
