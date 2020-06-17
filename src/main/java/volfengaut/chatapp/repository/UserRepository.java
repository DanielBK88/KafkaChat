package volfengaut.chatapp.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.IUserRepository;
import volfengaut.chatapp.entity.role.UserRole;
import volfengaut.chatapp.entity.user.User;

@Repository
public class UserRepository implements IUserRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public User getUserByName(String loginName) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        User user = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            
            user = em.find(User.class, loginName);
            
            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return user;
    }

    @Override
    public boolean deleteUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            em.remove(user);

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

    @Override
    public User addUser(String name, UserRole role) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        User user = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            user = new User(name, role);
            em.persist(user);

            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return user;
    }

}
